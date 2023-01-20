package com.example.almanac.database;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class DatabaseManager {

    Connection connection;
    public void connect() {
        if (connection != null) {
            isConnected();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Crafting.sqlite3");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void isConnected() {
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
                    IsRaw INTEGER NOT NULL
                    );""";

        String createTableRecipes = """
                CREATE TABLE Recipes (
                    ID INTEGER PRIMARY KEY,
                    ItemID INTEGER NOT NULL,
                    Need INTEGER NOT NULL,
                    Quantity INTEGER NOT NULL,
                    FOREIGN KEY (ItemID) REFERENCES Items(ID) ON DELETE CASCADE
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
    public void insertItem(String name, int isRaw) {
        try {
            String query = String.format(
                    "INSERT INTO Items (ID, ItemName, IsRaw) VALUES (null, '%s', %d)",
                    name.replace("'", "''"), isRaw);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.commit();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean scan() {
        Scanner in = new Scanner(System.in);
        System.out.print("\nItem name? (or press 'q' to quit) ");
        String name = in.nextLine();
        if (Objects.equals(name, "q")) return false;
        System.out.print("1 if it's a raw material; 0 if not ");
        int raw = in.nextInt();
        if (raw == 0) {
            askRecipe(name);
        }
        this.insertItem(name, raw);
        return true;
    }
    private void askRecipe(String name) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("\nWhat does this item need? (Press q when done) ");
            String need = in.nextLine();
            if (Objects.equals(name, "q")) return;
            System.out.print("\nHow many?");
            int quantity = in.nextInt();

        }
    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();

        boolean success;

        do {
            success = db.scan();
        } while (success);

        db.disconnect();
    }
}

