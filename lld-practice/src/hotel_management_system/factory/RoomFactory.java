package hotel_management_system.factory;

import hotel_management_system.enums.RoomStyle;
import hotel_management_system.enums.RoomType;
import hotel_management_system.model.Room;

public class RoomFactory {
    public static Room createRoom(String roomNumber, String type, String style, double price) {
        RoomType roomType = RoomType.valueOf(type.toUpperCase());
        RoomStyle roomStyle = RoomStyle.valueOf(style.toUpperCase());
        return new Room(roomNumber, roomType, roomStyle, price);
    }
}
