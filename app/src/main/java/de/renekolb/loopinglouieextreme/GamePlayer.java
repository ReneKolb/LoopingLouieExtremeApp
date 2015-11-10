package de.renekolb.loopinglouieextreme;

public class GamePlayer {

    private String displayName;
    private PlayerColor playerColor;
    private int points;
    private ItemType defaultItemType;
    private ItemStack itemStack;
    private int currentChips;

    private boolean enabled;
    private boolean remotePlayer;

    public GamePlayer(String displayName, PlayerColor playerColor){
        this.displayName = displayName;
        this.playerColor = playerColor;
        this.itemStack = new ItemStack();
        this.defaultItemType = null;
        this.currentChips = 0;
        this.enabled = true;
        this.remotePlayer = false;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public void setDisplayName(String newName){
        this.displayName = newName;
    }

    public PlayerColor getPlayerColor(){
        return this.playerColor;
    }

    public int getPoints(){
        return this.points;
    }

    public void addPoints(int amount){
        this.points += amount;
    }

    public boolean hasItem(){
        return this.itemStack.hasItems();
    }

    public int getItemsAmount(){
        return this.itemStack.getItemsAmount();
    }

    public ItemType getDefaultItemType(){
        return this.defaultItemType;
    }

    public void setDefaultItemType(ItemType itemType){
        this.defaultItemType = itemType;
    }

    public void addItem(ItemType itemType){
        this.itemStack.addItem(itemType);
    }

    public void addItem(ItemType itemType, int amount){
        this.itemStack.addItem(itemType, amount);
    }

    public ItemType getNextItem(){
        return this.itemStack.getNextItem();
    }

    public void removeItem(){
        this.itemStack.removeItem();
    }

    public void clearItems(){
        this.itemStack.clear();
    }

    public int getCurrentChips(){
        return this.currentChips;
    }

    public void setCurrentChips(int currentChips) {
        this.currentChips = currentChips;
    }

    public boolean isEnabled(){
        return this.enabled;
    }

    public void setEnabled(boolean isEnabled){
        this.enabled = isEnabled;
    }

    public boolean isRemotePlayer(){
        return this.remotePlayer;
    }

    public void setRemotePlayer(boolean isRemotePlayer){
        this.remotePlayer = isRemotePlayer;
    }

}
