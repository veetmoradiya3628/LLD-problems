package hotel_management_system.model;

import hotel_management_system.enums.RoomStyle;
import hotel_management_system.enums.RoomType;
import hotel_management_system.state.AvailableState;
import hotel_management_system.state.RoomState;

public class Room {
    private final String roomNumber;
    private final RoomType type;
    private final RoomStyle style;
    private final double price;
    private RoomState state;

    public Room(String roomNumber, RoomType type, RoomStyle style, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.style = style;
        this.price = price;
        this.state = new AvailableState(); // Initial state
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public void book() {
        state.book(this);
    }

    public void checkIn() {
        state.checkIn(this);
    }

    public void checkOut() {
        state.checkOut(this);
    }

    public void markForMaintenance() {
        state.markForMaintenance(this);
    }

    public String getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public RoomStyle getStyle() { return style; }
    public double getPrice() { return price; }
    public RoomState getState() { return state; }

    @Override
    public String toString() {
        return "Room [Number=" + roomNumber + ", Type=" + type + ", Style=" + style + ", Price=$" + price + ", State=" + state.getClass().getSimpleName() + "]";
    }
}
