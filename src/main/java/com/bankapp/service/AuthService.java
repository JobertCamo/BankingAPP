package com.bankapp.service;

import com.bankapp.dao.TransactionDAO;
import com.bankapp.dao.UserDAO;
import com.bankapp.model.Transaction;
import com.bankapp.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class AuthService {

    private UserDAO userDAO = new UserDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public boolean register(String firstName, String lastName, String username, String password) {
        if (userDAO.findByUsername(username) != null) {
            System.out.println("Username already exists!");
            return false;
        }
        User user = new User(firstName, lastName, username, password, 1000);
        return userDAO.save(user);
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) return user;
        System.out.println("Invalid username or password!");
        return null;
    }

    public boolean sendMoney(User sender, String receiverUsername, double amount) {
        if (amount <= 0 || sender.getBalance() < amount) {
            System.out.println("Insufficient balance or invalid amount!");
            return false;
        }

        User receiver = userDAO.findByUsername(receiverUsername);
        if (receiver == null) {
            System.out.println("Receiver not found!");
            return false;
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userDAO.save(sender);
        userDAO.save(receiver);

        Transaction tx = new Transaction();
        tx.setSenderId(sender.getId());
        tx.setReceiverId(receiver.getId());
        tx.setAmount(amount);
        transactionDAO.save(tx);

        System.out.println("=======================================");
        System.out.println("        Transaction successful!");
        System.out.println("=======================================");
        return true;
    }

    public void printTransactionHistory(User user) {
        List<Transaction> transactions = transactionDAO.findByUserId(user.getId());

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions yet.");
            return;
        }

        System.out.println("\n=======================================");
        System.out.println("===  Transaction History (Last 10)  ===");
        System.out.println("=======================================");
        System.out.printf("\n%-10s %-15s %-10s %-20s%n", "Type", "User", "Amount", "Timestamp");
        System.out.println("-----------------------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int count = 0;
        for (Transaction tx : transactions) {
            if (count >= 10) break;
            String type;
            String otherUser;

            if (tx.getSenderId() == 0) {
                type = "Deposit";
                otherUser = "-";
            } else if (tx.getReceiverId() == 0) {
                type = "Withdraw";
                otherUser = "-";
            } else if (tx.getSenderId() == user.getId()) {
                type = "Sent";
                otherUser = tx.getReceiverName();
            } else {
                type = "Received";
                otherUser = tx.getSenderName();
            }


            System.out.printf("%-10s %-15s %-10.2f %-20s%n",
                    type, otherUser, tx.getAmount(), sdf.format(tx.getTimestamp()));

            count++;
        }

        System.out.println("-----------------------------------------------------------");
    }

    public boolean deposit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("\n=======================================");
            System.out.println("        Invalid deposit amount.        ");
            System.out.println("=======================================");
            return false;
        }

        user.setBalance(user.getBalance() + amount);
        userDAO.save(user);
        transactionDAO.saveDeposit(user.getId(), amount);

        System.out.println("\n=======================================");
        System.out.println("          Deposit successful!          ");
        System.out.println("=======================================");
        return true;
    }

    public boolean withdraw(User user, double amount) {
        if (amount <= 0 || amount > user.getBalance()) {
            System.out.println("Invalid or insufficient balance.");
            return false;
        }

        user.setBalance(user.getBalance() - amount);
        userDAO.save(user);
        transactionDAO.saveWithdraw(user.getId(), amount);

        System.out.println("Withdrawal successful!");
        return true;
    }


}
