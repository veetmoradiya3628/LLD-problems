package main

import (
	"fmt"
	"log"
)

func main() {
	if err := runDemo(); err != nil {
		log.Fatal(err)
	}
}

func runDemo() error {
	socialNetwork, err := NewSocialNetwork(
		NewInMemoryUserRepository(),
		NewInMemoryPostRepository(),
		NewInMemoryFriendRequestRepository(),
		NewInMemoryNotificationRepository(),
	)
	if err != nil {
		return fmt.Errorf("create social network: %w", err)
	}

	user1 := NewUser("1", "John Doe", "john@example.com", "password", "profile1.jpg", "I love coding!")
	user2 := NewUser("2", "Jane Smith", "jane@example.com", "password", "profile2.jpg", "Exploring the world!")
	if err := socialNetwork.RegisterUser(user1); err != nil {
		return fmt.Errorf("register user1: %w", err)
	}
	if err := socialNetwork.RegisterUser(user2); err != nil {
		return fmt.Errorf("register user2: %w", err)
	}

	loggedInUser, err := socialNetwork.LoginUser(user1.Email, "password")
	if err != nil {
		return fmt.Errorf("log in user1: %w", err)
	}
	fmt.Printf("User logged in: %s\n", loggedInUser.Name)

	if err := socialNetwork.SendFriendRequest(user1.ID, user2.ID); err != nil {
		return fmt.Errorf("send friend request: %w", err)
	}
	if err := socialNetwork.AcceptFriendRequest(user2.ID, user1.ID); err != nil {
		return fmt.Errorf("accept friend request: %w", err)
	}

	post := NewPost("post1", user1.ID, "My first post!", nil, nil)
	if err := socialNetwork.CreatePost(post); err != nil {
		return fmt.Errorf("create post: %w", err)
	}
	if err := socialNetwork.CreatePost(NewPost("post2", user2.ID, "Having a great day!", nil, nil)); err != nil {
		return fmt.Errorf("create friend post: %w", err)
	}
	if err := socialNetwork.LikePost(user2.ID, post.ID); err != nil {
		return fmt.Errorf("like post: %w", err)
	}
	comment := NewComment("comment1", user2.ID, post.ID, "Great post!")
	if err := socialNetwork.CommentOnPost(comment); err != nil {
		return fmt.Errorf("comment on post: %w", err)
	}

	newsfeed, err := socialNetwork.GetNewsfeed(user1.ID)
	if err != nil {
		return fmt.Errorf("get newsfeed: %w", err)
	}
	fmt.Println("\nNewsfeed:")
	for _, post := range newsfeed {
		fmt.Printf("Post by %s: %s\n", post.UserID, post.Content)
		fmt.Printf("Likes: %d, Comments: %d\n\n", len(post.GetLikes()), len(post.GetComments()))
	}

	for _, user := range []*User{user1, user2} {
		notifications, err := socialNetwork.GetNotifications(user.ID)
		if err != nil {
			return fmt.Errorf("get notifications for %s: %w", user.ID, err)
		}
		fmt.Printf("%s's notifications:\n", user.Name)
		for _, notification := range notifications {
			fmt.Printf("Type: %s\nContent: %s\n", notification.Type, notification.Content)
		}
	}
	return nil
}
