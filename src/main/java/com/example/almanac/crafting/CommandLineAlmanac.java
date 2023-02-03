package com.example.almanac.crafting;

import com.example.almanac.database.DatabaseManager;

import java.util.*;

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
    private String spaces(int n) {
        String space = "";
        for (int i=0; i<n; i++) space += "    ";
        return space;
    }
    // System.out.println();
    private void printRecipe(Item item, int level) {
        if (level == 0) {
            if (item.getRaw()) System.out.println(item.getName() + " does not need a recipe.");
            else System.out.println(item.getName() + " needs:");
        }
        if (!item.getRaw()) {
            HashMap<Item, Integer> componentRecipe = item.getRecipe();
            for (Item component : componentRecipe.keySet()) {
                System.out.println(spaces(level + 1) + " x " + component.getName());
                printRecipe(component, level + 1);
            }
        }
    }
    public void inquire() {
        Scanner s = new Scanner(System.in);
        System.out.print("\nItem name? (or press 'q' to quit) ");
        String name = s.nextLine();
        if (Objects.equals(name, "q")) return;

        Item target = retrieveItem(name);
        printRecipe(target, 0);
    }
    public static void main(String[] args) {
        CommandLineAlmanac cla = new CommandLineAlmanac();
        cla.inquire();
    }
}
