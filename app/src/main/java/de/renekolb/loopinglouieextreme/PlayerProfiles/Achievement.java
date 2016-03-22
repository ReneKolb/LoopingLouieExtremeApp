package de.renekolb.loopinglouieextreme.PlayerProfiles;

public class Achievement {

    private String title;
    private String unlockedDescription;
    private String unlockDescription;
    private int drawableID;
    //UnlockConditions
    private StatisticType statType;
    private long unlockAmount;

    Achievement(String title, String unlockedDescription, String unlockDescription, int drawableID/*, UnlockCondition*/, StatisticType statType, long unlockAmount) {
        this.title = title;
        this.unlockDescription = unlockDescription;
        this.unlockedDescription = unlockedDescription;
        this.drawableID = drawableID;
        this.statType = statType;
        this.unlockAmount = unlockAmount;
    }

    public String getTitle() {
        return this.title;
    }

    @Deprecated
    public String getUnlockDescription() {
        //replace with resource IDs
        return this.unlockDescription;
    }

    public String getUnlockedDescription() {
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
