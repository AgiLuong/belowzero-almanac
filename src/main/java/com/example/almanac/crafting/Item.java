package com.example.almanac.crafting;

import java.util.ArrayList;

public class Item {
    private String name;
    private boolean isRaw;
    private ArrayList<Item> recipe;

    public Item(String name, boolean isRaw) {
        this.name = name;
        this.isRaw = isRaw;
        this.recipe = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isRaw() {
        return isRaw;
    }
    public void setRaw(boolean raw) {
        isRaw = raw;
    }

    public ArrayList<Item> getRecipe() {
        return recipe;
    }
    public void addRecipe(Item need) {
        this.recipe.add(need);
    }
}
