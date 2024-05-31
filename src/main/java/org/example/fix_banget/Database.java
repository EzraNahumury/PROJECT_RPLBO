package org.example.fix_banget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection conn = null;

    public static Connection connect() {
        try {
            // Pastikan driver JDBC SQLite dimuat
            Class.forName("org.sqlite.JDBC");
            // Berikan path yang benar ke database SQLite Anda
            String url = "jdbc:sqlite:MEMBERSHIP.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
