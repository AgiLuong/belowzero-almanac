package com.example.almanac.crafting;

import java.util.HashMap;
import java.util.Objects;

public class Item {
    private String name;
    private boolean isRaw;
    private HashMap<Item, Integer> recipe;

    public Item(String name, boolean isRaw) {
        this.name = name;
        this.isRaw = isRaw;
        this.recipe = new HashMap<Item, Integer>();
    }
    public String getName() {
        return name;
    }
    public HashMap<Item, Integer> getRecipe() {
        return recipe;
    }
    public void addRecipe(Item need, int quantity) {
        this.recipe.put(need, quantity);
    }
    public boolean getRaw() {
        return isRaw;
    }
}