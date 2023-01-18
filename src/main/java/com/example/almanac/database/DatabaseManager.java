package com.example.almanac.database;

import java.sql.*;

public class DatabaseManager {

    Connection connection;
    public void connect() {
        if (connection != null) {
            checkConnected();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Crafting.sqlite3");
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
    private void createTables() {
        String createTableItems = """
                CREATE TABLE Items (
                    ID INTEGER PRIMARY KEY,
                    ItemName VARCHAR(255) NOT NULL,
                    IsPrimitive VARCHAR(255) NOT NULL
                    );""";

        String createTableRecipes = """
                CREATE TABLE Recipes (
                    ID INTEGER PRIMARY KEY,
                    ItemName VARCHAR(255) NOT NULL,
                    Prerequisites INTEGER NOT NULL,
                    FOREIGN KEY (Prerequisites) REFERENCES Items(ID) ON DELETE CASCADE
                    );""";
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableItems);
            statement.execute(createTableRecipes);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

