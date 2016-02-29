package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.os.Handler;

/**
 * Created by Admi on 27.02.2016.
 */
public class PlayerProfile {

    private String playerName;
    private PlayerAchievements playerAchievements;
    private PlayerStatistics playerStatistics;
    private boolean isGuest;

    public PlayerProfile(String playerName, Handler mHandler, boolean isGuest){
        this.playerName = playerName;
        this.playerAchievements = new PlayerAchievements(mHandler);
        this.playerStatistics = new PlayerStatistics(this.playerAchievements);

        this.isGuest = isGuest;
        load();
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void load(){
        //load from file (with playerName) -> if not exist, create with defaults (=new Profile)
        if(!isGuest()){

        }
    }

    public void save(){
        if(!isGuest()){

        }
    }

    public boolean isGuest(){
        return isGuest;
    }

    public void updateTotalRoundsPlayed(int add){
        if(!isGuest())
            playerStatistics.updateTotalRoundsPlayed(add);
    }

    public void updateTotalGamesPlayed(int add){
        if(!isGuest())
            playerStatistics.updateTotalGamesPlayed(add);
    }

    public void updateTotalRoundsWon(int add){
        if(!isGuest())
            playerStatistics.updateTotalRoundsWon(add);
    }

    public void updateTotalGamesWon(int add){
        if(!isGuest())
            playerStatistics.updateTotalGamesWon(add);
    }

}
