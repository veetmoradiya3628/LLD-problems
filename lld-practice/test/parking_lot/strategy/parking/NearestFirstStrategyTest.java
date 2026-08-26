package parking_lot.strategy.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking_lot.entities.ParkingFloor;
import parking_lot.entities.ParkingSpot;
import parking_lot.vehicle.Vehicle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NearestFirstStrategyTest {

    private NearestFirstStrategy strategy;
    private Vehicle mockVehicle;
    private ParkingFloor floor1;
    private ParkingFloor floor2;

    @BeforeEach
    void setUp() {
        strategy = new NearestFirstStrategy();
        mockVehicle = mock(Vehicle.class);
        floor1 = mock(ParkingFloor.class);
        floor2 = mock(ParkingFloor.class);
    }

    @Test
    void findSpot_ShouldReturnEmpty_WhenNoFloorsAvailable() {
        Optional<ParkingSpot> result = strategy.findSpot(Collections.emptyList(), mockVehicle);
        assertTrue(result.isEmpty());
    }

    @Test
    void findSpot_ShouldReturnEmpty_WhenNoSpotsAvailableOnAnyFloor() {
        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = strategy.findSpot(floors, mockVehicle);

        assertTrue(result.isEmpty());
    }

    @Test
    void findSpot_ShouldReturnSpotFromFirstFloor_WhenMultipleAvailable() {
        ParkingSpot spotOnFloor1 = mock(ParkingSpot.class);
        ParkingSpot spotOnFloor2 = mock(ParkingSpot.class);

        // Both floors have spots available, but we expect it to pick floor1's spot first
        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor1));
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor2));

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = strategy.findSpot(floors, mockVehicle);

        assertTrue(result.isPresent());
        assertEquals(spotOnFloor1, result.get(), "Should return the spot from the nearest (first) floor.");
    }

    @Test
    void findSpot_ShouldReturnSpotFromSecondFloor_WhenFirstFloorIsFull() {
        ParkingSpot spotOnFloor2 = mock(ParkingSpot.class);

        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty()); // First floor full
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor2));

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = strategy.findSpot(floors, mockVehicle);

        assertTrue(result.isPresent());
        assertEquals(spotOnFloor2, result.get(), "Should return the spot from floor 2 since floor 1 is full.");
    }
}