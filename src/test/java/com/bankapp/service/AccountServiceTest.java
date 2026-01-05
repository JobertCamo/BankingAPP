package com.bankapp.service;

import com.bankapp.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    @Test
    void testDeposit() {
        AuthService authService = new AuthService();
        User user = new User("Alice", "Smith", "alice", "123", 500);

        boolean success = authService.deposit(user, 200);
        assertTrue(success);
        assertEquals(700, user.getBalance());
    }

    @Test
    void testWithdraw() {
        AuthService authService = new AuthService();
        User user = new User("Alucard", "Smith", "alucard", "123", 500);

        boolean success = authService.withdraw(user, 300);
        assertTrue(success);
        assertEquals(200, user.getBalance());
    }

    @Test
    void testWithdrawInvalid() {
        AuthService authService = new AuthService();
        User user = new User("Aamon", "Smith", "aamon", "123", 500);

        boolean success = authService.withdraw(user, 600);
        assertFalse(success);
        assertEquals(500, user.getBalance());
    }
}
