package com.cursebyte.plugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static Connection connection;
    private static String dbPath;

    public static void init(String pluginFolderPath) {
        try {
            dbPath = pluginFolderPath + "/data.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}