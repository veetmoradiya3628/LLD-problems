package main

import (
	"fmt"
	"sync"
)

type Booking struct {
	ID         string
	User       *User
	Concert    *Concert
	Seats      []*Seat
	TotalPrice float64
	Status     BookingStatus
	mu         sync.RWMutex
}

func NewBooking(id string, user *User, concert *Concert, seats []*Seat) *Booking {
	seatCopy := append([]*Seat(nil), seats...)
	totalPrice := calculateTotalPrice(seatCopy)
	return &Booking{
		ID:         id,
		User:       user,
		Concert:    concert,
		Seats:      seatCopy,
		TotalPrice: totalPrice,
		Status:     BookingStatusPending,
	}
}

func (b *Booking) ConfirmBooking() error {
	b.mu.Lock()
	defer b.mu.Unlock()

	if b.Status == BookingStatusPending {
		b.Status = BookingStatusConfirmed
		return nil
	}
	return fmt.Errorf("%w: cannot confirm booking in status %v", ErrInvalidBookingState, b.Status)
}

func (b *Booking) CancelBooking() error {
	b.mu.Lock()
	defer b.mu.Unlock()

	if b.Status == BookingStatusConfirmed {
		b.Status = BookingStatusCancelled
		for _, seat := range b.Seats {
			seat.Release()
		}
		return nil
	}
	return fmt.Errorf("%w: cannot cancel booking in status %v", ErrInvalidBookingState, b.Status)
}

func (b *Booking) GetStatus() BookingStatus {
	b.mu.RLock()
	defer b.mu.RUnlock()

	return b.Status
}

func calculateTotalPrice(seats []*Seat) float64 {
	var total float64
	for _, seat := range seats {
		total += seat.Price
	}
	return total
}
