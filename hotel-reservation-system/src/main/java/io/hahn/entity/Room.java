package io.hahn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {
    private int roomNumber;
    private RoomType roomType;
    private int pricePerNight;
}
