package com.bankapp.service;

import com.bankapp.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    @Test
    void testTransactionHistory() {
        AuthService authService = new AuthService();
        User user = new User("John", "Doe", "john", "123", 1000);
        User receiver = new User("Jane", "Doe", "jane", "123", 500);

        authService.deposit(user, 200);
        authService.withdraw(user, 100);
        authService.sendMoney(user, receiver.getUsername(), 150);

        assertDoesNotThrow(() -> authService.printTransactionHistory(user));
    }
}
