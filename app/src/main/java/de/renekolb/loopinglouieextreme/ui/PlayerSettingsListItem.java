package de.renekolb.loopinglouieextreme.ui;

import android.graphics.Color;

import de.renekolb.loopinglouieextreme.Connection;
import de.renekolb.loopinglouieextreme.ConnectionState;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.ItemType;
import de.renekolb.loopinglouieextreme.PlayerColor;

public class PlayerSettingsListItem {

    private String playerName;
    private int color;
    private ItemType itemType;
    private int chipAmount;

    private ConnectionState connectionState;

    /*public PlayerSettingsListItem(String playerName, PlayerColor color, String booster, int chipAmount){
        this.playerName = playerName;
        this.color = color.getColor();
        this.booster  = booster;
        this.chipAmount = chipAmount;
    }*/

    public PlayerSettingsListItem(GamePlayer player){
        this.playerName = player.getDisplayName();
        this.color = player.getPlayerColor().getColor();
        this.itemType = player.getDefaultItemType();
        this.chipAmount = player.getCurrentChips();
        this.connectionState = player.getConnectionState();
    }

    public void update(GamePlayer player){
        this.playerName = player.getDisplayName();
        this.color = player.getPlayerColor().getColor();
        this.itemType = player.getDefaultItemType();
        this.chipAmount = player.getCurrentChips();
        this.connectionState = player.getConnectionState();
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public int getColor(){
        return this.color;
    }

    public ItemType getBooster(){
        return this.itemType;
    }

    public int getChipAmount(){
        return this.chipAmount;
    }

    public ConnectionState getConnectionState(){
        return this.connectionState;
    }
}
