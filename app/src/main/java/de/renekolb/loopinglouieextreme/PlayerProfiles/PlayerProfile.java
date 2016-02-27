package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.os.Handler;

/**
 * Created by Admi on 27.02.2016.
 */
public class PlayerProfile {

    private String playerName;
    private PlayerAchievements playerAchievements;
    private PlayerStatistics playerStatistics;

    public PlayerProfile(String playerName, Handler mHandler){
        this.playerName = playerName;
        this.playerAchievements = new PlayerAchievements(mHandler);
        this.playerStatistics = new PlayerStatistics(this.playerAchievements);
    }

    public void load(){
        //load from file (with playerName) -> if not exist, create with defaults (=new Profile)
    }

    public void updateTotalRoundsPlayed(int add){
        playerStatistics.updateTotalRoundsPlayed(add);
    }

    public void updateTotalGamesPlayed(int add){
        playerStatistics.updateTotalGamesPlayed(add);
    }

    public void updateTotalRoundsWon(int add){
        playerStatistics.updateTotalRoundsWon(add);
    }

    public void updateTotalGamesWon(int add){
        playerStatistics.updateTotalGamesWon(add);
    }

}
