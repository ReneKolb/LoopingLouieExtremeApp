package de.renekolb.loopinglouieextreme.PlayerProfiles;

/**
 * Created by Admi on 27.02.2016.
 */
public class PlayerStatistics {

    private int totalRoundsPlayed;
    private int totalGamesPlayed;
    private int totalRoundsWon;
    private int totalGamesWon;
    private long totalEarnedPoints;
    private int[] usedItemsAmount;

    private PlayerAchievements playerAchievements;

    public PlayerStatistics(PlayerAchievements playerAchievements){
        this.playerAchievements = playerAchievements;
        this.totalRoundsPlayed = 0; //or load from file
        this.totalGamesPlayed = 0;
        this.totalRoundsWon = 0;
        this.totalGamesWon = 0;
        this.totalEarnedPoints = 0;
        this.usedItemsAmount = new int[]{0,0,0,0};
    }

    public void updateTotalRoundsPlayed(int add){
        this.totalRoundsPlayed += add;

        if(!playerAchievements.hasUnlocked(Achievements.PLAY_A_ROUND) && totalRoundsPlayed >= 1){
            playerAchievements.addAchievement(Achievements.PLAY_A_ROUND);
        }
        if(playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_ROUNDS) && totalRoundsPlayed >= 5){
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_ROUNDS);
        }
    }

    public void updateTotalGamesPlayed(int add){
        this.totalGamesPlayed += add;

        if(!playerAchievements.hasUnlocked(Achievements.PLAY_A_GAME) && totalGamesPlayed >= 1){
            playerAchievements.addAchievement(Achievements.PLAY_A_GAME);
        }
        if(playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_GAMES) && totalGamesPlayed >= 5){
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_GAMES);
        }
    }

    public void updateTotalRoundsWon(int add){
        this.totalRoundsWon += add;

        if(!playerAchievements.hasUnlocked(Achievements.WIN_A_ROUND) && totalRoundsWon >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_ROUND);
        }
    }

    public void updateTotalGamesWon(int add){
        this.totalGamesWon += add;

        if(!playerAchievements.hasUnlocked(Achievements.WIN_A_GAME) && totalGamesWon >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_GAME);
        }
    }

}
