package com.cursebyte.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class DatabaseManager {

    private static Connection connection;
    private static String dbPath;

    private static final ExecutorService DB_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Cursebyte-DB");
        t.setDaemon(true);
        return t;
    });

    public static void init(String pluginFolderPath) {
        try {
            dbPath = pluginFolderPath + "/data.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute("PRAGMA journal_mode = WAL;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static CompletableFuture<Void> runAsync(Runnable action) {
        return CompletableFuture.runAsync(action, DB_EXECUTOR);
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> action) {
        return CompletableFuture.supplyAsync(action, DB_EXECUTOR);
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA optimize;");
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB_EXECUTOR.shutdown();
        }
    }
}