package main

import (
	"sync"
	"time"
)

type Post struct {
	ID        string
	UserID    string
	Content   string
	ImageURLs []string
	VideoURLs []string
	Timestamp time.Time
	likes     map[string]bool
	comments  []*Comment
	mu        sync.RWMutex
}

func NewPost(id, userID, content string, imageURLs, videoURLs []string) *Post {
	return &Post{
		ID:        id,
		UserID:    userID,
		Content:   content,
		ImageURLs: append([]string(nil), imageURLs...),
		VideoURLs: append([]string(nil), videoURLs...),
		Timestamp: time.Now(),
		likes:     make(map[string]bool),
		comments:  make([]*Comment, 0),
	}
}

func (p *Post) AddLike(userID string) bool {
	p.mu.Lock()
	defer p.mu.Unlock()
	if !p.likes[userID] {
		if p.likes == nil {
			p.likes = make(map[string]bool)
		}
		p.likes[userID] = true
		return true
	}
	return false
}

func (p *Post) AddComment(comment *Comment) error {
	if comment == nil || comment.ID == "" {
		return ErrInvalidSocialNetwork
	}
	p.mu.Lock()
	defer p.mu.Unlock()
	for _, existing := range p.comments {
		if existing.ID == comment.ID {
			return ErrCommentAlreadyExists
		}
	}
	p.comments = append(p.comments, comment)
	return nil
}

func (p *Post) RemoveLike(userID string) {
	p.mu.Lock()
	defer p.mu.Unlock()
	delete(p.likes, userID)
}

func (p *Post) GetLikes() []string {
	p.mu.RLock()
	defer p.mu.RUnlock()
	likes := make([]string, 0, len(p.likes))
	for userID := range p.likes {
		likes = append(likes, userID)
	}
	return likes
}

func (p *Post) GetComments() []*Comment {
	p.mu.RLock()
	defer p.mu.RUnlock()
	comments := make([]*Comment, len(p.comments))
	copy(comments, p.comments)
	return comments
}
