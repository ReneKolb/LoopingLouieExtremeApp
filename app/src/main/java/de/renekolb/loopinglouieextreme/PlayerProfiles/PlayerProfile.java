package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.content.Context;
import android.os.Handler;

import java.util.UUID;

/**
 * Created by Admi on 27.02.2016.
 */
public class PlayerProfile {

    private String playerName;
    private PlayerAchievements playerAchievements;
    private PlayerStatistics playerStatistics;
    private int profileID;
    //private boolean isGuest;

    public PlayerProfile(String playerName, Handler mHandler/*, boolean isGuest*/){
        //create a Guest Profile!
        this.playerName = playerName;
        //this.playerAchievements = new PlayerAchievements(mHandler);
        //this.playerStatistics = new PlayerStatistics(this.playerAchievements);
        this.playerAchievements = null;
        this.playerStatistics = null;

        this.profileID = -1;
        //this.isGuest = isGuest;
//        load();
    }

    PlayerProfile(int profileID, String playerName, PlayerStatistics ps){
        //Create a player Profile (with staticstics etc.)
        this.playerName = playerName;
        this.profileID = profileID;
        this.playerAchievements = ps.getPlayerAchievements();
        this.playerStatistics = ps;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void editPlayerName(String newName){
        this.playerName = newName;
    }

    public int getProfileID(){
        return this.profileID;
    }

    PlayerStatistics getPlayerStatistics(){
        return this.playerStatistics;
    }

    /*public void load(){
        //load from file (with playerName) -> if not exist, create with defaults (=new Profile)
        if(!isGuest()){

        }
    }

    public void save(){
        if(!isGuest()){

        }
    }*/

    public boolean isGuest(){
        return profileID == -1;
        //return isGuest;
    }

    public void updateTotalRoundsPlayed(int add){
        if(!isGuest())
            playerStatistics.updateTotalRoundsPlayed(add, true);
    }

    public void updateTotalGamesPlayed(int add){
        if(!isGuest())
            playerStatistics.updateTotalGamesPlayed(add, true);
    }

    public void updateTotalRoundsWon(int add){
        if(!isGuest())
            playerStatistics.updateTotalRoundsWon(add, true);
    }

    public void updateTotalGamesWon(int add){
        if(!isGuest())
            playerStatistics.updateTotalGamesWon(add, true);
    }

}
