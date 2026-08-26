package parking_lot.strategy.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking_lot.entities.ParkingFloor;
import parking_lot.entities.ParkingSpot;
import parking_lot.vehicle.Vehicle;
import parking_lot.vehicle.VehicleSize;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BestFitStrategyTest {
    private BestFitStrategy bestFitStrategy;
    private Vehicle mockVehicle;
    private ParkingFloor floor1;
    private ParkingFloor floor2;

    @BeforeEach
    void setUp() {
        bestFitStrategy = new BestFitStrategy();
        mockVehicle = mock(Vehicle.class);
        floor1 = mock(ParkingFloor.class);
        floor2 = mock(ParkingFloor.class);
    }

    @Test
    void findSpot_ShouldReturnEmpty_WhenNoFloorsAvailable() {
        Optional<ParkingSpot> result = bestFitStrategy.findSpot(Collections.emptyList(), mockVehicle);
        assertTrue(result.isEmpty());
    }

    @Test
    void findSpot_ShouldReturnEmpty_WhenNoSpotsAvailableOnAnyFloor() {
        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = bestFitStrategy.findSpot(floors, mockVehicle);

        assertTrue(result.isEmpty());
    }

    @Test
    void findSpot_ShouldReturnSpot_WhenOnlyOneSpotAvailable() {
        ParkingSpot mockSpot = mock(ParkingSpot.class);
        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(mockSpot));
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.empty());

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = bestFitStrategy.findSpot(floors, mockVehicle);

        assertTrue(result.isPresent());
        assertEquals(mockSpot, result.get());
    }

    @Test
    void findSpot_ShouldReturnSmallestSpot_WhenMultipleSpotsAvailable() {
        ParkingSpot largeSpot = mock(ParkingSpot.class);
        ParkingSpot mediumSpot = mock(ParkingSpot.class);

        // Assuming VehicleSize/SpotSize has ordinals (e.g., SMALL=0, MEDIUM=1, LARGE=2)
        when(largeSpot.getSpotSize()).thenReturn(VehicleSize.LARGE);
        when(mediumSpot.getSpotSize()).thenReturn(VehicleSize.MEDIUM);

        when(floor1.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(largeSpot));
        when(floor2.findAvailableSpot(mockVehicle)).thenReturn(Optional.of(mediumSpot));

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);
        Optional<ParkingSpot> result = bestFitStrategy.findSpot(floors, mockVehicle);

        // It should pick mediumSpot because its ordinal is smaller than largeSpot
        assertTrue(result.isPresent());
        assertEquals(mediumSpot, result.get());
    }
}