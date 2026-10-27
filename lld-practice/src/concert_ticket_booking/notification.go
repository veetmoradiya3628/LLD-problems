package main

import "fmt"

type BookingEventType string

const (
	BookingConfirmedEvent BookingEventType = "confirmed"
	BookingCancelledEvent BookingEventType = "cancelled"
)

type BookingEvent struct {
	Type    BookingEventType
	Booking *Booking
}

type BookingObserver interface {
	OnBookingEvent(event BookingEvent)
}

type ConsoleBookingObserver struct{}

func (ConsoleBookingObserver) OnBookingEvent(event BookingEvent) {
	fmt.Printf("Booking %s %s\n", event.Booking.ID, event.Type)
}
