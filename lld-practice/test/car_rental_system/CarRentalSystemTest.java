package car_rental_system;

import car_rental_system.entities.Car;
import car_rental_system.entities.Customer;
import car_rental_system.entities.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalSystemTest {

    private CarRentalSystem system;
    private Car sampleCar;
    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        system = CarRentalSystem.getInstance();
        system.clearSystemForTesting(); // Critical to isolate tests!

        // Initialize reusable test objects
        sampleCar = new Car("Toyota", "Camry", 2026,"MH12-1234", 30.0);
        sampleCustomer = new Customer("CUST01", "9824338238", "john1234");
    }

    @Test
    void testInitialization() {
        assertNotNull(system, "Car rental system instance should not be null");
    }

    @Test
    void testSingleInitializationOnly() {
        CarRentalSystem instance2 = CarRentalSystem.getInstance();
        assertSame(system, instance2, "Both references must point to the same instance");
    }

    @Test
    void testAddAndSearchCar_Success() {
        system.addCar(sampleCar);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);

        List<Car> foundCars = system.searchCars("Toyota", "Camry", start, end);

        assertEquals(1, foundCars.size(), "Should find exactly 1 registered car");
        assertEquals("MH12-1234", foundCars.getFirst().getLicensePlate());
    }

    @Test
    void testRemoveCar_ShouldNotBeSearchable() {
        system.addCar(sampleCar);
        system.removeCar("MH12-1234");

        List<Car> foundCars = system.searchCars("Toyota", "Camry", LocalDate.now(), LocalDate.now().plusDays(2));
        assertTrue(foundCars.isEmpty(), "Car list should be empty after removal");
    }

    @Test
    void testMakeReservation_Successful() {
        system.addCar(sampleCar);
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);

        Reservation reservation = system.makeReservation(sampleCustomer, sampleCar, start, end);

        assertNotNull(reservation, "Reservation should be successfully created");
        assertTrue(reservation.getReservationId().startsWith("RES"), "ID format should start with RES");
        assertFalse(sampleCar.isAvailable(), "Car availability flag should be flipped to false");
    }

    @Test
    void testMakeReservation_Fails_WhenCarUnavailable() {
        sampleCar.setAvailable(false); // Manually make it unavailable
        system.addCar(sampleCar);

        Reservation reservation = system.makeReservation(sampleCustomer, sampleCar, LocalDate.now(), LocalDate.now().plusDays(2));

        assertNull(reservation, "Reservation should fail and return null if the car is marked unavailable");
    }

    @Test
    void testCancelReservation_ShouldRestoreCarAvailability() {
        system.addCar(sampleCar);
        Reservation res = system.makeReservation(sampleCustomer, sampleCar, LocalDate.now(), LocalDate.now().plusDays(2));
        assertFalse(sampleCar.isAvailable());

        system.cancelReservation(res.getReservationId());

        assertTrue(sampleCar.isAvailable(), "Car should become available again upon reservation cancellation");
    }

    @Test
    void testSearchCars_Fails_WhenDatesOverlapExistingReservation() {
        system.addCar(sampleCar);

        // Existing booked window: Oct 10 to Oct 15
        LocalDate existingStart = LocalDate.of(2026, 10, 10);
        LocalDate existingEnd = LocalDate.of(2026, 10, 15);
        system.makeReservation(sampleCustomer, sampleCar, existingStart, existingEnd);

        // Re-enable car availability for searching overlap logic
        sampleCar.setAvailable(true);

        // Conflicting requested window: Oct 12 to Oct 14 (Fully inside existing)
        LocalDate requestedStart = LocalDate.of(2026, 10, 12);
        LocalDate requestedEnd = LocalDate.of(2026, 10, 14);

        List<Car> availableCars = system.searchCars("Toyota", "Camry", requestedStart, requestedEnd);

        assertTrue(availableCars.isEmpty(), "Car should not appear available during an active date overlap");
    }
}