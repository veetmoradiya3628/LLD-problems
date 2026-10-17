package main

import "testing"

func TestNewUser(t *testing.T) {
	user := NewUser("user_1", "User 1", "user1@gmail.com", "12345678")

	if user.ID != "user_1" {
		t.Errorf("Expected user ID 'user_1', got %s", user.ID)
	}

	if len(user.accounts) != 0 {
		t.Errorf("Expected 0 accounts, got %d", len(user.accounts))
	}
}

func TestUser_AccountAddition(t *testing.T) {
	user := NewUser("user_1", "User 1", "user1@gmail.com", "12345678")
	account := NewAccount("acc-123", user, "123456789", USD)

	if len(user.accounts) != 0 {
		t.Errorf("Expected 0 accounts, got %d", len(user.accounts))
	}

	user.AddAccount(account)

	if !(len(user.accounts) == 1) {
		t.Errorf("Expected 1 accounts, got %d", len(user.accounts))
	}
}

func TestUser_AccountRemoval(t *testing.T) {
	user := NewUser("user_1", "User 1", "user1@gmail.com", "12345678")
	account := NewAccount("acc-123", user, "123456789", USD)

	if len(user.accounts) != 0 {
		t.Errorf("Expected 0 accounts, got %d", len(user.accounts))
	}

	user.AddAccount(account)

	if !(len(user.accounts) == 1) {
		t.Errorf("Expected 1 accounts, got %d", len(user.accounts))
	}

	user.RemoveAccount(account)

	if len(user.accounts) != 0 {
		t.Errorf("Expected 0 accounts, got %d", len(user.accounts))
	}
}
