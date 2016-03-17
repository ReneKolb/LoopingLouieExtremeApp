package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.ui.Constants;

public class PlayerAchievements {

    private ArrayList<Integer> achievements;

    private Handler mHandler;

    public PlayerAchievements(Handler mHandler) {
        this.achievements = new ArrayList<>();
        this.mHandler = mHandler;
    }


    public void addAchievement(int achievementID, boolean notifyUser) {
        if (!achievements.contains(achievementID)) {
            if (notifyUser) {
                Message msg = this.mHandler.obtainMessage(Constants.messages.UNLOCKED_ACHIEVEMENT);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.messages.KEY_ACHIEVEMENT_ID, achievementID);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            this.achievements.add(achievementID);
        }
    }

    public ArrayList<Integer> getAchievements() {
        return this.achievements;
    }

    public int getAmount() {
        return this.achievements.size();
    }

    public boolean hasUnlocked(int achievement) {
        return achievements.contains(achievement);
    }
}
