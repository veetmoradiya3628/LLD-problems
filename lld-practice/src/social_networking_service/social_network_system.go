package main

import (
	"fmt"
	"sort"
	"strings"
	"sync"
	"sync/atomic"
)

type SocialNetwork struct {
	users          UserRepository
	posts          PostRepository
	friendRequests FriendRequestRepository
	notifications  NotificationRepository
	mu             sync.Mutex
	nextID         atomic.Uint64
}

var (
	instance *SocialNetwork
	once     sync.Once
)

func NewSocialNetwork(
	users UserRepository,
	posts PostRepository,
	friendRequests FriendRequestRepository,
	notifications NotificationRepository,
) (*SocialNetwork, error) {
	if users == nil || posts == nil || friendRequests == nil || notifications == nil {
		return nil, fmt.Errorf("%w: repositories are required", ErrInvalidSocialNetwork)
	}
	return &SocialNetwork{
		users:          users,
		posts:          posts,
		friendRequests: friendRequests,
		notifications:  notifications,
	}, nil
}

func GetSocialNetwork() *SocialNetwork {
	once.Do(func() {
		network, err := NewSocialNetwork(
			NewInMemoryUserRepository(),
			NewInMemoryPostRepository(),
			NewInMemoryFriendRequestRepository(),
			NewInMemoryNotificationRepository(),
		)
		if err != nil {
			panic(err)
		}
		instance = network
	})
	return instance
}

func (sn *SocialNetwork) RegisterUser(user *User) error {
	if user == nil || strings.TrimSpace(user.ID) == "" ||
		strings.TrimSpace(user.Name) == "" || strings.TrimSpace(user.Email) == "" ||
		strings.TrimSpace(user.Password) == "" {
		return fmt.Errorf("%w: user ID, name, email, and password are required", ErrInvalidSocialNetwork)
	}

	sn.mu.Lock()
	defer sn.mu.Unlock()
	if _, exists := sn.users.FindUser(user.ID); exists {
		return fmt.Errorf("%w: user ID %s already exists", ErrUserAlreadyExists, user.ID)
	}
	email := strings.ToLower(strings.TrimSpace(user.Email))
	if _, exists := sn.users.FindUserByEmail(email); exists {
		return fmt.Errorf("%w: email %s is already registered", ErrUserAlreadyExists, email)
	}
	user.Email = email
	sn.users.SaveUser(user)
	return nil
}

func (sn *SocialNetwork) LoginUser(email, password string) (*User, error) {
	user, exists := sn.users.FindUserByEmail(strings.ToLower(strings.TrimSpace(email)))
	if !exists || user.Password != password {
		return nil, ErrInvalidCredentials
	}
	return user, nil
}

func (sn *SocialNetwork) UpdateUserProfile(user *User) error {
	if user == nil || strings.TrimSpace(user.ID) == "" || strings.TrimSpace(user.Name) == "" {
		return fmt.Errorf("%w: user ID and name are required", ErrInvalidSocialNetwork)
	}

	sn.mu.Lock()
	defer sn.mu.Unlock()
	current, exists := sn.users.FindUser(user.ID)
	if !exists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, user.ID)
	}
	email := strings.ToLower(strings.TrimSpace(user.Email))
	if email != "" {
		if other, found := sn.users.FindUserByEmail(email); found && other.ID != user.ID {
			return fmt.Errorf("%w: email %s is already registered", ErrUserAlreadyExists, email)
		}
	}

	current.mu.Lock()
	current.Name = user.Name
	current.ProfilePicture = user.ProfilePicture
	current.Bio = user.Bio
	if email != "" {
		current.Email = email
	}
	current.mu.Unlock()
	sn.users.SaveUser(current)
	return nil
}

func (sn *SocialNetwork) SendFriendRequest(senderID, receiverID string) error {
	if senderID == "" || receiverID == "" || senderID == receiverID {
		return fmt.Errorf("%w: distinct sender and receiver IDs are required", ErrInvalidFriendRequest)
	}

	sn.mu.Lock()
	defer sn.mu.Unlock()
	sender, senderExists := sn.users.FindUser(senderID)
	_, receiverExists := sn.users.FindUser(receiverID)
	if !senderExists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, senderID)
	}
	if !receiverExists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, receiverID)
	}
	if sender.IsFriend(receiverID) {
		return ErrAlreadyFriends
	}
	if request, exists := sn.friendRequests.FindFriendRequest(senderID, receiverID); exists &&
		request.Status == FriendRequestPending {
		return ErrFriendRequestAlreadyPending
	}
	if request, exists := sn.friendRequests.FindFriendRequest(receiverID, senderID); exists &&
		request.Status == FriendRequestPending {
		return ErrFriendRequestAlreadyPending
	}

	request := NewFriendRequest(sn.newID("FR"), senderID, receiverID)
	sn.friendRequests.SaveFriendRequest(request)
	sn.addNotification(receiverID, NotificationTypeFriendRequest,
		fmt.Sprintf("Friend request from %s", senderID))
	return nil
}

func (sn *SocialNetwork) AcceptFriendRequest(userID, friendID string) error {
	return sn.respondToFriendRequest(userID, friendID, FriendRequestAccepted)
}

func (sn *SocialNetwork) DeclineFriendRequest(userID, friendID string) error {
	return sn.respondToFriendRequest(userID, friendID, FriendRequestDeclined)
}

func (sn *SocialNetwork) respondToFriendRequest(userID, friendID string, status FriendRequestStatus) error {
	sn.mu.Lock()
	defer sn.mu.Unlock()
	user, userExists := sn.users.FindUser(userID)
	friend, friendExists := sn.users.FindUser(friendID)
	if !userExists || !friendExists {
		return fmt.Errorf("%w: user or friend not found", ErrUserNotFound)
	}
	request, exists := sn.friendRequests.FindFriendRequest(friendID, userID)
	if !exists || request.Status != FriendRequestPending {
		return ErrFriendRequestNotFound
	}
	request.Status = status
	sn.friendRequests.SaveFriendRequest(request)

	if status == FriendRequestAccepted {
		user.AddFriend(friendID)
		friend.AddFriend(userID)
		sn.addNotification(friendID, NotificationTypeFriendRequestAccepted,
			fmt.Sprintf("Friend request accepted by %s", userID))
	}
	return nil
}

func (sn *SocialNetwork) CreatePost(post *Post) error {
	if post == nil || strings.TrimSpace(post.ID) == "" || strings.TrimSpace(post.UserID) == "" {
		return fmt.Errorf("%w: post ID and author ID are required", ErrInvalidSocialNetwork)
	}
	sn.mu.Lock()
	defer sn.mu.Unlock()
	user, exists := sn.users.FindUser(post.UserID)
	if !exists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, post.UserID)
	}
	if _, exists := sn.posts.FindPost(post.ID); exists {
		return fmt.Errorf("%w: post ID %s already exists", ErrPostAlreadyExists, post.ID)
	}
	sn.posts.SavePost(post)
	user.AddPost(post)
	return nil
}

func (sn *SocialNetwork) GetPost(postID string) (*Post, error) {
	post, exists := sn.posts.FindPost(postID)
	if !exists {
		return nil, fmt.Errorf("%w: %s", ErrPostNotFound, postID)
	}
	return post, nil
}

func (sn *SocialNetwork) GetNewsfeed(userID string) ([]*Post, error) {
	user, exists := sn.users.FindUser(userID)
	if !exists {
		return nil, fmt.Errorf("%w: %s", ErrUserNotFound, userID)
	}
	authorIDs := append(user.GetFriends(), userID)
	feed := sn.posts.ListPostsByUsers(authorIDs)
	sort.Slice(feed, func(i, j int) bool {
		if feed[i].Timestamp.Equal(feed[j].Timestamp) {
			return feed[i].ID < feed[j].ID
		}
		return feed[i].Timestamp.After(feed[j].Timestamp)
	})
	return feed, nil
}

func (sn *SocialNetwork) LikePost(userID, postID string) error {
	sn.mu.Lock()
	defer sn.mu.Unlock()
	if _, exists := sn.users.FindUser(userID); !exists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, userID)
	}
	post, exists := sn.posts.FindPost(postID)
	if !exists {
		return fmt.Errorf("%w: %s", ErrPostNotFound, postID)
	}
	if post.AddLike(userID) && post.UserID != userID {
		sn.addNotification(post.UserID, NotificationTypeLike,
			fmt.Sprintf("Your post was liked by %s", userID))
	}
	return nil
}

func (sn *SocialNetwork) UnlikePost(userID, postID string) error {
	sn.mu.Lock()
	defer sn.mu.Unlock()
	if _, exists := sn.users.FindUser(userID); !exists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, userID)
	}
	post, exists := sn.posts.FindPost(postID)
	if !exists {
		return fmt.Errorf("%w: %s", ErrPostNotFound, postID)
	}
	post.RemoveLike(userID)
	return nil
}

func (sn *SocialNetwork) CommentOnPost(comment *Comment) error {
	if comment == nil || strings.TrimSpace(comment.ID) == "" ||
		strings.TrimSpace(comment.Content) == "" {
		return fmt.Errorf("%w: comment ID and content are required", ErrInvalidSocialNetwork)
	}
	sn.mu.Lock()
	defer sn.mu.Unlock()
	if _, exists := sn.users.FindUser(comment.UserID); !exists {
		return fmt.Errorf("%w: %s", ErrUserNotFound, comment.UserID)
	}
	post, exists := sn.posts.FindPost(comment.PostID)
	if !exists {
		return fmt.Errorf("%w: %s", ErrPostNotFound, comment.PostID)
	}
	if err := post.AddComment(comment); err != nil {
		return err
	}
	if post.UserID != comment.UserID {
		sn.addNotification(post.UserID, NotificationTypeComment,
			fmt.Sprintf("Your post received a comment from %s", comment.UserID))
	}
	return nil
}

func (sn *SocialNetwork) GetNotifications(userID string) ([]*Notification, error) {
	if _, exists := sn.users.FindUser(userID); !exists {
		return nil, fmt.Errorf("%w: %s", ErrUserNotFound, userID)
	}
	return sn.notifications.ListNotifications(userID), nil
}

func (sn *SocialNetwork) newID(prefix string) string {
	return fmt.Sprintf("%s-%06d", prefix, sn.nextID.Add(1))
}

func (sn *SocialNetwork) addNotification(userID string, notificationType NotificationType, content string) {
	sn.notifications.SaveNotification(NewNotification(
		sn.newID("NTF"), userID, notificationType, content,
	))
}
