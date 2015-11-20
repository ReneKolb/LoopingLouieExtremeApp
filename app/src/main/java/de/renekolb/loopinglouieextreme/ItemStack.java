package de.renekolb.loopinglouieextreme;

import java.util.LinkedList;

public class ItemStack {
    private LinkedList<ItemType> items;
    private int maxItems;

    public ItemStack() {
        this(4);
    }

    public ItemStack(int maxItems) {
        this.items = new LinkedList<>();
        this.maxItems = maxItems;
    }

    public void addItem(ItemType itemType) {
        addItem(itemType, 1);
    }

    public void addItem(ItemType itemType, int amount) {
        if (items.size() + amount > maxItems) {
            amount = maxItems - items.size();
        }

        for (int i = 1; i <= amount; i++) {
            items.add(itemType);
        }
    }

    public ItemType getNextItem() {
        return items.getFirst();
    }

    public void removeItem() {
        items.removeFirst();
    }

    public void clear() {
        items.clear();
    }

    public int getItemsAmount() {
        return this.items.size();
    }

    public boolean hasItems() {
        return !this.items.isEmpty();
    }
}
