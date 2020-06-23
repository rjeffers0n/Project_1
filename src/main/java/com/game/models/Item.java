package com.game.models;

public class Item {
    private final String itemName;
    private final String itemType;
    private final String itemCategory;
    private final int value;

    public Item(String itemName, String itemType, String itemCategory, int value) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemCategory = itemCategory;
        this.value = value;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public int getValue() {
        return value;
    }
}
