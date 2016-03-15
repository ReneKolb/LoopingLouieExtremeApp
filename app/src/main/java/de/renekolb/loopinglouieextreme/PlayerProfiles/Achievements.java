package de.renekolb.loopinglouieextreme.PlayerProfiles;

import de.renekolb.loopinglouieextreme.R;

public class Achievements {

    private static final Achievement[] achievements = new Achievement[]{
            new Achievement("And the addiction begins","You have finished a round","rounds finished", R.drawable.ic_settings_white, StatisticType.TOTAL_ROUNDS_PLAYED,1),
            new Achievement("Play five rounds","You have finished 5 rounds","rounds finished", R.drawable.ic_settings_white,StatisticType.TOTAL_ROUNDS_PLAYED,5),
            new Achievement("Play a game","You have finished a hole gane","games finished", R.drawable.ic_settings_black,StatisticType.TOTAL_GAMES_PLAYED,1),
            new Achievement("Play five game","You have finished 5 games","games finished", R.drawable.ic_settings_black,StatisticType.TOTAL_GAMES_PLAYED,5),
            new Achievement("Win a round","You have won a round","rounds won",R.drawable.ic_action_info,StatisticType.TOTAL_ROUNDS_WON,1),
            new Achievement("Win a game","You have won a hole game","games won", R.drawable.ic_action_info_black,StatisticType.TOTAL_GAMES_WON,1),

            //win 5 games
            //win 5 rounds
            //win a round without loosing a chip
            //Pechvogel: erster der ausscheidet
            //Mittelfeldspieler: in einem Spiel nicht am glücksrad drehen dürfen

            //spin a joker
            //spin a blank
            //spin 3times in a row (2x spin again)

    };
    public static final int PLAY_A_ROUND = 0;
    public static final int PLAY_FIVE_ROUNDS = 1;
    public static final int PLAY_A_GAME = 2;
    public static final int PLAY_FIVE_GAMES = 3;
    public static final int WIN_A_ROUND = 4;
    public static final int WIN_A_GAME = 5;

    private static final int[] achievementIDs = new int[] {PLAY_A_ROUND, PLAY_FIVE_ROUNDS, PLAY_A_GAME, PLAY_FIVE_GAMES, WIN_A_ROUND, WIN_A_GAME};


    public static Achievement getAchievement(int achievementID){
        return achievements[achievementID];
    }

    public static int[] getAchievementIDs(){
        return achievementIDs;
    }
}
