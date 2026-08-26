package parking_lot.strategy.fee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking_lot.entities.ParkingTicket;
import parking_lot.vehicle.Vehicle;
import parking_lot.vehicle.VehicleSize;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VehicleBasedFeeStrategyTest {
    private VehicleBasedFeeStrategy feeStrategy;
    private ParkingTicket mockTicket;
    private Vehicle mockVehicle;

    private static final long ONE_HOUR_MS = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        feeStrategy = new VehicleBasedFeeStrategy();
        mockTicket = mock(ParkingTicket.class);
        mockVehicle = mock(Vehicle.class);

        when(mockTicket.getVehicle()).thenReturn(mockVehicle);
    }

    @Test
    void calculateFee_ShouldChargeSmallRate_ForSmallVehicle() {
        long entryTime = System.currentTimeMillis();
        long exitTime = entryTime + (ONE_HOUR_MS / 2); // 30 minutes (charges for 1 hr)

        when(mockTicket.getEntryTimestamp()).thenReturn(entryTime);
        when(mockTicket.getExitTimestamp()).thenReturn(exitTime);
        when(mockVehicle.getSize()).thenReturn(VehicleSize.SMALL);

        double fee = feeStrategy.calculateFee(mockTicket);

        // 1 hour * 10.0
        assertEquals(10.0, fee);
    }

    @Test
    void calculateFee_ShouldChargeMediumRate_ForMediumVehicle() {
        long entryTime = System.currentTimeMillis();
        long exitTime = entryTime + (long)(ONE_HOUR_MS * 1.5); // 1.5 hours (charges for 2 hrs)

        when(mockTicket.getEntryTimestamp()).thenReturn(entryTime);
        when(mockTicket.getExitTimestamp()).thenReturn(exitTime);
        when(mockVehicle.getSize()).thenReturn(VehicleSize.MEDIUM);

        double fee = feeStrategy.calculateFee(mockTicket);

        // 2 hours * 20.0
        assertEquals(40.0, fee);
    }

    @Test
    void calculateFee_ShouldChargeLargeRate_ForLargeVehicle() {
        long entryTime = System.currentTimeMillis();
        long exitTime = entryTime + (ONE_HOUR_MS * 2); // exactly 2 hours (charges for 3 hrs)

        when(mockTicket.getEntryTimestamp()).thenReturn(entryTime);
        when(mockTicket.getExitTimestamp()).thenReturn(exitTime);
        when(mockVehicle.getSize()).thenReturn(VehicleSize.LARGE);

        double fee = feeStrategy.calculateFee(mockTicket);

        // 3 hours * 30.0
        assertEquals(90.0, fee);
    }
}