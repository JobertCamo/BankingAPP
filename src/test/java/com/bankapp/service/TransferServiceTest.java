package com.bankapp.service;

import com.bankapp.dao.UserDAO;
import com.bankapp.model.User;

import com.bankapp.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferServiceTest {

    @Test
    void testSendMoneySuccess() {
        AuthService authService = new AuthService();

        User sender = new User("Bob", "Lee", "bob", "123", 1000);
        User receiver = new User("Charlie", "Kim", "charlie", "123", 500);

        UserDAO userDAO = new UserDAO();
        userDAO.save(sender);
        userDAO.save(receiver);

        boolean success = authService.sendMoney(sender, receiver.getUsername(), 300);
        assertTrue(success);

        receiver = userDAO.findByUsername(receiver.getUsername());

        assertEquals(700, sender.getBalance());
        assertEquals(800, receiver.getBalance());

    }


    @Test
    void testSendMoneyInsufficientBalance() {
        AuthService authService = new AuthService();
        User sender = new User("Bob", "Lee", "bob", "123", 100);
        User receiver = new User("Charlie", "Kim", "charlie", "123", 500);

        boolean success = authService.sendMoney(sender, receiver.getUsername(), 200);

        assertFalse(success);
        assertEquals(100, sender.getBalance());
        assertEquals(500, receiver.getBalance());
    }
}
