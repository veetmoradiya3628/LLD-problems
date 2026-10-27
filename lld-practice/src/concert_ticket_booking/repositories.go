package main

import (
	"sync"
	"time"
)

type ConcertRepository interface {
	SaveConcert(concert *Concert)
	FindConcert(id string) (*Concert, bool)
	SearchConcerts(artist, venue string, dateTime time.Time) []*Concert
}

type BookingRepository interface {
	SaveBooking(booking *Booking)
	FindBooking(id string) (*Booking, bool)
}

type InMemoryConcertRepository struct {
	mu       sync.RWMutex
	concerts map[string]*Concert
}

func NewInMemoryConcertRepository() *InMemoryConcertRepository {
	return &InMemoryConcertRepository{concerts: make(map[string]*Concert)}
}

func (r *InMemoryConcertRepository) SaveConcert(concert *Concert) {
	r.mu.Lock()
	defer r.mu.Unlock()
	r.concerts[concert.ID] = concert
}

func (r *InMemoryConcertRepository) FindConcert(id string) (*Concert, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	concert, exists := r.concerts[id]
	return concert, exists
}

func (r *InMemoryConcertRepository) SearchConcerts(artist, venue string, dateTime time.Time) []*Concert {
	r.mu.RLock()
	defer r.mu.RUnlock()

	results := make([]*Concert, 0)
	for _, concert := range r.concerts {
		if artist != "" && concert.Artist != artist {
			continue
		}
		if venue != "" && concert.Venue != venue {
			continue
		}
		if !dateTime.IsZero() && !concert.DateTime.Equal(dateTime) {
			continue
		}
		results = append(results, concert)
	}
	return results
}

type InMemoryBookingRepository struct {
	mu       sync.RWMutex
	bookings map[string]*Booking
}

func NewInMemoryBookingRepository() *InMemoryBookingRepository {
	return &InMemoryBookingRepository{bookings: make(map[string]*Booking)}
}

func (r *InMemoryBookingRepository) SaveBooking(booking *Booking) {
	r.mu.Lock()
	defer r.mu.Unlock()
	r.bookings[booking.ID] = booking
}

func (r *InMemoryBookingRepository) FindBooking(id string) (*Booking, bool) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	booking, exists := r.bookings[id]
	return booking, exists
}
