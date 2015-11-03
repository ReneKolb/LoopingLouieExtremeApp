package de.renekolb.loopinglouieextreme.ui;

import android.graphics.Color;

/**
 * Created by Admi on 03.11.2015.
 */
public class PlayerSettingsData {

    private String playerName;
    private int color;
    private String booster;

    public PlayerSettingsData(String playerName, int color, String booster){
        this.playerName = playerName;
        this.color = color;
        this.booster  = booster;
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

    public void setPlayerName(String newName){
        this.playerName = newName;
    }

    public void setBooster(String newBooser){
        this.booster = newBooser;
    }
}
