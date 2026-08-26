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

class FarthestFirstStrategyTest {

    private FarthestFirstStrategy strategy;
    private Vehicle mockVehicle;
    private ParkingFloor floor1;
    private ParkingFloor floor2;
    private ParkingFloor floor3;

    @BeforeEach
    void setUp() {
        strategy = new FarthestFirstStrategy();
        mockVehicle = mock(Vehicle.class);
        floor1 = mock(ParkingFloor.class);
        floor2 = mock(ParkingFloor.class);
        floor3 = mock(ParkingFloor.class);
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
    void findSpot_ShouldReturnSpotFromLastFloor_WhenAvailable() {
        ParkingSpot spotOnFloor1 = mock(ParkingSpot.class);
        ParkingSpot spotOnFloor3 = mock(ParkingSpot.class);

        // All floors have a spot, but we expect it to pick from floor3 because it reverses the list
        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor1));
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());
        when(floor3.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor3));

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2, floor3);
        Optional<ParkingSpot> result = strategy.findSpot(floors, mockVehicle);

        assertTrue(result.isPresent());
        assertEquals(spotOnFloor3, result.get(), "Should return the spot from the farthest (last) floor.");
    }

    @Test
    void findSpot_ShouldReturnSpotFromMiddleFloor_WhenLastFloorIsFull() {
        ParkingSpot spotOnFloor1 = mock(ParkingSpot.class);
        ParkingSpot spotOnFloor2 = mock(ParkingSpot.class);

        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor1));
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(spotOnFloor2));
        when(floor3.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty()); // Last floor is full

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2, floor3);
        Optional<ParkingSpot> result = strategy.findSpot(floors, mockVehicle);

        assertTrue(result.isPresent());
        assertEquals(spotOnFloor2, result.get(), "Should return the spot from floor 2 since floor 3 is full.");
    }
}