package parking_lot.strategy.parking;


import parking_lot.entities.ParkingFloor;
import parking_lot.entities.ParkingSpot;
import parking_lot.vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {
    Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
