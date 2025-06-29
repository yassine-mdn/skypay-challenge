package io.hahn;

import java.time.*;


public class App {
    public static void main(String[] args) {
        Clock jan10 = Clock.fixed(LocalDate.of(2012, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        AccountServiceImpl account = new AccountServiceImpl(jan10);
        account.deposit(1000);

        Clock jan13 = Clock.fixed(LocalDate.of(2012, 1, 13).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        account.setClock(jan13);
        account.deposit(2000);

        Clock jan14 = Clock.fixed(LocalDate.of(2012, 1, 14).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        account.setClock(jan14);
        account.withdraw(500);


        account.printStatement();
    }

}
