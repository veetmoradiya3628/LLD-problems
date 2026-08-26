package parking_lot.strategy.fee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking_lot.entities.ParkingTicket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlatRateFeeStrategyTest {
    private FlatRateFeeStrategy feeStrategy;
    private ParkingTicket mockTicket;

    // 1 hour in milliseconds
    private static final long ONE_HOUR_MS = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        feeStrategy = new FlatRateFeeStrategy();
        mockTicket = mock(ParkingTicket.class);
    }

    @Test
    void calculateFee_ShouldChargeOneHour_ForLessThanOneHour() {
        long entryTime = System.currentTimeMillis();
        long exitTime = entryTime + (ONE_HOUR_MS / 2); // 30 minutes

        when(mockTicket.getEntryTimestamp()).thenReturn(entryTime);
        when(mockTicket.getExitTimestamp()).thenReturn(exitTime);

        double fee = feeStrategy.calculateFee(mockTicket);

        // (0 hours + 1) * 10.0 = 10.0
        assertEquals(10.0, fee);
    }

    @Test
    void calculateFee_ShouldChargeTwoHours_ForExactlyOneHour() {
        long entryTime = System.currentTimeMillis();
        long exitTime = entryTime + ONE_HOUR_MS; // Exactly 60 minutes

        when(mockTicket.getEntryTimestamp()).thenReturn(entryTime);
        when(mockTicket.getExitTimestamp()).thenReturn(exitTime);

        double fee = feeStrategy.calculateFee(mockTicket);

        // Based on your formula: (1 + 1) * 10.0 = 20.0
        assertEquals(20.0, fee);
    }
}