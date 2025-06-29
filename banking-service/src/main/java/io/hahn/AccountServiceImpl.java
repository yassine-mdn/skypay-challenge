package io.hahn;

import lombok.Getter;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

@Getter
public class AccountServiceImpl implements AccountService {

    private int balance;
    private final ArrayList<Transaction> transactions;
    // will be really useful for tests
    @Setter
    private Clock clock;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public AccountServiceImpl(Clock clock) {
        this.clock = clock;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }


    @Override
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance += amount;
        transactions.add(new Transaction(LocalDateTime.now(clock), amount, balance));
    }

    @Override
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal.");
        }
        balance -= amount;
        transactions.add(new Transaction(LocalDateTime.now(clock), -amount, balance));
    }

    @Override
    public void printStatement() {
        if (transactions.isEmpty()) {
            System.out.print("No transactions to display\n");
            return;
        }
        System.out.print("date       || Amount || balance\n");
        transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimeStamp).reversed())
                .forEach(this::printTransaction);
    }

    private void printTransaction(Transaction transaction) {
        System.out.printf("%s || %d || %d\n",
                formatDate(transaction.getTimeStamp()),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction());
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }

}
