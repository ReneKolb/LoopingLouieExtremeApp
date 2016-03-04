package de.renekolb.loopinglouieextreme.PlayerProfiles;

import java.io.Serializable;

/**
 * Created by Admi on 27.02.2016.
 */
public class PlayerStatistics implements Serializable{

    private int totalRoundsPlayed;
    private int totalGamesPlayed;
    private int totalRoundsWon;
    private int totalGamesWon;
    private long totalEarnedPoints;
    private int[] usedItemsAmount;

    private transient PlayerAchievements playerAchievements;

    public PlayerStatistics(PlayerAchievements playerAchievements){
        this.playerAchievements = playerAchievements;
        this.totalRoundsPlayed = 0; //or load from file
        this.totalGamesPlayed = 0;
        this.totalRoundsWon = 0;
        this.totalGamesWon = 0;
        this.totalEarnedPoints = 0;
        this.usedItemsAmount = new int[]{0,0,0,0};
    }

    void setPlayerAchievements(PlayerAchievements pa){
        this.playerAchievements = pa;
    }

    PlayerAchievements getPlayerAchievements(){
        return this.playerAchievements;
    }

    public void updateAllAchievements(boolean notifyUser){
        updateTotalRoundsPlayed(0, false);
        updateTotalGamesPlayed(0, false);
        updateTotalRoundsWon(0, false);
        updateTotalGamesWon(0, false);
    }

    public void updateTotalRoundsPlayed(int add, boolean notifyUser){
        this.totalRoundsPlayed += add;

        if(!playerAchievements.hasUnlocked(Achievements.PLAY_A_ROUND) && totalRoundsPlayed >= 1){
            playerAchievements.addAchievement(Achievements.PLAY_A_ROUND, notifyUser);
        }
        if(playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_ROUNDS) && totalRoundsPlayed >= 5){
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_ROUNDS, notifyUser);
        }
    }

    public void updateTotalGamesPlayed(int add, boolean notifyUser){
        this.totalGamesPlayed += add;

        if(!playerAchievements.hasUnlocked(Achievements.PLAY_A_GAME) && totalGamesPlayed >= 1){
            playerAchievements.addAchievement(Achievements.PLAY_A_GAME, notifyUser);
        }
        if(playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_GAMES) && totalGamesPlayed >= 5){
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_GAMES, notifyUser);
        }
    }

    public void updateTotalRoundsWon(int add, boolean notifyUser){
        this.totalRoundsWon += add;

        if(!playerAchievements.hasUnlocked(Achievements.WIN_A_ROUND) && totalRoundsWon >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_ROUND, notifyUser);
        }
    }

    public void updateTotalGamesWon(int add, boolean notifyUser){
        this.totalGamesWon += add;

        if(!playerAchievements.hasUnlocked(Achievements.WIN_A_GAME) && totalGamesWon >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_GAME, notifyUser);
        }
    }

}
