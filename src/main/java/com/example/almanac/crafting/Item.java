package com.example.almanac.crafting;

import java.util.HashMap;
import java.util.Objects;

public class Item {
    private String name;
    private Boolean isRaw;
    private HashMap<Item, Integer> recipe;

    public Item(String name, boolean isRaw) {
        this.name = name;
        this.isRaw = isRaw;
        this.recipe = new HashMap<Item, Integer>();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public HashMap<Item, Integer> getRecipe() {
        return recipe;
    }
    public void addRecipe(Item need, int quantity) {
        this.recipe.put(need, quantity);
    }
    public Boolean getRaw() {
        return isRaw;
    }
    public void setRaw(Boolean raw) {
        this.isRaw = raw;
    }
    public boolean equal(Item other) {
        return Objects.equals(this.name, other.name);
    }
}