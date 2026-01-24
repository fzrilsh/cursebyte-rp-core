package com.cursebyte.plugin.economy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cursebyte.plugin.database.DatabaseManager;
import com.cursebyte.plugin.modules.economy.EconomyService;

public class TransactionManager {

    public static void logTransfer(UUID from, UUID to, double amount) {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO transactions(sender, receiver, amount, time, description) VALUES(?,?,?,?,?)")) {
            ps.setString(1, from.toString());
            ps.setString(2, to.toString());
            ps.setDouble(3, amount);
            ps.setLong(4, System.currentTimeMillis());
            ps.setString(5, "Transfer");

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getTransactions(UUID uuid, int limit, int offset) {
        List<Transaction> list = new ArrayList<>();

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
                """
                        SELECT * FROM transactions
                        WHERE sender=? OR receiver=?
                        ORDER BY time DESC
                        LIMIT ? OFFSET ?
                        """)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, uuid.toString());
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getDouble("amount"),
                        rs.getLong("time"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean transfer(UUID from, UUID to, double amount) {
        if (EconomyService.getBalance(from) < amount)
            return false;

        EconomyManager.add(from, -amount);
        EconomyManager.add(to, amount);

        TransactionManager.logTransfer(from, to, amount);
        return true;
    }
}