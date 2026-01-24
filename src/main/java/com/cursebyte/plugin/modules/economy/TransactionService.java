package com.cursebyte.plugin.modules.economy;

import com.cursebyte.plugin.database.DatabaseManager;

import java.sql.*;
import java.util.*;

public class TransactionService {

    public static void log(UUID sender, UUID receiver, double amount, String desc){
        String sql = """
            INSERT INTO transactions(sender, receiver, amount, time, description)
            VALUES(?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, sender.toString());
            ps.setString(2, receiver.toString());
            ps.setDouble(3, amount);
            ps.setLong(4, System.currentTimeMillis());
            ps.setString(5, desc);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<TransactionRecord> getHistory(UUID uuid, int page, int size){
        List<TransactionRecord> list = new ArrayList<>();
        int offset = page * size;

        String sql = """
            SELECT * FROM transactions
            WHERE sender = ? OR receiver = ?
            ORDER BY time DESC
            LIMIT ? OFFSET ?
        """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, uuid.toString());
            ps.setInt(3, size);
            ps.setInt(4, offset);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new TransactionRecord(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getDouble("amount"),
                        rs.getLong("time"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }
}