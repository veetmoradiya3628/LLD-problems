package main

import "errors"

var (
	ErrSeatNotAvailable    = errors.New("seat is not available")
	ErrInvalidBooking      = errors.New("invalid booking request")
	ErrInvalidBookingState = errors.New("invalid booking state")
	ErrConcertNotFound     = errors.New("concert not found")
)

type SeatNotAvailableError struct {
	message string
}

func NewSeatNotAvailableError(message string) *SeatNotAvailableError {
	return &SeatNotAvailableError{message: message}
}

func (e *SeatNotAvailableError) Error() string {
	return e.message
}

func (e *SeatNotAvailableError) Unwrap() error {
	return ErrSeatNotAvailable
}
