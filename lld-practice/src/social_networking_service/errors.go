package main

import "errors"

var (
	ErrInvalidSocialNetwork        = errors.New("invalid social network request")
	ErrUserNotFound                = errors.New("user not found")
	ErrUserAlreadyExists           = errors.New("user already exists")
	ErrInvalidCredentials          = errors.New("invalid email or password")
	ErrInvalidFriendRequest        = errors.New("invalid friend request")
	ErrAlreadyFriends              = errors.New("users are already friends")
	ErrFriendRequestAlreadyPending = errors.New("friend request is already pending")
	ErrFriendRequestNotFound       = errors.New("pending friend request not found")
	ErrPostNotFound                = errors.New("post not found")
	ErrPostAlreadyExists           = errors.New("post already exists")
	ErrCommentAlreadyExists        = errors.New("comment already exists")
)
