package io.hahn;

import io.hahn.entity.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class App {
    public static void main(String[] args) {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR, 2000);
        service.setRoom(3, RoomType.SUITE, 3000);

        service.setUser(1, 5000);
        service.setUser(2, 10000);

        try {
            service.bookRoom(1, 2, sdf.parse("30/06/2026"), sdf.parse("07/07/2026"));
        } catch (Exception e) {
            System.out.println("Booking 1 failed: " + e.getMessage());
        }

        try {
            service.bookRoom(1, 2, sdf.parse("07/07/2026"), sdf.parse("30/06/2026"));
        } catch (Exception e) {
            System.out.println("Booking 2 failed: " + e.getMessage());
        }

        try {
            service.bookRoom(1, 1, sdf.parse("07/07/2026"), sdf.parse("08/07/2026"));
        } catch (Exception e) {
            System.out.println("Booking 3 failed: " + e.getMessage());
        }

        try {
            service.bookRoom(2, 1, sdf.parse("07/07/2026"), sdf.parse("09/07/2026"));
        } catch (Exception e) {
            System.out.println("Booking 4 failed: " + e.getMessage());
        }

        try {
            service.bookRoom(2, 3, sdf.parse("07/07/2026"), sdf.parse("08/07/2026"));
        } catch (Exception e) {
            System.out.println("Booking 5 failed: " + e.getMessage());
        }

        try {
            service.setRoom(1, RoomType.SUITE, 10000);
        } catch (Exception e) {
            System.out.println("Set room failed: " + e.getMessage());
        }

        System.out.println("\n---------------------------------- PRINT ALL --------------------------------------\n");
        service.printAll();

        System.out.println("\n-------------------------------------- PRINT USERS --------------------------------------\n");
        service.printAllUsers();
    }
}
