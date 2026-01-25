package com.cursebyte.plugin.modules.report;

import java.util.UUID;

import com.cursebyte.plugin.database.DatabaseManager;

public class ReportRepository {

    public static void init() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS reports (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        reporter TEXT,
                        target TEXT,
                        crime_type TEXT,
                        description TEXT,
                        time INTEGER,
                        status TEXT
                    )
                """;

        try (var st = DatabaseManager.getConnection().createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert(UUID reporter, UUID target, String crime, String desc) {
        String sql = "INSERT INTO reports(reporter, target, crime_type, description, time, status) VALUES(?,?,?,?,?,?)";

        try (var ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, reporter.toString());
            ps.setString(2, target.toString());
            ps.setString(3, crime);
            ps.setString(4, desc);
            ps.setLong(5, System.currentTimeMillis());
            ps.setString(6, "PENDING");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}