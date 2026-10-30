package main

import (
	"errors"
	"fmt"
	"sync"
	"sync/atomic"
	"testing"
	"time"
)

func newTestNetwork(t *testing.T) *SocialNetwork {
	t.Helper()
	network, err := NewSocialNetwork(
		NewInMemoryUserRepository(),
		NewInMemoryPostRepository(),
		NewInMemoryFriendRequestRepository(),
		NewInMemoryNotificationRepository(),
	)
	if err != nil {
		t.Fatal(err)
	}
	return network
}

func registerTestUser(t *testing.T, network *SocialNetwork, id, email string) *User {
	t.Helper()
	user := NewUser(id, id, email, "password", "", "")
	if err := network.RegisterUser(user); err != nil {
		t.Fatal(err)
	}
	return user
}

func TestRegisterAndLoginNormalizesEmail(t *testing.T) {
	network := newTestNetwork(t)
	user := NewUser("u1", "Alice", " Alice@Example.com ", "secret", "", "")
	if err := network.RegisterUser(user); err != nil {
		t.Fatal(err)
	}
	if user.Email != "alice@example.com" {
		t.Fatalf("normalized email = %q", user.Email)
	}
	loggedIn, err := network.LoginUser("ALICE@example.com", "secret")
	if err != nil || loggedIn.ID != user.ID {
		t.Fatalf("login = %v, %v; want user %s", loggedIn, err, user.ID)
	}
	if _, err := network.LoginUser(user.Email, "wrong"); !errors.Is(err, ErrInvalidCredentials) {
		t.Fatalf("login error = %v, want invalid credentials", err)
	}
	if err := network.RegisterUser(NewUser("u2", "Other", "ALICE@example.com", "secret", "", "")); !errors.Is(err, ErrUserAlreadyExists) {
		t.Fatalf("duplicate email error = %v", err)
	}
}

func TestFriendRequestLifecycleAndNotifications(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")

	if err := network.SendFriendRequest(alice.ID, bob.ID); err != nil {
		t.Fatal(err)
	}
	if err := network.SendFriendRequest(alice.ID, bob.ID); !errors.Is(err, ErrFriendRequestAlreadyPending) {
		t.Fatalf("duplicate request error = %v", err)
	}
	if err := network.SendFriendRequest(bob.ID, alice.ID); !errors.Is(err, ErrFriendRequestAlreadyPending) {
		t.Fatalf("reverse request error = %v", err)
	}
	if err := network.AcceptFriendRequest(alice.ID, bob.ID); !errors.Is(err, ErrFriendRequestNotFound) {
		t.Fatalf("accepting as sender error = %v", err)
	}
	if err := network.AcceptFriendRequest(bob.ID, alice.ID); err != nil {
		t.Fatal(err)
	}
	if !alice.IsFriend(bob.ID) || !bob.IsFriend(alice.ID) {
		t.Fatal("accepted friendship should be reciprocal")
	}
	if err := network.AcceptFriendRequest(bob.ID, alice.ID); !errors.Is(err, ErrFriendRequestNotFound) {
		t.Fatalf("repeated acceptance error = %v", err)
	}

	notifications, err := network.GetNotifications(bob.ID)
	if err != nil {
		t.Fatal(err)
	}
	if len(notifications) != 1 || notifications[0].Type != NotificationTypeFriendRequest {
		t.Fatalf("bob notifications = %#v", notifications)
	}
	notifications, err = network.GetNotifications(alice.ID)
	if err != nil {
		t.Fatal(err)
	}
	if len(notifications) != 1 || notifications[0].Type != NotificationTypeFriendRequestAccepted {
		t.Fatalf("alice notifications = %#v", notifications)
	}
}

func TestDeclineFriendRequestCanBeResent(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")
	if err := network.SendFriendRequest(alice.ID, bob.ID); err != nil {
		t.Fatal(err)
	}
	if err := network.DeclineFriendRequest(bob.ID, alice.ID); err != nil {
		t.Fatal(err)
	}
	if alice.IsFriend(bob.ID) {
		t.Fatal("declining a request must not create a friendship")
	}
	if err := network.SendFriendRequest(alice.ID, bob.ID); err != nil {
		t.Fatalf("resend after decline: %v", err)
	}
}

func TestNewsfeedIncludesOnlyUserAndFriendsInReverseChronologicalOrder(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")
	outsider := registerTestUser(t, network, "outsider", "outsider@example.com")
	if err := network.SendFriendRequest(alice.ID, bob.ID); err != nil {
		t.Fatal(err)
	}
	if err := network.AcceptFriendRequest(bob.ID, alice.ID); err != nil {
		t.Fatal(err)
	}

	oldest := NewPost("p1", alice.ID, "oldest", nil, nil)
	oldest.Timestamp = time.Unix(1, 0)
	newest := NewPost("p2", bob.ID, "newest", nil, nil)
	newest.Timestamp = time.Unix(3, 0)
	middle := NewPost("p3", alice.ID, "middle", nil, nil)
	middle.Timestamp = time.Unix(2, 0)
	outsiderPost := NewPost("p4", outsider.ID, "hidden", nil, nil)
	for _, post := range []*Post{oldest, newest, middle, outsiderPost} {
		if err := network.CreatePost(post); err != nil {
			t.Fatal(err)
		}
	}

	feed, err := network.GetNewsfeed(alice.ID)
	if err != nil {
		t.Fatal(err)
	}
	if len(feed) != 3 || feed[0].ID != newest.ID || feed[1].ID != middle.ID || feed[2].ID != oldest.ID {
		t.Fatalf("feed order = %v", postIDs(feed))
	}
}

func TestLikesCommentsAndValidation(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")
	post := NewPost("post", alice.ID, "hello", nil, nil)
	if err := network.CreatePost(post); err != nil {
		t.Fatal(err)
	}
	if err := network.LikePost(bob.ID, post.ID); err != nil {
		t.Fatal(err)
	}
	if err := network.LikePost(bob.ID, post.ID); err != nil {
		t.Fatal(err)
	}
	if got := len(post.GetLikes()); got != 1 {
		t.Fatalf("likes = %d, want 1", got)
	}
	if err := network.UnlikePost(bob.ID, post.ID); err != nil {
		t.Fatal(err)
	}
	if got := len(post.GetLikes()); got != 0 {
		t.Fatalf("likes after unlike = %d, want 0", got)
	}
	comment := NewComment("comment", bob.ID, post.ID, "nice")
	if err := network.CommentOnPost(comment); err != nil {
		t.Fatal(err)
	}
	if err := network.CommentOnPost(comment); !errors.Is(err, ErrCommentAlreadyExists) {
		t.Fatalf("duplicate comment error = %v", err)
	}
	if got := len(post.GetComments()); got != 1 {
		t.Fatalf("comments = %d, want 1", got)
	}
	notifications, err := network.GetNotifications(alice.ID)
	if err != nil {
		t.Fatal(err)
	}
	if len(notifications) != 2 || notifications[0].Type != NotificationTypeLike ||
		notifications[1].Type != NotificationTypeComment {
		t.Fatalf("post notifications = %#v", notifications)
	}
	if err := network.LikePost("missing", post.ID); !errors.Is(err, ErrUserNotFound) {
		t.Fatalf("unknown liker error = %v", err)
	}
}

func TestConcurrentFriendRequestsOnlyCreateOne(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")
	const attempts = 20
	var workers sync.WaitGroup
	var successes atomic.Int32
	workers.Add(attempts)
	for i := 0; i < attempts; i++ {
		go func() {
			defer workers.Done()
			if err := network.SendFriendRequest(alice.ID, bob.ID); err == nil {
				successes.Add(1)
			}
		}()
	}
	workers.Wait()
	if got := successes.Load(); got != 1 {
		t.Fatalf("successful request count = %d", got)
	}
	notifications, err := network.GetNotifications(bob.ID)
	if err != nil {
		t.Fatal(err)
	}
	if len(notifications) != 1 {
		t.Fatalf("notification count = %d, want 1", len(notifications))
	}
}

func postIDs(posts []*Post) []string {
	ids := make([]string, len(posts))
	for i, post := range posts {
		ids[i] = post.ID
	}
	return ids
}

func TestUpdateProfilePreservesFriendsAndRejectsDuplicateEmail(t *testing.T) {
	network := newTestNetwork(t)
	alice := registerTestUser(t, network, "alice", "alice@example.com")
	bob := registerTestUser(t, network, "bob", "bob@example.com")
	if err := network.SendFriendRequest(alice.ID, bob.ID); err != nil {
		t.Fatal(err)
	}
	if err := network.AcceptFriendRequest(bob.ID, alice.ID); err != nil {
		t.Fatal(err)
	}

	if err := network.UpdateUserProfile(NewUser(alice.ID, "Alice Updated", "ALICE.UPDATED@EXAMPLE.COM", "ignored", "photo", "bio")); err != nil {
		t.Fatal(err)
	}
	if alice.Name != "Alice Updated" || alice.ProfilePicture != "photo" || alice.Bio != "bio" {
		t.Fatalf("profile was not updated: %#v", alice)
	}
	if !alice.IsFriend(bob.ID) {
		t.Fatal("profile update must preserve friendships")
	}
	if err := network.UpdateUserProfile(NewUser(alice.ID, "Alice", bob.Email, "", "", "")); !errors.Is(err, ErrUserAlreadyExists) {
		t.Fatalf("duplicate profile email error = %v", err)
	}
	if _, err := network.LoginUser("alice.updated@example.com", "password"); err != nil {
		t.Fatalf("profile update should preserve password: %v", err)
	}
	if _, err := network.LoginUser("alice@example.com", "password"); !errors.Is(err, ErrInvalidCredentials) {
		t.Fatalf("old email should no longer authenticate: %v", err)
	}
	if err := network.RegisterUser(NewUser("charlie", "Charlie", "alice@example.com", "password", "", "")); err != nil {
		t.Fatalf("released email should be reusable: %v", err)
	}
}

func TestConcurrentPostCreationRejectsDuplicateID(t *testing.T) {
	network := newTestNetwork(t)
	user := registerTestUser(t, network, "author", "author@example.com")
	const attempts = 10
	var workers sync.WaitGroup
	var successes int
	var mu sync.Mutex
	workers.Add(attempts)
	for i := 0; i < attempts; i++ {
		go func() {
			defer workers.Done()
			err := network.CreatePost(NewPost("same", user.ID, "content", nil, nil))
			if err == nil {
				mu.Lock()
				successes++
				mu.Unlock()
			}
		}()
	}
	workers.Wait()
	if successes != 1 {
		t.Fatalf("successful creates = %d", successes)
	}
}

func ExampleSocialNetwork_GetNewsfeed() {
	network, _ := NewSocialNetwork(
		NewInMemoryUserRepository(),
		NewInMemoryPostRepository(),
		NewInMemoryFriendRequestRepository(),
		NewInMemoryNotificationRepository(),
	)
	user := NewUser("u1", "Alex", "alex@example.com", "password", "", "")
	_ = network.RegisterUser(user)
	_ = network.CreatePost(NewPost("p1", user.ID, "Hello, network!", nil, nil))
	feed, _ := network.GetNewsfeed(user.ID)
	fmt.Println(feed[0].Content)
	// Output: Hello, network!
}
