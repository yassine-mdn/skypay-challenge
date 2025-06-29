package io.hahn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AccountServiceImplTest {
    private AccountServiceImpl accountServiceImpl;
    private Clock clock;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private static final String PRINT_STATEMENT_HEADER = "date       || Amount || balance\n";
    private static final String PRINT_STATEMENT_EMPTY = "No transactions to display\n";
    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        clock = mock(Clock.class);
        setClockTo("2025-01-29T00:00");
        accountServiceImpl = new AccountServiceImpl(clock);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
    /**
     * @param dateTime This method sets the clock to a specific date.
     */
    private void setClockTo(String dateTime) {
        Instant instant = LocalDateTime.parse(dateTime)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    void transaction_ShouldBeEmptyList_WhenAccountIsCreated() {
        assertEquals(0, accountServiceImpl.getTransactions().size());
    }

    @Test
    void deposit_ShouldIncreaseBalanceToSameValueAsDeposit_WhenBalanceIsEmpty() {
        accountServiceImpl.deposit(100);
        assertEquals(100, accountServiceImpl.getBalance());
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenGivenPositiveValues() {
        accountServiceImpl.deposit(100);
        accountServiceImpl.deposit(200);
        assertEquals(300, accountServiceImpl.getBalance());
    }

    @Test
    void deposit_ShouldThrowException_WhenGivenNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> accountServiceImpl.deposit(-200));
    }

    @Test
    void deposit_ShouldAddTransactionToStatement_WhenDepositIsMade() {
        accountServiceImpl.deposit(100);
        assertEquals(1, accountServiceImpl.getTransactions().size());
        assertEquals(LocalDateTime.now(clock).format(formater), accountServiceImpl.getTransactions().get(0).getTimeStamp().format(formater));
        assertEquals(100, accountServiceImpl.getTransactions().get(0).getAmount());
        assertEquals(100, accountServiceImpl.getTransactions().get(0).getBalanceAfterTransaction());
    }

    @Test
    void deposit_ShouldTransactionsHaveDifferentTimeStamps_WhenMadeInTheSameDay(){
        setClockTo("2020-01-01T00:00");
        accountServiceImpl.deposit(100);
        setClockTo("2020-01-01T01:00");
        accountServiceImpl.deposit(200);
        assertEquals(2, accountServiceImpl.getTransactions().size());
        assertNotEquals(accountServiceImpl.getTransactions().get(0).getTimeStamp().toString(), accountServiceImpl.getTransactions().get(1).getTimeStamp().toString());
    }

    @Test
    void withdraw_ShouldThrowException_WhenGivenNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> accountServiceImpl.withdraw(-200));
    }

    @Test
    void withdraw_ShouldThrowException_WhenGivenZero() {
        assertThrows(IllegalArgumentException.class, () -> accountServiceImpl.withdraw(0));
    }

    @Test
    void withdraw_ShouldThrowException_WhenGivenValueGreaterThanBalance() {
        assertThrows(IllegalArgumentException.class, () -> accountServiceImpl.withdraw(200));
    }

    @Test
    void withdraw_ShouldDecreaseBalance_WhenGivenPositiveValuesThatIsLowerThanBalance() {
        accountServiceImpl.deposit(100);
        accountServiceImpl.withdraw(50);
        assertEquals(50, accountServiceImpl.getBalance());
    }

    @Test
    void withdraw_ShouldAddTransactionToStatement_WhenWithdrawIsMade() {
        accountServiceImpl.deposit(100);
        accountServiceImpl.withdraw(50);
        assertEquals(2, accountServiceImpl.getTransactions().size());
        assertEquals(LocalDateTime.now(clock).format(formater), accountServiceImpl.getTransactions().get(1).getTimeStamp().format(formater));
        assertEquals(-50, accountServiceImpl.getTransactions().get(1).getAmount());
        assertEquals(50, accountServiceImpl.getTransactions().get(1).getBalanceAfterTransaction());
    }

    @Test
    void withdraw_ShouldTransactionsHaveDifferentTimeStamps_WhenMadeInTheSameDay(){
        setClockTo("2020-01-01T00:00");
        accountServiceImpl.deposit(100);
        accountServiceImpl.deposit(200);
        setClockTo("2021-02-01T01:00");
        accountServiceImpl.withdraw(50);
        setClockTo("2022-03-01T05:00");
        accountServiceImpl.withdraw(150);
        assertEquals(4, accountServiceImpl.getTransactions().size());
        assertNotEquals(accountServiceImpl.getTransactions().get(2).getTimeStamp().toString(), accountServiceImpl.getTransactions().get(3).getTimeStamp().toString());
    }

    @Test
    void printStatement_ShouldPrintNoTransactionMessage_WhenNoTransactions() {
        accountServiceImpl.printStatement();
        assertEquals(PRINT_STATEMENT_EMPTY, outputStreamCaptor.toString());
    }

    @Test
    void printStatement_ShouldIncludePrintHeader_WhenTransactionsExist() {
        accountServiceImpl.deposit(100);
        accountServiceImpl.printStatement();
        assertTrue(outputStreamCaptor.toString().trim().contains(PRINT_STATEMENT_HEADER));
    }

    @Test
    void PrintStatement_ShouldDisplayTransactionsInReverseChronologicalOrder_WhenTransactionsExist() {
        setClockTo("2020-01-01T00:00");
        accountServiceImpl.deposit(100);
        setClockTo("2021-01-01T01:00");
        accountServiceImpl.deposit(200);
        setClockTo("2022-01-02T00:00");
        accountServiceImpl.withdraw(50);
        setClockTo("2023-01-02T01:00");
        accountServiceImpl.withdraw(150);

        accountServiceImpl.printStatement();

        String expected = PRINT_STATEMENT_HEADER+
                "02/01/2023 || -150 || 100\n"+
                "02/01/2022 || -50 || 250\n"+
                "01/01/2021 || 200 || 300\n"+
                "01/01/2020 || 100 || 100\n";

        assertEquals(expected.trim(), outputStreamCaptor.toString().trim());
    }

}