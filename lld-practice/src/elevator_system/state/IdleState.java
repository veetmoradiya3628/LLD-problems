package elevator_system.state;

import elevator_system.Elevator;
import elevator_system.enums.Direction;
import elevator_system.models.Request;

public class IdleState implements ElevatorState {
    @Override
    public void move(Elevator elevator) {
        if (!elevator.getUpRequests().isEmpty()) {
            elevator.setState(new MovingUpState());
        } else if (!elevator.getDownRequests().isEmpty()) {
            elevator.setState(new MovingDownState());
        }
        // else stay idle
    }

    @Override
    public void addRequest(Elevator elevator, Request request) {
        if (request.getTargetFloor() > elevator.getCurrentFloor()) {
            elevator.getUpRequests().add(request.getTargetFloor());
        } else if (request.getTargetFloor() < elevator.getCurrentFloor()) {
            elevator.getDownRequests().add(request.getTargetFloor());
        }
        // if request is for current floor, doors would open
    }

    @Override
    public Direction getDirection() {
        return Direction.IDLE;
    }
}
