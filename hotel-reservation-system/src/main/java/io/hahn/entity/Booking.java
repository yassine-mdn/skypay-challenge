package io.hahn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Booking {
    private User user;
    private Room room;
    private Date checkIn;
    private Date checkOut;
}
