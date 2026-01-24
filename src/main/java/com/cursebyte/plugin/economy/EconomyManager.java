package com.cursebyte.plugin.economy;

import com.cursebyte.plugin.database.DatabaseManager;

import java.sql.*;
import java.util.UUID;

public class EconomyManager {

    public static void init() {
        createTable();
        createTransactionTable();
    }

    private static void createTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS economy (
                        uuid TEXT PRIMARY KEY,
                        balance REAL
                    );
                """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTransactionTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        sender TEXT,
                        receiver TEXT,
                        amount REAL,
                        time INTEGER,
                        description TEXT
                    )
                """;
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPlayer(UUID uuid) {
        String sql = "INSERT OR IGNORE INTO economy(uuid, balance) VALUES(?, ?)";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, 0.0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getBalance(UUID uuid) {
        String sql = "SELECT balance FROM economy WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public static void setBalance(UUID uuid, double amount) {
        String sql = "UPDATE economy SET balance = ? WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add(UUID uuid, double amount) {
        double current = getBalance(uuid);
        setBalance(uuid, current + amount);
    }

    public static void remove(UUID uuid, double amount) {
        double current = getBalance(uuid);
        setBalance(uuid, Math.max(0, current - amount));
    }
}