package de.renekolb.loopinglouieextreme.PlayerProfiles;

import de.renekolb.loopinglouieextreme.R;

public class Achievements {

    private static final Achievement[] achievements = new Achievement[]{
            new Achievement(R.string.ach_finish_1_round_title, R.string.ach_finish_1_round_descr_unlocked, R.string.ach_finish_1_round_descr_unlock, R.drawable.ic_settings_white, StatisticType.TOTAL_ROUNDS_PLAYED, 1),
            new Achievement(R.string.ach_finish_5_rounds_title, R.string.ach_finish_5_rounds_descr_unlocked, R.string.ach_finish_5_rounds_descr_unlock, R.drawable.ic_settings_white, StatisticType.TOTAL_ROUNDS_PLAYED, 5),
            new Achievement(R.string.ach_finish_1_game_title, R.string.ach_finish_1_game_descr_unlocked, R.string.ach_finish_1_game_desr_unlock, R.drawable.ic_settings_black, StatisticType.TOTAL_GAMES_PLAYED, 1),
            new Achievement(R.string.ach_finish_5_games_title, R.string.ach_finish_5_games_descr_unlocked, R.string.ach_finish_5_games_descr_unlock, R.drawable.ic_settings_black, StatisticType.TOTAL_GAMES_PLAYED, 5),
            new Achievement(R.string.ach_win_1_round_title, R.string.ach_win_1_round_descr_unlocked, R.string.ach_win_1_round_descr_unlock, R.drawable.ic_action_info, StatisticType.TOTAL_ROUNDS_WON, 1),
            new Achievement(R.string.ach_win_5_rounds_title, R.string.ach_win_5_rounds_descr_unlocked, R.string.ach_win_5_rounds_descr_unlock, R.drawable.ic_action_info, StatisticType.TOTAL_ROUNDS_WON, 5),
            new Achievement(R.string.ach_win_1_game_title, R.string.ach_win_1_game_descr_unlocked, R.string.ach_win_1_game_descr_unlock, R.drawable.ic_action_info_black, StatisticType.TOTAL_GAMES_WON, 1),
            new Achievement(R.string.ach_win_5_games_title, R.string.ach_win_5_games_descr_unlocked, R.string.ach_win_5_games_descr_unlock, R.drawable.ic_action_info_black, StatisticType.TOTAL_GAMES_WON, 5),

            //win a round without loosing a chip
            //Pechvogel: erster der ausscheidet
            //Mittelfeldspieler: in einem Spiel nicht erster oder letzter geworden

            //spin a joker
            //spin a blank
            //spin 3times in a row (2x spin again)

    };
    public static final int PLAY_A_ROUND = 0;
    public static final int PLAY_FIVE_ROUNDS = 1;
    public static final int PLAY_A_GAME = 2;
    public static final int PLAY_FIVE_GAMES = 3;
    public static final int WIN_A_ROUND = 4;
    public static final int WIN_FIVE_ROUNDS = 5;
    public static final int WIN_A_GAME = 6;
    public static final int WIN_FIVE_GAMES = 7;

    private static final int[] achievementIDs = new int[]{PLAY_A_ROUND, PLAY_FIVE_ROUNDS, PLAY_A_GAME, PLAY_FIVE_GAMES, WIN_A_ROUND, WIN_FIVE_ROUNDS, WIN_A_GAME, WIN_FIVE_GAMES};


    public static Achievement getAchievement(int achievementID) {
        return achievements[achievementID];
    }

    public static int[] getAchievementIDs() {
        return achievementIDs;
    }
}
