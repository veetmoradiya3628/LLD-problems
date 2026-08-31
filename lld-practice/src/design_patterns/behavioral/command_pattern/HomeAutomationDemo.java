package design_patterns.behavioral.command_pattern;

import java.util.Stack;

interface Command {
    void execute();
    void undo();
}

class Light {
    public void on() {
        System.out.println("Light turned on");
    }

    public void off() {
        System.out.println("Light turned off");
    }
}

class Thermostat {
    private int currentTemperature = 20;

    public void setTemperature(int temp) {
        System.out.println("Thermostat set to " + temp + "C");
        currentTemperature = temp;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }
}

class LightOnCommand implements Command {
    private final Light light;
    public LightOnCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

class LightOffCommand implements Command {
    private final Light light;

    public LightOffCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}

class SetTemperatureCommand implements Command {
    private final Thermostat thermostat;
    private final int newTemperature;
    private int previousTemperature;

    public SetTemperatureCommand(Thermostat thermostat, int temperature) {
        this.thermostat = thermostat;
        this.newTemperature = temperature;
    }

    @Override
    public void execute() {
        previousTemperature = thermostat.getCurrentTemperature();
        thermostat.setTemperature(newTemperature);
    }

    @Override
    public void undo() {
        thermostat.setTemperature(previousTemperature);
    }
}

class RemoteControl {
    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command command){
        command.execute();
        history.push(command);
    }

    public void undoLast(){
        if (!history.empty()){
            Command lastCommand = history.pop();
            lastCommand.undo();
        } else {
            System.out.println("Nothing to undo");
        }
    }
}

public class HomeAutomationDemo {
    public static void main(String[] args) {
        Light light = new Light();
        Thermostat thermostat = new Thermostat();

        Command lightOn = new LightOnCommand(light);
        Command lightOff = new LightOffCommand(light);
        Command setTemp = new SetTemperatureCommand(thermostat, 25);

        RemoteControl rc = new RemoteControl();

        System.out.println("----------- Executing commands ----------------");
        rc.executeCommand(lightOn);
        rc.executeCommand(setTemp);
        rc.executeCommand(lightOff);

        System.out.println("Undoing commands");
        rc.undoLast();
        rc.undoLast();
        rc.undoLast();
        rc.undoLast();
    }
}
