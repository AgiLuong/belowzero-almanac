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

        if (retrieved != null) this.items.add(retrieved);
        return retrieved;
    }
    private String spaces(int n) {
        String space = "";
        for (int i=0; i<n; i++) space += "    ";
        return space;
    }
    private void printRecipe(Item item, int level) {
        if (level == 0) {
            if (item.getRaw()) System.out.println(item.getName() + " does not need a recipe.");
            else System.out.println(item.getName() + " needs:");
        }
        if (!item.getRaw()) {
            HashMap<Item, Integer> componentRecipe = item.getRecipe();
            for (Item component : componentRecipe.keySet()) {
                System.out.print(spaces(level + 1) + "+ ");
                System.out.print(componentRecipe.get(component));
                System.out.println(" " + component.getName());
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
    private String createString(Item item, int level) {
        StringBuilder result = new StringBuilder();
        if (level == 0) {
            if (item.getRaw()) {
                return item.getName() + " does not need a recipe.";
            } else {
                result.append(item.getName()).append(" needs:\n");
            }
        }
        if (!item.getRaw()) {
            HashMap<Item, Integer> componentRecipe = item.getRecipe();
            for (Item component : componentRecipe.keySet()) {
                result.append(spaces(level + 1)).append("+ ");
                result.append(componentRecipe.get(component));
                result.append(" ").append(component.getName()).append("\n");
                result.append(createString(component, level + 1));
            }
        }
        return result.toString();
    }
    private String sanitizeString(String input) {
        String[] split = input.split(" ");
        StringBuilder result = new StringBuilder();
        for (String s : split) {
            result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }
        return result.substring(0, input.length());
    }
    public String getRecipeAsString(String itemName) {
        Item item = retrieveItem(sanitizeString(itemName));
        if (item == null) return "Item " + itemName + " does not exist.";
        return createString(item, 0);
    }
    public static void main(String[] args) {
        CommandLineAlmanac cla = new CommandLineAlmanac();
        cla.inquire();
    }
}
