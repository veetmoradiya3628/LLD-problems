package elevator_system.state;

import elevator_system.Elevator;
import elevator_system.enums.Direction;
import elevator_system.models.Request;

public interface ElevatorState {
    void move(Elevator elevator);

    void addRequest(Elevator elevator, Request request);

    Direction getDirection();
}
