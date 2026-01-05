package com.bankapp.service;

import com.bankapp.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void testValidLogin() {
        AuthService authService = new AuthService();
        User user = new User("John", "Doe", "john", "123", 1000);

        User result = authService.login("john", "123");
        assertNotNull(result);
        assertEquals("john", result.getUsername());

    }

    @Test
    void testInvalidLogin() {
        AuthService authService = new AuthService();

        User result = authService.login("wrong", "wrong");

        assertNull(result);
    }
}
