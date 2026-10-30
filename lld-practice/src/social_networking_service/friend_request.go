package main

import "time"

type FriendRequest struct {
	ID         string
	SenderID   string
	ReceiverID string
	Status     FriendRequestStatus
	CreatedAt  time.Time
}

func NewFriendRequest(id, senderID, receiverID string) *FriendRequest {
	return &FriendRequest{
		ID:         id,
		SenderID:   senderID,
		ReceiverID: receiverID,
		Status:     FriendRequestPending,
		CreatedAt:  time.Now(),
	}
}
