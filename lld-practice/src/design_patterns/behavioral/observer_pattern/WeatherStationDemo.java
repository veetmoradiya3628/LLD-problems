package design_patterns.behavioral.observer_pattern;

import java.util.*;

interface WeatherObserver {
    void update(WeatherStation station);
}

class WeatherStation {
    private double temperature;
    private double humidity;
    private double pressure;
    private List<WeatherObserver> observers = new ArrayList<>();

    public void registerObserver(WeatherObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyObservers() {
        for(WeatherObserver observer: observers){
            observer.update(this);
        }
    }

    public void setMeasurements(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }

    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
}

class CurrentConditionsDisplay implements WeatherObserver {
    @Override
    public void update(WeatherStation station) {
        double temp = station.getTemperature();
        double humidity = station.getHumidity();
        double pressure = station.getPressure();
        System.out.println("Current Conditions -> Temp: " + temp + ", Humidity: " + humidity + "%, Pressure: " + pressure + " hPa");
    }
}

class StatisticsDisplay implements WeatherObserver {
    private List<Double> readings = new ArrayList<>();

    @Override
    public void update(WeatherStation station) {
        readings.add(station.getTemperature());
        double sum = 0;
        for (double r : readings) sum += r;
        double avg = sum / readings.size();
        System.out.println("Statistics -> Avg Temperature: " + avg);
    }
}

public class WeatherStationDemo {
    public static void main(String[] args) {
        WeatherStation station = new WeatherStation();
        CurrentConditionsDisplay current = new CurrentConditionsDisplay();
        StatisticsDisplay stats = new StatisticsDisplay();
        station.registerObserver(current);
        station.registerObserver(stats);
        station.setMeasurements(25.0, 65.0, 1013.0);
        station.setMeasurements(28.0, 70.0, 1012.0);
        station.setMeasurements(22.0, 90.0, 1011.0);
    }
}