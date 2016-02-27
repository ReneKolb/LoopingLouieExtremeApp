package de.renekolb.loopinglouieextreme.PlayerProfiles;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.R;

public class Achievements {

    private static final Achievement[] achievements = new Achievement[]{
            new Achievement("Play a round","Unlock this achievement by finishing a round", R.drawable.ic_settings_white),
            new Achievement("Play five rounds","Unlock this achievement by finishing five rounds", R.drawable.ic_settings_white),
            new Achievement("Play a game","Unlock this achievement by finishing a hole game", R.drawable.ic_settings_black),
            new Achievement("Play five game","Unlock this achievement by finishing five games", R.drawable.ic_settings_black),
            new Achievement("Win a round","Unlock this achievement by winning a round",R.drawable.ic_action_info),
            new Achievement("Win a game","Unlock this achievement by winning a hole game", R.drawable.ic_action_info_black)

    };
    public static final int PLAY_A_ROUND = 0;
    public static final int PLAY_FIVE_ROUNDS = 1;
    public static final int PLAY_A_GAME = 2;
    public static final int PLAY_FIVE_GAMES = 3;
    public static final int WIN_A_ROUND = 4;
    public static final int WIN_A_GAME = 5;


    public static Achievement getAchievement(int achievementID){
        return achievements[achievementID];
    }
}
