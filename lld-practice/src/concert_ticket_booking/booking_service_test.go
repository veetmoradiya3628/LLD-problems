package main

import (
	"context"
	"errors"
	"sync"
	"sync/atomic"
	"testing"
	"time"
)

type failingPaymentProcessor struct{}

func (failingPaymentProcessor) ProcessPayment(context.Context, *Booking) error {
	return errors.New("payment declined")
}

func newTestService(t *testing.T, payment PaymentProcessor) (*BookingService, *Concert, *Seat) {
	t.Helper()

	seat := NewSeat("seat-1", "A1", SeatTypeRegular, 50)
	concert := NewConcert("concert-1", "Artist", "Venue", time.Now(), []*Seat{seat})
	service, err := NewBookingService(
		NewInMemoryConcertRepository(),
		NewInMemoryBookingRepository(),
		payment,
	)
	if err != nil {
		t.Fatal(err)
	}
	if err := service.AddConcert(concert); err != nil {
		t.Fatal(err)
	}
	return service, concert, seat
}

func TestBookTicketsPaymentFailureReleasesSeat(t *testing.T) {
	service, concert, seat := newTestService(t, failingPaymentProcessor{})

	_, err := service.BookTickets(context.Background(), NewUser("user-1", "Alex", "a@example.com"), concert.ID, []*Seat{seat})
	if err == nil {
		t.Fatal("expected payment failure")
	}
	if got := seat.GetStatus(); got != StatusAvailable {
		t.Fatalf("seat status = %v, want available", got)
	}
}

func TestBookTicketsReleasesEarlierSeatsWhenSelectionIsUnavailable(t *testing.T) {
	first := NewSeat("seat-1", "A1", SeatTypeRegular, 50)
	second := NewSeat("seat-2", "A2", SeatTypeRegular, 50)
	concert := NewConcert("concert-1", "Artist", "Venue", time.Now(), []*Seat{first, second})
	service, err := NewBookingService(
		NewInMemoryConcertRepository(),
		NewInMemoryBookingRepository(),
		MockPaymentProcessor{},
	)
	if err != nil {
		t.Fatal(err)
	}
	if err := service.AddConcert(concert); err != nil {
		t.Fatal(err)
	}
	if err := second.Book(); err != nil {
		t.Fatal(err)
	}

	_, err = service.BookTickets(
		context.Background(),
		NewUser("user-1", "Alex", "a@example.com"),
		concert.ID,
		[]*Seat{first, second},
	)
	if !errors.Is(err, ErrSeatNotAvailable) {
		t.Fatalf("booking error = %v, want ErrSeatNotAvailable", err)
	}
	if got := first.GetStatus(); got != StatusAvailable {
		t.Fatalf("first seat status = %v, want available", got)
	}
}

func TestConcurrentBookingsCannotBookSameSeat(t *testing.T) {
	service, concert, seat := newTestService(t, MockPaymentProcessor{})
	const requests = 20

	var successes atomic.Int32
	var workers sync.WaitGroup
	workers.Add(requests)
	for i := 0; i < requests; i++ {
		go func(i int) {
			defer workers.Done()
			user := NewUser(string(rune('a'+i)), "User", "user@example.com")
			if _, err := service.BookTickets(context.Background(), user, concert.ID, []*Seat{seat}); err == nil {
				successes.Add(1)
			}
		}(i)
	}
	workers.Wait()

	if got := successes.Load(); got != 1 {
		t.Fatalf("successful bookings = %d, want 1", got)
	}
}

func TestCancelBookingReleasesSeats(t *testing.T) {
	service, concert, seat := newTestService(t, MockPaymentProcessor{})
	booking, err := service.BookTickets(
		context.Background(),
		NewUser("user-1", "Alex", "a@example.com"),
		concert.ID,
		[]*Seat{seat},
	)
	if err != nil {
		t.Fatal(err)
	}
	if err := service.CancelBooking(booking.ID); err != nil {
		t.Fatal(err)
	}
	if got := booking.GetStatus(); got != BookingStatusCancelled {
		t.Fatalf("booking status = %v, want cancelled", got)
	}
	if got := seat.GetStatus(); got != StatusAvailable {
		t.Fatalf("seat status = %v, want available", got)
	}
}
