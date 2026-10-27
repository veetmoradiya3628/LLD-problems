package main

import (
	"context"
	"fmt"
	"log"
	"time"
)

func main() {
	concerts := NewInMemoryConcertRepository()
	bookings := NewInMemoryBookingRepository()
	service, err := NewBookingService(
		concerts,
		bookings,
		MockPaymentProcessor{},
		ConsoleBookingObserver{},
	)
	if err != nil {
		log.Fatal(err)
	}

	seats := GenerateSeats(35)
	concert := NewConcert(
		"concert-2026",
		"The Example Band",
		"City Arena",
		time.Date(2026, time.December, 15, 20, 0, 0, 0, time.Local),
		seats,
	)
	if err := service.AddConcert(concert); err != nil {
		log.Fatal(err)
	}

	matches := service.SearchConcerts("The Example Band", "City Arena", time.Time{})
	fmt.Printf("Found %d matching concert(s)\n", len(matches))

	user := NewUser("user-1", "Alex", "alex@example.com")
	booking, err := service.BookTickets(
		context.Background(),
		user,
		concert.ID,
		[]*Seat{seats[0], seats[1]},
	)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("Booking %s confirmed for %s: %d seat(s), total $%.2f\n",
		booking.ID, user.Name, len(booking.Seats), booking.TotalPrice)

	if err := service.CancelBooking(booking.ID); err != nil {
		log.Fatal(err)
	}
	fmt.Printf("Booking status: %v; seat %s status: %v\n",
		booking.GetStatus(), seats[0].SeatNumber, seats[0].GetStatus())
}
