package parking_lot.vehicle;

public class Bike extends Vehicle {
    public Bike(String licenseNumber) {
        super(licenseNumber, VehicleSize.SMALL);
    }
}
