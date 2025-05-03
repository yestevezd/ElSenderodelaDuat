package com.yestevezd.elsenderodeladuat.core.inventory;

import com.yestevezd.elsenderodeladuat.core.entities.Item;

public class Inventory {
    private final Item[] slots;

    public Inventory(int size) {
        this.slots = new Item[size];
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = item;
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(Class<? extends Item> itemType) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && itemType.isInstance(slots[i])) {
                slots[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(Class<? extends Item> itemType) {
        for (Item item : slots) {
            if (item != null && itemType.isInstance(item)) {
                return true;
            }
        }
        return false;
    }

    public Item[] getItems() {
        return slots;
    }
}
