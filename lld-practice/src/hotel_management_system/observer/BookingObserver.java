package hotel_management_system.observer;

import hotel_management_system.model.Booking;

public interface BookingObserver {
    void update(Booking booking);
}
