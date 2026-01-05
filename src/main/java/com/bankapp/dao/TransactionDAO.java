package com.bankapp.dao;
import com.bankapp.model.Transaction;
import com.bankapp.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean save(Transaction tx) {
        String sql = "INSERT INTO transactions (sender_id, receiver_id, amount) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tx.getSenderId());
            stmt.setInt(2, tx.getReceiverId());
            stmt.setDouble(3, tx.getAmount());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Transaction> findByUserId(int userId) {
        String sql = """
            SELECT t.id, t.sender_id, t.receiver_id, t.amount, t.timestamp,
                   s.username AS sender_name, r.username AS receiver_name
            FROM transactions t
            JOIN users s ON t.sender_id = s.id
            JOIN users r ON t.receiver_id = r.id
            WHERE t.sender_id = ? OR t.receiver_id = ?
            ORDER BY t.timestamp DESC
            """;

        List<Transaction> list = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setSenderId(rs.getInt("sender_id"));
                tx.setReceiverId(rs.getInt("receiver_id"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setTimestamp(rs.getTimestamp("timestamp"));
                tx.setSenderName(rs.getString("sender_name"));
                tx.setReceiverName(rs.getString("receiver_name"));
                list.add(tx);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean saveDeposit(int userId, double amount) {
        String sql = "INSERT INTO transactions (sender_id, receiver_id, amount) VALUES (NULL, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveWithdraw(int userId, double amount) {
        String sql = "INSERT INTO transactions (sender_id, receiver_id, amount) VALUES (?, NULL, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
