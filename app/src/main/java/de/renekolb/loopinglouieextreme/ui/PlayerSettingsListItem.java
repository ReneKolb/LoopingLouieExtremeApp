package de.renekolb.loopinglouieextreme.ui;

import android.graphics.Color;

import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.PlayerColor;

public class PlayerSettingsListItem {

    private String playerName;
    private int color;
    private String booster;
    private int chipAmount;

    private boolean playerEnabled;
    private boolean remotePlayer;

    /*public PlayerSettingsListItem(String playerName, PlayerColor color, String booster, int chipAmount){
        this.playerName = playerName;
        this.color = color.getColor();
        this.booster  = booster;
        this.chipAmount = chipAmount;
    }*/

    public PlayerSettingsListItem(GamePlayer player){
        this.playerName = player.getDisplayName();
        this.color = player.getPlayerColor().getColor();
        this.booster = player.getDefaultItemType()==null?"None":player.getDefaultItemType().getDisplayName();
        this.chipAmount = player.getCurrentChips();
        this.playerEnabled = player.isEnabled();
        this.remotePlayer = player.isRemotePlayer();
    }

    public void update(GamePlayer player){
        this.playerName = player.getDisplayName();
        this.color = player.getPlayerColor().getColor();
        this.booster = player.getDefaultItemType()==null?"None":player.getDefaultItemType().getDisplayName();
        this.chipAmount = player.getCurrentChips();
        this.playerEnabled = player.isEnabled();
        this.remotePlayer = player.isRemotePlayer();
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public int getColor(){
        return this.color;
    }

    public String getBooster(){
        return this.booster;
    }

    public int getChipAmount(){
        return this.chipAmount;
    }

    public boolean isPlayerEnabled(){
        return this.playerEnabled;
    }

    public boolean isRemotePlayer(){
        return this.remotePlayer;
    }

    @Deprecated
    public void setPlayerName(String newName){
        //just for testing the SYSTEM
        this.playerName = newName;
    }

    @Deprecated
    public void setBooster(String newBooser){
        //just for TEsting the system
        this.booster = newBooser;
    }
}
