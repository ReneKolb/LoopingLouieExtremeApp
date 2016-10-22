package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Achievement {

    private int title;
    private int unlockedDescription;
    private int unlockDescription;
    private int drawableID;
    //UnlockConditions
    private StatisticType statType;
    private long unlockAmount;

    Achievement(@StringRes int title, @StringRes int unlockedDescription, @StringRes int unlockDescription, @DrawableRes int drawableID/*, UnlockCondition*/, StatisticType statType, long unlockAmount) {
        this.title = title;
        this.unlockDescription = unlockDescription;
        this.unlockedDescription = unlockedDescription;
        this.drawableID = drawableID;
        this.statType = statType;
        this.unlockAmount = unlockAmount;
    }

    public int getTitle() {
        return this.title;
    }

    public int getUnlockDescription() {
        return this.unlockDescription;
    }

    public int getUnlockedDescription() {
        return this.unlockedDescription;
    }

    public StatisticType getStatType() {
        return this.statType;
    }

    public long getUnlockAmount() {
        return this.unlockAmount;
    }

    public int getDrawableID() {
        return this.drawableID;
    }

}
