package elevator_system.strategy;

import elevator_system.Elevator;
import elevator_system.models.Request;

import java.util.List;
import java.util.Optional;

public interface ElevatorSelectionStrategy {
    Optional<Elevator> selectElevator(List<Elevator> elevators, Request request);
}
