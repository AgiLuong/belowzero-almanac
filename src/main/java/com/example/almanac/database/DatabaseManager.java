package com.example.almanac.database;

import com.example.almanac.crafting.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            connection.commit();
            statement.close();
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
        int ID = -1;
        try {
            ID = getItemInfo(name).get(0);
        } catch (RuntimeException ignored) {}
        if (ID != -1) System.out.println("warning: item might have been added already");
        System.out.println("0 if it's not a raw material; 1 if it is; 2 to enter a new item ");

        int raw = in.nextInt();
        if (raw == 2) return true;
        if (ID == -1) this.insertItem(name, raw);
        if (raw == 0) {
            askRecipe(name);
        }
        return true;
    }
    private void askRecipe(String name) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\nWhat does this item need? (Press q when done) ");
            String need = in.nextLine();
            if (Objects.equals(need, "q")) return;
            System.out.println("How many? ");
            int quantity = Integer.parseInt(in.nextLine());
            addRecipeToDB(name, need, quantity);
        }
    }
    private void addRecipeToDB(String name, String need, int quantity) {
        int itemID = getItemInfo(name).get(0);
        int neededItemID = getItemInfo(need).get(0);
        try {
            String query = String.format(
                    "INSERT INTO Recipes (ID, ItemID, Need, Quantity) VALUES (null, %d, %d, %d)",
                    itemID, neededItemID, quantity);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /** Return an ArrayList containing information about the given item name
     *  Index 0 is the ID; index 1 is the raw status
    **/
    private ArrayList<Integer> getItemInfo(String name) {
        try {
            String query = String.format(
                    "SELECT * FROM Items WHERE ItemName='%s'",
                    name.replace("'", "''"));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) throw new RuntimeException("error: item " + name + " does not exist");

            int ID = resultSet.getInt("ID");
            int isRaw = resultSet.getInt("IsRaw");
            statement.close();
            return new ArrayList<>(Arrays.asList(ID, isRaw));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getItemName(int ID) {
        try {
            String query = String.format(
                    "SELECT ItemName FROM Items WHERE ID=%d",
                    ID);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) throw new RuntimeException("error: item does not exist");

            String name = resultSet.getString("ItemName");
            statement.close();
            return name;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

