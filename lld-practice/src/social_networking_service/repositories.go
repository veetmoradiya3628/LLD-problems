package main

import "sync"

type UserRepository interface {
	SaveUser(user *User)
	FindUser(id string) (*User, bool)
	FindUserByEmail(email string) (*User, bool)
}

type PostRepository interface {
	SavePost(post *Post)
	FindPost(id string) (*Post, bool)
	ListPostsByUsers(userIDs []string) []*Post
}

type FriendRequestRepository interface {
	SaveFriendRequest(request *FriendRequest)
	FindFriendRequest(senderID, receiverID string) (*FriendRequest, bool)
}

type NotificationRepository interface {
	SaveNotification(notification *Notification)
	ListNotifications(userID string) []*Notification
}

type InMemoryUserRepository struct {
	mu     sync.RWMutex
	users  map[string]*User
	emails map[string]string
}

func NewInMemoryUserRepository() *InMemoryUserRepository {
	return &InMemoryUserRepository{users: make(map[string]*User), emails: make(map[string]string)}
}

func (r *InMemoryUserRepository) SaveUser(user *User) {
	r.mu.Lock()
	defer r.mu.Unlock()
	for email, id := range r.emails {
		if id == user.ID && email != user.Email {
			delete(r.emails, email)
		}
	}
	r.users[user.ID] = user
	r.emails[user.Email] = user.ID
}

func (r *InMemoryUserRepository) FindUser(id string) (*User, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	user, exists := r.users[id]
	return user, exists
}

func (r *InMemoryUserRepository) FindUserByEmail(email string) (*User, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	id, exists := r.emails[email]
	if !exists {
		return nil, false
	}
	user, exists := r.users[id]
	return user, exists
}

type InMemoryPostRepository struct {
	mu    sync.RWMutex
	posts map[string]*Post
}

func NewInMemoryPostRepository() *InMemoryPostRepository {
	return &InMemoryPostRepository{posts: make(map[string]*Post)}
}

func (r *InMemoryPostRepository) SavePost(post *Post) {
	r.mu.Lock()
	defer r.mu.Unlock()
	r.posts[post.ID] = post
}

func (r *InMemoryPostRepository) FindPost(id string) (*Post, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	post, exists := r.posts[id]
	return post, exists
}

func (r *InMemoryPostRepository) ListPostsByUsers(userIDs []string) []*Post {
	r.mu.RLock()
	defer r.mu.RUnlock()
	authors := make(map[string]struct{}, len(userIDs))
	for _, id := range userIDs {
		authors[id] = struct{}{}
	}
	posts := make([]*Post, 0)
	for _, post := range r.posts {
		if _, included := authors[post.UserID]; included {
			posts = append(posts, post)
		}
	}
	return posts
}

type InMemoryFriendRequestRepository struct {
	mu       sync.RWMutex
	requests map[string]*FriendRequest
}

func NewInMemoryFriendRequestRepository() *InMemoryFriendRequestRepository {
	return &InMemoryFriendRequestRepository{requests: make(map[string]*FriendRequest)}
}

func friendRequestKey(senderID, receiverID string) string {
	return senderID + "\x00" + receiverID
}

func (r *InMemoryFriendRequestRepository) SaveFriendRequest(request *FriendRequest) {
	r.mu.Lock()
	defer r.mu.Unlock()
	r.requests[friendRequestKey(request.SenderID, request.ReceiverID)] = request
}

func (r *InMemoryFriendRequestRepository) FindFriendRequest(senderID, receiverID string) (*FriendRequest, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	request, exists := r.requests[friendRequestKey(senderID, receiverID)]
	return request, exists
}

type InMemoryNotificationRepository struct {
	mu            sync.RWMutex
	notifications map[string][]*Notification
}

func NewInMemoryNotificationRepository() *InMemoryNotificationRepository {
	return &InMemoryNotificationRepository{notifications: make(map[string][]*Notification)}
}

func (r *InMemoryNotificationRepository) SaveNotification(notification *Notification) {
	r.mu.Lock()
	defer r.mu.Unlock()
	r.notifications[notification.UserID] = append(r.notifications[notification.UserID], notification)
}

func (r *InMemoryNotificationRepository) ListNotifications(userID string) []*Notification {
	r.mu.RLock()
	defer r.mu.RUnlock()
	notifications := r.notifications[userID]
	result := make([]*Notification, len(notifications))
	copy(result, notifications)
	return result
}
