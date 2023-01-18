package com.example.almanac.database;

import java.sql.*;

public class DatabaseManager {

    Connection connection;
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Crafting:sqlite3");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConnected() {
        try {
            if (!connection.isClosed()) {
                throw new IllegalStateException("error: already connected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.disconnect();
    }
}

