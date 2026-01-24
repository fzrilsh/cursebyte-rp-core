package com.cursebyte.plugin.modules.citizen;

import com.cursebyte.plugin.database.DatabaseManager;

import java.sql.*;
import java.util.UUID;

public class CitizenRepository {

    public static void initTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS citizenship (
                uuid TEXT PRIMARY KEY,
                nik VARCHAR(20) UNIQUE,
                full_name VARCHAR(100),
                join_date TIMESTAMP,
                is_registered BOOLEAN DEFAULT FALSE
            );
        """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createIfNotExists(UUID uuid) {
        String sql = "INSERT OR IGNORE INTO citizenship(uuid) VALUES(?)";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(UUID uuid, String nik, String fullName, long joinDate, boolean registered) {
        String sql = """
            UPDATE citizenship
            SET nik = ?, full_name = ?, join_date = ?, is_registered = ?
            WHERE uuid = ?
        """;

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, nik);
            ps.setString(2, fullName);
            ps.setLong(3, joinDate);
            ps.setBoolean(4, registered);
            ps.setString(5, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRegistered(UUID uuid) {
        String sql = "SELECT is_registered FROM citizenship WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_registered");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static CitizenProfile get(UUID uuid) {
        String sql = "SELECT * FROM citizenship WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CitizenProfile(
                        uuid,
                        rs.getString("nik"),
                        rs.getString("full_name"),
                        rs.getLong("join_date"),
                        rs.getBoolean("is_registered")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}