package de.renekolb.loopinglouieextreme;

import android.util.Log;

import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;

public class GamePlayer {

    //private final String defaultName;
    private String displayName;
    private final PlayerColor playerColor;
    private int points;
    private ItemType defaultItemType;
    private final ItemStack itemStack;
    private int currentChips;

    private ConnectionState connectionState;
    private String remoteAddress;

    private PlayerProfile playerProfile;

    public GamePlayer(PlayerColor playerColor, boolean localPlayer) {
        //this.defaultName = defaultName;
        //this.playerProfile = profile;
        this.displayName = null;
        this.playerColor = playerColor;
        this.itemStack = new ItemStack();
        this.defaultItemType = ItemType.NONE;
        this.currentChips = -1;
        this.connectionState = localPlayer ? ConnectionState.LOCAL : ConnectionState.OPEN;
        this.remoteAddress = null;
    }

    public void setPlayerProfile(PlayerProfile profile){
        this.playerProfile = profile;
        this.connectionState = ConnectionState.LOCAL;
        this.remoteAddress = null;
        this.displayName = null;
    }

    public boolean isGuest(){
        return this.playerProfile == null ||this.playerProfile.isGuest();
    }

    public String getDisplayName(){
        if(this.displayName != null) {
            return displayName;
        }
        if(playerProfile != null) {
            return playerProfile.getPlayerName();
        }
        return "No profile Profile";
    }

    public void setGuestName(String newName) {
        if(isGuest()) {
            this.displayName = newName;
        }else{
            Log.e("Tag","Cannot set Player Name. Only Guest Names can be changed");
        }
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
            Log.w("GamePlayer", "Wrong method to set Connected connectionState?");
            //but as a client it's ok...
            //return;
        }
        this.connectionState = connectionState;
        this.remoteAddress = null; // clear eventually remaining old address
        this.displayName = null;
    }

    public void setConnectionState(String connectedRemoteAddress) {
        this.connectionState = ConnectionState.CONNECTED;
        this.remoteAddress = connectedRemoteAddress;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

}
