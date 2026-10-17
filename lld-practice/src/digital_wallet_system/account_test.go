package main

import (
	"math/big"
	"sync"
	"testing"
)

func TestNewAccount(t *testing.T) {
	acc := NewAccount("acc-123", nil, "123456789", USD)

	if acc.ID != "acc-123" {
		t.Errorf("Expected ID 'acc-123', got %s", acc.ID)
	}

	if acc.GetBalance().Cmp(big.NewFloat(0)) != 0 {
		t.Errorf("Expected initial balance to be 0, got %v", acc.GetBalance())
	}

	if len(acc.GetTransactions()) != 0 {
		t.Errorf("Expected 0 transactions, got %d", len(acc.GetTransactions()))
	}
}

// TestAccount_Deposit verifies that depositing money increases the balance.
func TestAccount_Deposit(t *testing.T) {
	acc := NewAccount("acc-123", nil, "123456789", USD)

	acc.Deposit(big.NewFloat(100.50))

	if acc.GetBalance().Cmp(big.NewFloat(100.50)) != 0 {
		t.Errorf("Expected balance 100.50, got %v", acc.GetBalance())
	}
}

// TestAccount_Withdraw verifies withdrawal logic and error handling for insufficient funds.
func TestAccount_Withdraw(t *testing.T) {
	t.Run("Success", func(t *testing.T) {
		acc := NewAccount("acc-123", nil, "123456789", USD)
		acc.Deposit(big.NewFloat(100.0))

		err := acc.Withdraw(big.NewFloat(40.0))
		if err != nil {
			t.Errorf("Unexpected error during withdrawal: %v", err)
		}

		if acc.GetBalance().Cmp(big.NewFloat(60.0)) != 0 {
			t.Errorf("Expected balance 60.0, got %v", acc.GetBalance())
		}
	})

	t.Run("InsufficientFunds", func(t *testing.T) {
		acc := NewAccount("acc-123", nil, "123456789", USD)
		acc.Deposit(big.NewFloat(50.0))

		err := acc.Withdraw(big.NewFloat(100.0))
		if err == nil {
			t.Error("Expected an error for insufficient funds, got nil")
		}

		// Balance should remain unchanged after a failed withdrawal
		if acc.GetBalance().Cmp(big.NewFloat(50.0)) != 0 {
			t.Errorf("Expected balance to remain 50.0, got %v", acc.GetBalance())
		}
	})
}

// TestAccount_AddTransaction verifies that transactions are appended correctly.
func TestAccount_AddTransaction(t *testing.T) {
	acc := NewAccount("acc-123", nil, "123456789", USD)

	// Assuming Transaction is defined in your package
	dummyTx := &Transaction{}
	acc.AddTransaction(dummyTx)

	txs := acc.GetTransactions()
	if len(txs) != 1 {
		t.Fatalf("Expected 1 transaction, got %d", len(txs))
	}
	if txs[0] != dummyTx {
		t.Errorf("Expected the appended transaction to match the retrieved one")
	}
}

// TestAccount_Concurrency ensures the RWMutex prevents race conditions.
func TestAccount_Concurrency(t *testing.T) {
	acc := NewAccount("acc-123", nil, "123456789", USD)
	var wg sync.WaitGroup

	// Perform 100 concurrent deposits of 10.0
	workers := 100
	depositAmount := big.NewFloat(10.0)

	for i := 0; i < workers; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			acc.Deposit(depositAmount)

			// Simulate a concurrent read to trigger the RLock
			_ = acc.GetBalance()
		}()
	}

	wg.Wait()

	// 100 * 10 = 1000
	expectedBalance := big.NewFloat(1000.0)
	if acc.GetBalance().Cmp(expectedBalance) != 0 {
		t.Errorf("Expected concurrent balance to be 1000.0, got %v", acc.GetBalance())
	}
}
