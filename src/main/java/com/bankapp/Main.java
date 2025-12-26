package com.bankapp;

import com.bankapp.model.User;
import com.bankapp.service.AuthService;

import java.io.Console;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AuthService authService = new AuthService();
        Scanner sc = new Scanner(System.in);
        Console console = System.console();

        System.out.println("===========================");
        System.out.println("=== Welcome to Bank App ===");
        System.out.println("===========================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        User currentUser = null;

        // Registration
        if (choice == 1) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            String password;
            if (console != null) {
                System.out.print("Enter password: ");
                password = new String(console.readPassword());
            } else {
                System.out.print("Enter password: ");
                password = sc.nextLine();
            }

            if (authService.register(username, password)) {
                System.out.println("===========================================");
                System.out.println("Registration successful! You can now login.");
                System.out.println("===========================================");
            } else {
                System.out.println("Registration failed. Username might already exist.");
            }
        }

        // Login
        System.out.print("Enter username to login: ");
        String username = sc.nextLine();

        String password;
        if (console != null) {
            System.out.print("Enter password: ");
            password = new String(console.readPassword());
        } else {
            System.out.print("Enter password: ");
            password = sc.nextLine();
        }

        currentUser = authService.login(username, password);
        if (currentUser == null) {
            System.out.println("Login failed. Exiting.");
            sc.close();
            return;
        }

        // Dashboard loop
        boolean running = true;
        while (running) {
            System.out.println("\n=================");
            System.out.println("=== Dashboard ===");
            System.out.println("=================");
            System.out.printf("Hello, %s! Your balance: %.2f PHP%n", currentUser.getUsername(), currentUser.getBalance());
            System.out.println("1. Balance");
            System.out.println("2. Send Money");
            System.out.println("3. Transaction History");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.printf("Your balance: %.2f PHP%n", currentUser.getBalance());
                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
                }
                case 2 -> {
                    System.out.print("Enter recipient username: ");
                    String receiver = sc.nextLine();
                    System.out.print("Enter amount to send: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    // Confirm transaction
                    System.out.print("Are you sure you want to send " + amount + " PHP to " + receiver + "? (Y/N): ");
                    String confirm = sc.nextLine();
                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("Transaction canceled.");
                        break;
                    }

                    boolean success = authService.sendMoney(currentUser, receiver, amount);
                    if (success) {
                        currentUser = authService.login(currentUser.getUsername(), currentUser.getPassword());
                    }

                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
                }
                case 3 -> {
                    authService.printTransactionHistory(currentUser);
                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
                }
                case 4 -> {
                    System.out.println("Logged out. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option! Please try again.");
            }
        }

        sc.close();
    }
}
