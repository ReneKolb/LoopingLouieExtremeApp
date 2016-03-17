package de.renekolb.loopinglouieextreme.PlayerProfiles;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerStatistics implements Serializable {

    /*private int totalRoundsPlayed;
    private int totalGamesPlayed;
    private int totalRoundsWon;
    private int totalGamesWon;
    private long totalEarnedPoints;
    private int[] usedItemsAmount;*/
    private HashMap<StatisticType, Long> statistics;

    private transient PlayerAchievements playerAchievements;

    public PlayerStatistics(PlayerAchievements playerAchievements) {
        this.playerAchievements = playerAchievements;
        this.statistics = new HashMap<>();
        for (StatisticType st : StatisticType.values()) {
            this.statistics.put(st, 0L);
        }
        /*this.totalRoundsPlayed = 0; //or load from file
        this.totalGamesPlayed = 0;
        this.totalRoundsWon = 0;
        this.totalGamesWon = 0;
        this.totalEarnedPoints = 0;
        this.usedItemsAmount = new int[]{0,0,0,0};*/
    }

    void setPlayerAchievements(PlayerAchievements pa) {
        this.playerAchievements = pa;
    }

    PlayerAchievements getPlayerAchievements() {
        return this.playerAchievements;
    }

    public void updateAllAchievements(boolean notifyUser) {
        updateTotalRoundsPlayed(0, false);
        updateTotalGamesPlayed(0, false);
        updateTotalRoundsWon(0, false);
        updateTotalGamesWon(0, false);
    }

    public void updateTotalRoundsPlayed(int add, boolean notifyUser) {
        addValue(StatisticType.TOTAL_ROUNDS_PLAYED, add);
        //this.totalRoundsPlayed += add;

        if (!playerAchievements.hasUnlocked(Achievements.PLAY_A_ROUND) && this.statistics.get(StatisticType.TOTAL_ROUNDS_PLAYED) >= 1) {
            playerAchievements.addAchievement(Achievements.PLAY_A_ROUND, notifyUser);
        }
        if (!playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_ROUNDS) && this.statistics.get(StatisticType.TOTAL_ROUNDS_PLAYED) >= 5) {
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_ROUNDS, notifyUser);
        }
    }

    public void updateTotalGamesPlayed(int add, boolean notifyUser) {
        addValue(StatisticType.TOTAL_GAMES_PLAYED, add);
        //this.totalGamesPlayed += add;

        if (!playerAchievements.hasUnlocked(Achievements.PLAY_A_GAME) && this.statistics.get(StatisticType.TOTAL_GAMES_PLAYED) >= 1) {
            playerAchievements.addAchievement(Achievements.PLAY_A_GAME, notifyUser);
        }
        if (!playerAchievements.hasUnlocked(Achievements.PLAY_FIVE_GAMES) && this.statistics.get(StatisticType.TOTAL_GAMES_PLAYED) >= 5) {
            playerAchievements.addAchievement(Achievements.PLAY_FIVE_GAMES, notifyUser);
        }
    }

    public void updateTotalRoundsWon(int add, boolean notifyUser) {
        addValue(StatisticType.TOTAL_ROUNDS_WON, add);
        //this.totalRoundsWon += add;

        if (!playerAchievements.hasUnlocked(Achievements.WIN_A_ROUND) && this.statistics.get(StatisticType.TOTAL_ROUNDS_WON) >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_ROUND, notifyUser);
        }
        if (!playerAchievements.hasUnlocked(Achievements.WIN_FIVE_ROUNDS) && this.statistics.get(StatisticType.TOTAL_ROUNDS_WON) >= 5) {
            playerAchievements.addAchievement(Achievements.WIN_FIVE_ROUNDS, notifyUser);
        }
    }

    public void updateTotalGamesWon(int add, boolean notifyUser) {
        addValue(StatisticType.TOTAL_GAMES_WON, add);
        //this.totalGamesWon += add;

        if (!playerAchievements.hasUnlocked(Achievements.WIN_A_GAME) && this.statistics.get(StatisticType.TOTAL_GAMES_WON) >= 1) {
            playerAchievements.addAchievement(Achievements.WIN_A_GAME, notifyUser);
        }
        if (!playerAchievements.hasUnlocked(Achievements.WIN_FIVE_GAMES) && this.statistics.get(StatisticType.TOTAL_GAMES_WON) >= 5) {
            playerAchievements.addAchievement(Achievements.WIN_FIVE_GAMES, notifyUser);
        }
    }

    public long getAmount(StatisticType type) {
        return this.statistics.get(type);
    }

    private void addValue(StatisticType type, long value) {
        this.statistics.put(type, this.statistics.get(type) + value);
    }

}
