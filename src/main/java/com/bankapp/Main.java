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

        System.out.println("=======================================");
        System.out.println("|         Welcome to Bank App         |");
        System.out.println("=======================================");
        System.out.println("|             1. Register             |");
        System.out.println("|             2. Login                |");
        System.out.println("=======================================");
        System.out.print("\nChoose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        User currentUser;

        // ===== Registration =====
        if (choice == 1) {
            System.out.println("\n=======================================");
            System.out.println("|             Registration            |");
            System.out.println("=======================================");
            System.out.print("\nEnter first name: ");
            String firstName = sc.nextLine();

            System.out.print("Enter last name: ");
            String lastName = sc.nextLine();

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

            boolean registered = authService.register(firstName, lastName, username, password);
            if (!registered) {
                System.out.println("Registration failed.");
                return;
            }
            System.out.println("\n=======================================");
            System.out.println("|Registration successful Please login |");
            System.out.println("=======================================");
        }

        System.out.println("\n=======================================");
        System.out.println("|                Login                |");
        System.out.println("=======================================");
        System.out.print("\nUsername: ");
        String username = sc.nextLine();

        String password;
        if (console != null) {
            System.out.print("Password: ");
            password = new String(console.readPassword());
        } else {
            System.out.print("Password: ");
            password = sc.nextLine();
        }

        currentUser = authService.login(username, password);
        if (currentUser == null) {
            System.out.println("Invalid login.");
            return;
        }

        // ===== Dashboard =====
        boolean running = true;
        while (running) {
            System.out.println("=======================================");
            System.out.println("|              Dashboard              |");
            System.out.println("=======================================");
            System.out.printf("          Welcome %s %s           " +
                            "\n  Your current balance: %.2f PHP%n",
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getBalance());
            System.out.println("=======================================");
            System.out.println("             1. Balance");
            System.out.println("             2. Deposit");
            System.out.println("             3. Withdraw");
            System.out.println("             4. Send Money");
            System.out.println("             5. Transaction History");
            System.out.println("             6. Logout");
            System.out.println("=======================================");
            System.out.print("\nChoose option: ");

            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> {
                        System.out.println("=======================================");
                        System.out.printf("         Balance: %.2f PHP%n", currentUser.getBalance());
                        System.out.println("=======================================");
                }

                case 2 -> {
                    System.out.print("Deposit amount: ");
                    authService.deposit(currentUser, sc.nextDouble());
                    sc.nextLine();
                }

                case 3 -> {
                    System.out.print("Withdraw amount: ");
                    authService.withdraw(currentUser, sc.nextDouble());
                    sc.nextLine();
                }

                case 4 -> {
                    System.out.print("Recipient username: ");
                    String receiver = sc.nextLine();
                    System.out.print("Amount: ");
                    authService.sendMoney(currentUser, receiver, sc.nextDouble());
                    sc.nextLine();
                }

                case 5 -> authService.printTransactionHistory(currentUser);

                case 6 -> running = false;
            }

            currentUser = authService.login(currentUser.getUsername(), currentUser.getPassword());
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
        }

        sc.close();
    }
}
