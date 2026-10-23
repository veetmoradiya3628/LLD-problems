package main

import (
	"sync"
	"testing"
	"time"
)

func TestSearchFlightsIncludesAddedFlights(t *testing.T) {
	system := NewAirlineManagementSystem()
	departure := time.Date(2026, time.October, 24, 9, 0, 0, 0, time.UTC)
	flight := NewFlight("F001", "New York", "London", departure, departure.Add(2*time.Hour))
	system.AddFlight(flight)

	results := system.SearchFlights("new york", "LONDON", departure)
	if len(results) != 1 || results[0] != flight {
		t.Fatalf("SearchFlights() = %v, want the newly added flight", results)
	}
}

func TestBookingReservesSeatAndCancellationReleasesIt(t *testing.T) {
	manager := &BookingManager{bookings: make(map[string]*Booking)}
	flight := NewFlight("F001", "New York", "London", time.Now(), time.Now().Add(time.Hour))
	passenger := NewPassenger("U001", "John Doe", "john@example.com", "1234567890")
	seat := NewSeat("25A", SeatTypeEconomy)

	booking := manager.CreateBooking(flight, passenger, seat, 100)
	if booking == nil {
		t.Fatal("CreateBooking() returned nil for an available seat")
	}
	if seat.GetStatus() != SeatStatusReserved {
		t.Fatalf("seat status = %v, want reserved", seat.GetStatus())
	}
	if duplicate := manager.CreateBooking(flight, passenger, seat, 100); duplicate != nil {
		t.Fatal("CreateBooking() succeeded for an already-reserved seat")
	}

	manager.CancelBooking(booking.BookingNumber)
	if booking.Status != BookingStatusCancelled {
		t.Fatalf("booking status = %v, want cancelled", booking.Status)
	}
	if seat.GetStatus() != SeatStatusAvailable {
		t.Fatalf("seat status = %v after cancellation, want available", seat.GetStatus())
	}

	rebooking := manager.CreateBooking(flight, passenger, seat, 100)
	if rebooking == nil {
		t.Fatal("CreateBooking() failed after the seat was released")
	}
	manager.CancelBooking(booking.BookingNumber)
	if seat.GetStatus() != SeatStatusReserved {
		t.Fatal("cancelling an older booking released a seat reserved by a newer booking")
	}
}

func TestBookingRejectsInvalidRequests(t *testing.T) {
	manager := &BookingManager{bookings: make(map[string]*Booking)}
	flight := NewFlight("F001", "New York", "London", time.Now(), time.Now().Add(time.Hour))
	passenger := NewPassenger("U001", "John Doe", "john@example.com", "1234567890")
	seat := NewSeat("25A", SeatTypeEconomy)

	if booking := manager.CreateBooking(nil, passenger, seat, 100); booking != nil {
		t.Fatal("CreateBooking() accepted a nil flight")
	}
	if booking := manager.CreateBooking(flight, passenger, seat, -1); booking != nil {
		t.Fatal("CreateBooking() accepted a negative price")
	}
	if seat.GetStatus() != SeatStatusAvailable {
		t.Fatal("invalid booking request reserved the seat")
	}
}

func TestBookingManagerRegistersBookingsWithUniqueNumbers(t *testing.T) {
	manager := &BookingManager{bookings: make(map[string]*Booking)}
	flight := NewFlight("F001", "New York", "London", time.Now(), time.Now().Add(time.Hour))
	passenger := NewPassenger("U001", "John Doe", "john@example.com", "1234567890")

	first := manager.CreateBooking(flight, passenger, NewSeat("25A", SeatTypeEconomy), 100)
	second := manager.CreateBooking(flight, passenger, NewSeat("25B", SeatTypeEconomy), 100)
	if first == nil || second == nil {
		t.Fatal("CreateBooking() failed for an available seat")
	}
	if first.BookingNumber == "" || second.BookingNumber == "" || first.BookingNumber == second.BookingNumber {
		t.Fatalf("booking numbers are not unique and non-empty: %q, %q", first.BookingNumber, second.BookingNumber)
	}
	if manager.bookings[first.BookingNumber] != first || manager.bookings[second.BookingNumber] != second {
		t.Fatal("CreateBooking() did not register bookings by booking number")
	}
}

func TestBookingManagerCancelUnknownBookingDoesNothing(t *testing.T) {
	manager := &BookingManager{bookings: make(map[string]*Booking)}
	flight := NewFlight("F001", "New York", "London", time.Now(), time.Now().Add(time.Hour))
	passenger := NewPassenger("U001", "John Doe", "john@example.com", "1234567890")
	seat := NewSeat("25A", SeatTypeEconomy)
	booking := manager.CreateBooking(flight, passenger, seat, 100)
	if booking == nil {
		t.Fatal("CreateBooking() returned nil for an available seat")
	}

	manager.CancelBooking("unknown-booking")
	if booking.Status != BookingStatusConfirmed {
		t.Fatalf("booking status = %v after unknown cancellation, want confirmed", booking.Status)
	}
	if seat.GetStatus() != SeatStatusReserved {
		t.Fatalf("seat status = %v after unknown cancellation, want reserved", seat.GetStatus())
	}
}

func TestConcurrentBookingsForSameSeatOnlyOneSucceeds(t *testing.T) {
	manager := &BookingManager{bookings: make(map[string]*Booking)}
	flight := NewFlight("F001", "New York", "London", time.Now(), time.Now().Add(time.Hour))
	passenger := NewPassenger("U001", "John Doe", "john@example.com", "1234567890")
	seat := NewSeat("25A", SeatTypeEconomy)

	const attempts = 16
	var waitGroup sync.WaitGroup
	var successCount int
	var successMu sync.Mutex

	for i := 0; i < attempts; i++ {
		waitGroup.Add(1)
		go func() {
			defer waitGroup.Done()
			if manager.CreateBooking(flight, passenger, seat, 100) != nil {
				successMu.Lock()
				successCount++
				successMu.Unlock()
			}
		}()
	}
	waitGroup.Wait()

	if successCount != 1 {
		t.Fatalf("successful bookings = %d, want 1", successCount)
	}
}

func TestPaymentProcessor(t *testing.T) {
	processor := &PaymentProcessor{}
	processor.ProcessPayment(nil)

	payment := NewPayment("P001", "card", 100)
	processor.ProcessPayment(payment)
	if payment.status != PaymentStatusCompleted {
		t.Fatalf("payment status = %v, want completed", payment.status)
	}
}
