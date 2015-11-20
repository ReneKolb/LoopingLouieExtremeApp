package de.renekolb.loopinglouieextreme;

import android.util.Log;

public class GamePlayer {

    private String displayName;
    private PlayerColor playerColor;
    private int points;
    private ItemType defaultItemType;
    private ItemStack itemStack;
    private int currentChips;

    private ConnectionState connectionState;
    private String remoteAddress;

    public GamePlayer(String displayName, PlayerColor playerColor, boolean localPlayer) {
        this.displayName = displayName;
        this.playerColor = playerColor;
        this.itemStack = new ItemStack();
        this.defaultItemType = null;
        this.currentChips = 0;
        this.connectionState = localPlayer ? ConnectionState.LOCAL : ConnectionState.OPEN;
        this.remoteAddress = null;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String newName) {
        this.displayName = newName;
    }

    public PlayerColor getPlayerColor() {
        return this.playerColor;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public boolean hasItem() {
        return this.itemStack.hasItems();
    }

    public int getItemsAmount() {
        return this.itemStack.getItemsAmount();
    }

    public ItemType getDefaultItemType() {
        return this.defaultItemType;
    }

    public void setDefaultItemType(ItemType itemType) {
        this.defaultItemType = itemType;
    }

    public void addItem(ItemType itemType) {
        this.itemStack.addItem(itemType);
    }

    public void addItem(ItemType itemType, int amount) {
        this.itemStack.addItem(itemType, amount);
    }

    public ItemType getNextItem() {
        return this.itemStack.getNextItem();
    }

    public void removeItem() {
        this.itemStack.removeItem();
    }

    public void clearItems() {
        this.itemStack.clear();
    }

    public int getCurrentChips() {
        return this.currentChips;
    }

    public void setCurrentChips(int currentChips) {
        this.currentChips = currentChips;
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        if (connectionState == ConnectionState.CONNECTED) {
            Log.e("GamePlayer", "Wrong method to set Connected connectionState", new Exception());
            return;
        }
        this.connectionState = connectionState;
    }

    public void setConnectionState(String connectedRemoteAddress) {
        this.connectionState = ConnectionState.CONNECTED;
        this.remoteAddress = connectedRemoteAddress;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

}
