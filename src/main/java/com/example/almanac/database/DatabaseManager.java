package com.example.almanac.database;

import com.example.almanac.crafting.Item;

import java.sql.*;
import java.util.*;

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
            ID = getItemID(name);
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
        int itemID = getItemID(name);
        int neededItemID = getItemID(need);
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
    private int getItemID(String name) {
        try {
            int ID;
            String query = String.format(
                    "SELECT ID FROM Items WHERE ItemName='%s'",
                    name.replace("'", "''"));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                ID = -1;
            } else {
                ID = resultSet.getInt("ID");
            }
            statement.close();
            return ID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private List<String> getItemInfo(int ID) {
        try {
            String query = String.format(
                    "SELECT * FROM Items WHERE ID=%d",
                    ID);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) throw new RuntimeException("error: item does not exist");

            String name = resultSet.getString("ItemName");
            int isRaw = resultSet.getInt("IsRaw");
            statement.close();
            return Arrays.asList(name, Integer.toString(isRaw));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Item getRecipe(String itemName) {
        int itemID = getItemID(itemName);
        if (itemID == -1) return null;
        return getRecipe(itemID);
    }
    private Item getRecipe(int itemID) {
        List<String> itemInfo = getItemInfo(itemID);
        String itemName = itemInfo.get(0);
        boolean isRaw = Objects.equals(itemInfo.get(1), "1");
        Item item = new Item(itemName, isRaw);
        if (isRaw) return item;

        try {
            String query = String.format(
                    "SELECT * FROM Recipes WHERE ItemID=%d", itemID);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int componentID = resultSet.getInt("Need");
            int quantity = resultSet.getInt("Quantity");
            item.addRecipe(getRecipe(componentID), quantity);
        }

            statement.close();
            return item;
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

