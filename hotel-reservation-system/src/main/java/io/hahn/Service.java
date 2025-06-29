package io.hahn;

import io.hahn.entity.Booking;
import io.hahn.entity.Room;
import io.hahn.entity.RoomType;
import io.hahn.entity.User;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Service {
    ArrayList<Room> rooms;
    ArrayList<User> users;
    ArrayList<Booking> bookings;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Service() {
        rooms = new ArrayList<>();
        users = new ArrayList<>();
        bookings = new ArrayList<>();
    }


    void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        if (isRoomExist(roomNumber)) {
            throw new IllegalArgumentException("room number already exists");
        }
        if (roomPricePerNight < 0) {
            throw new IllegalArgumentException("room price per night cannot be negative");
        }
        Room room = new Room(roomNumber, roomType, roomPricePerNight);
        rooms.add(room);
    }

    void setUser(int userId, int balance) {
        if (isUserExist(userId)) {
            throw new IllegalArgumentException("user already exists");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("balance cannot be negative");
        }
        User user = new User(userId, balance);
        users.add(user);
    }

    void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        User user = findByUserId(userId);
        Room room = findByRoomNumber(roomNumber);
        if (checkOut.before(checkIn)) {
            throw new IllegalArgumentException("check out date cannot be before check in");
        }
        if (isBooked(roomNumber,checkIn,checkOut)) {
            throw new IllegalArgumentException("booking already exists");
        }

        bookings.add(new Booking(user,room,checkIn,checkOut));

    }
    void printAll() {
        System.out.println("\n--- ALL USERS ---\n");
        printAllUsers();
        System.out.println("\n--- ALL ROOMS ---\n");
        printAllRooms();
        if (bookings.isEmpty()) {
            System.out.println("No bookings to display\n");
        }
        System.out.println("\n--- ALL BOOKINGS ---\n");
        System.out.println("roomNumber || userID || CheckIN || CheckOUT\n");
        for (Booking booking : bookings) {
            StringBuilder sb  = new StringBuilder()
                    .append(booking.getRoom().getRoomNumber())
                    .append("           || ")
                    .append(booking.getUser().getUserId())
                    .append(" || ")
                    .append(formatDate(booking.getCheckIn()))
                    .append(" || ")
                    .append(formatDate(booking.getCheckOut()))
                    .append("\n");
            System.out.println(sb.toString());
        }
    }

    void printAllUsers() {
        if (users.isEmpty()) {
            System.out.print("No Users to display\n");
            return;
        }
        System.out.print("ID || balance\n");
        for (User user : users) {
            System.out.print(user.getUserId()+"  ||"+user.getBalance()+"\n");
        }
    }

    private void printAllRooms() {
        if (rooms.isEmpty()) {
            System.out.print("No Rooms to display\n");
        }
        System.out.println("room number || type        || price per night\n");
        for (Room room : rooms) {
            StringBuilder sb = new StringBuilder().append(room.getRoomNumber())
                    .append("           || ").append(room.getRoomType())
                    .append("      || ").append(room.getPricePerNight())
                    .append("\n");
            System.out.println(sb.toString());
        }
    }

    private boolean isRoomExist(int roomNumber) {
        return rooms.stream().anyMatch(room -> room.getRoomNumber() == roomNumber);
    }

    private boolean isUserExist(int userId) {
        return users.stream().anyMatch(user -> user.getUserId() == userId);
    }

    private boolean isBooked(int roomNumber,  Date checkIn, Date checkOut) {
        return bookings.stream()
                .filter(b -> b.getRoom().getRoomNumber() == roomNumber)
                .anyMatch(b -> checkIn.before(b.getCheckOut()) && checkOut.after(b.getCheckIn()));
    }

    private Room findByRoomNumber(int roomNumber) {
        return rooms.stream().filter(room -> room.getRoomNumber() == roomNumber).findFirst().orElseThrow(
                () -> new IllegalArgumentException("room with number not found")
        );
    }

    private User findByUserId(int userId) {
        return users.stream().filter(user -> user.getUserId() == userId).findFirst().orElseThrow(
                () -> new IllegalArgumentException("user with id not found")
        );
    }

    private static String formatDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(DATE_FORMATTER);
    }
}
