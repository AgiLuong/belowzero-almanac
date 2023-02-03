package com.example.almanac.crafting;

import com.example.almanac.database.DatabaseManager;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandLineAlmanac {
    private final DatabaseManager manager;
    private final HashSet<Item> items;
    public CommandLineAlmanac() {
        this.manager = new DatabaseManager();
        this.items = new HashSet<>();
    }
    private Item retrieveItem(String itemName) {
        if (this.items.stream().map(Item::getName).anyMatch(s -> s.contains(itemName))) {
            return (Item) this.items.stream().filter(i -> Objects.equals(i.getName(), itemName));
        }
        manager.connect();
        Item retrieved = manager.getRecipe(itemName);
        manager.disconnect();
        this.items.add(retrieved);
        return retrieved;
    }
    private void printRecipe() {



    }
}
