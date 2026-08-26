package parking_lot.strategy.fee;

import parking_lot.entities.ParkingTicket;

public interface FeeStrategy {
    double calculateFee(ParkingTicket parkingTicket);
}
