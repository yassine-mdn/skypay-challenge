package io.hahn;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Transaction {
    LocalDateTime timeStamp;
    int amount;
    int balanceAfterTransaction;
}
