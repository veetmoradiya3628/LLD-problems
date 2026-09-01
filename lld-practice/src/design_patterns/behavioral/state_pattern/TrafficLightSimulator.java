package design_patterns.behavioral.state_pattern;

// Implement the TrafficLightState interface here.
interface TrafficLightState {
    String message();
    TrafficLightState next();
    String color();
}
// Implement the RedState class here.

class RedState implements TrafficLightState {
    @Override
    public String message() {
        return "RED light - STOP";
    }

    @Override
    public TrafficLightState next() {
        return new GreenState();
    }

    @Override
    public String color() {
        return "RED";
    }
}

// Implement the GreenState class here.

class GreenState implements TrafficLightState {
    @Override
    public String message() {
        return "GREEN light - GO";
    }

    @Override
    public TrafficLightState next() {
        return new YellowState();
    }

    @Override
    public String color() {
        return "GREEN";
    }
}

// Implement the YellowState class here.

class YellowState implements TrafficLightState {
    @Override
    public String message() {
        return "YELLOW light - Slow down";
    }

    @Override
    public TrafficLightState next() {
        return new RedState();
    }

    @Override
    public String color() {
        return "YELLOW";
    }
}

// The TrafficLight context is pre-implemented. Do not modify it.
class TrafficLight {
    private TrafficLightState state = new RedState();
    private int changes = 0;

    public TrafficLight() {
    }

    public String change() {
        String message = state.message();
        state = state.next();
        changes++;
        return message;
    }

    public String currentColor() {
        return state.color();
    }

    public String currentMessage() {
        return state.message();
    }

    public int changeCount() {
        return changes;
    }
}


public class TrafficLightSimulator {
    public static void main(String[] args) {
        TrafficLight obj = new TrafficLight();
        String param_1 = obj.change();
        String param_2 = obj.currentColor();
        String param_3 = obj.currentMessage();
        int param_4 = obj.changeCount();
    }
}
