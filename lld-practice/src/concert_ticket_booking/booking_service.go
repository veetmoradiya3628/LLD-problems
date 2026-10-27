package main

import (
	"context"
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

type BookingService struct {
	concerts  ConcertRepository
	bookings  BookingRepository
	payments  PaymentProcessor
	observers []BookingObserver
	mu        sync.Mutex
	nextID    atomic.Uint64
}

func NewBookingService(
	concerts ConcertRepository,
	bookings BookingRepository,
	payments PaymentProcessor,
	observers ...BookingObserver,
) (*BookingService, error) {
	if concerts == nil || bookings == nil || payments == nil {
		return nil, fmt.Errorf("%w: repositories and payment processor are required", ErrInvalidBooking)
	}
	for _, observer := range observers {
		if observer == nil {
			return nil, fmt.Errorf("%w: observer cannot be nil", ErrInvalidBooking)
		}
	}
	return &BookingService{
		concerts:  concerts,
		bookings:  bookings,
		payments:  payments,
		observers: append([]BookingObserver(nil), observers...),
	}, nil
}

func (s *BookingService) AddConcert(concert *Concert) error {
	if concert == nil || concert.ID == "" {
		return fmt.Errorf("%w: concert and concert ID are required", ErrInvalidBooking)
	}
	s.concerts.SaveConcert(concert)
	return nil
}

func (s *BookingService) GetConcert(concertID string) (*Concert, error) {
	concert, exists := s.concerts.FindConcert(concertID)
	if !exists {
		return nil, fmt.Errorf("%w: %s", ErrConcertNotFound, concertID)
	}
	return concert, nil
}

func (s *BookingService) SearchConcerts(artist, venue string, dateTime time.Time) []*Concert {
	return s.concerts.SearchConcerts(artist, venue, dateTime)
}

func (s *BookingService) BookTickets(ctx context.Context, user *User, concertID string, seats []*Seat) (*Booking, error) {
	if ctx == nil || user == nil || user.ID == "" || len(seats) == 0 {
		return nil, fmt.Errorf("%w: context, user, and at least one seat are required", ErrInvalidBooking)
	}

	s.mu.Lock()
	concert, exists := s.concerts.FindConcert(concertID)
	if !exists {
		s.mu.Unlock()
		return nil, fmt.Errorf("%w: %s", ErrConcertNotFound, concertID)
	}
	if err := validateSeats(concert, seats); err != nil {
		s.mu.Unlock()
		return nil, err
	}

	booked := make([]*Seat, 0, len(seats))
	for _, seat := range seats {
		if err := seat.Book(); err != nil {
			for _, acquired := range booked {
				acquired.Release()
			}
			s.mu.Unlock()
			return nil, err
		}
		booked = append(booked, seat)
	}

	bookingID := fmt.Sprintf("BKG-%06d", s.nextID.Add(1))
	booking := NewBooking(bookingID, user, concert, seats)
	if err := s.payments.ProcessPayment(ctx, booking); err != nil {
		for _, acquired := range booked {
			acquired.Release()
		}
		s.mu.Unlock()
		return nil, fmt.Errorf("process payment for booking %s: %w", bookingID, err)
	}
	if err := booking.ConfirmBooking(); err != nil {
		for _, acquired := range booked {
			acquired.Release()
		}
		s.mu.Unlock()
		return nil, err
	}
	s.bookings.SaveBooking(booking)
	s.mu.Unlock()

	s.notify(BookingEvent{Type: BookingConfirmedEvent, Booking: booking})
	return booking, nil
}

func (s *BookingService) CancelBooking(bookingID string) error {
	s.mu.Lock()
	booking, exists := s.bookings.FindBooking(bookingID)
	if !exists {
		s.mu.Unlock()
		return fmt.Errorf("%w: booking %s not found", ErrInvalidBooking, bookingID)
	}
	if err := booking.CancelBooking(); err != nil {
		s.mu.Unlock()
		return err
	}
	s.bookings.SaveBooking(booking)
	s.mu.Unlock()

	s.notify(BookingEvent{Type: BookingCancelledEvent, Booking: booking})
	return nil
}

func validateSeats(concert *Concert, seats []*Seat) error {
	availableSeats := make(map[string]*Seat, len(concert.Seats))
	for _, seat := range concert.Seats {
		if seat != nil {
			availableSeats[seat.ID] = seat
		}
	}

	selected := make(map[string]struct{}, len(seats))
	for _, seat := range seats {
		if seat == nil {
			return fmt.Errorf("%w: seat cannot be nil", ErrInvalidBooking)
		}
		if seat.ID == "" || availableSeats[seat.ID] != seat {
			return fmt.Errorf("%w: seat %q does not belong to concert %s", ErrInvalidBooking, seat.SeatNumber, concert.ID)
		}
		if _, exists := selected[seat.ID]; exists {
			return fmt.Errorf("%w: seat %s was selected more than once", ErrInvalidBooking, seat.SeatNumber)
		}
		selected[seat.ID] = struct{}{}
	}
	return nil
}

func (s *BookingService) notify(event BookingEvent) {
	for _, observer := range s.observers {
		observer.OnBookingEvent(event)
	}
}
