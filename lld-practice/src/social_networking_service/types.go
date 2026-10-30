package main

type NotificationType int
type FriendRequestStatus int

const (
	NotificationTypeFriendRequest NotificationType = iota
	NotificationTypeFriendRequestAccepted
	NotificationTypeLike
	NotificationTypeComment
	NotificationTypeMention
)

const (
	FriendRequestPending FriendRequestStatus = iota
	FriendRequestAccepted
	FriendRequestDeclined
)

func (nt NotificationType) String() string {
	values := [...]string{
		"FRIEND_REQUEST",
		"FRIEND_REQUEST_ACCEPTED",
		"LIKE",
		"COMMENT",
		"MENTION",
	}
	if nt < 0 || int(nt) >= len(values) {
		return "UNKNOWN"
	}
	return values[nt]
}
