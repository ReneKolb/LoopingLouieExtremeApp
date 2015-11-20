package de.renekolb.loopinglouieextreme;

/**
 * Created by Admi on 28.10.2015.
 */
public class CustomGameSettings {

    //7boolean = ~1Byte
    //3 byte   =  3Byte
    //3 short  =  6Byte
    //-----------------
    //           10Byte

    private boolean randomSpeed;
    private byte startSpeed;
    private short speedMinDelay;
    private short speedMaxDelay;
    private byte speedMinStepSize;
    private byte speedMaxStepSize;
    private boolean enableReverse;

    private boolean chefMode;
    private boolean chefRoulette;
    private short chefChangeDelay;
    private boolean chefHasShorterCooldown;

    private boolean enableItems;
    private boolean enableEvents;

    public CustomGameSettings() {
        loadDefaults();
    }

    public void loadDefaults() {
        this.randomSpeed = true;
        this.startSpeed = 64;
        this.speedMinDelay = 5000;
        this.speedMaxDelay = 12000;
        this.speedMinStepSize = 5;
        this.speedMaxStepSize = 15;
        this.enableReverse = true;

        //Chef Mode
        this.chefMode = false;
        this.chefRoulette = true;
        this.chefChangeDelay = 7000;
        this.chefHasShorterCooldown = true;

        this.enableItems = true;
        this.enableEvents = true;
    }

    public void loadClassic() {
        this.startSpeed = 64;
        this.randomSpeed = false;

        this.chefMode = false;

        this.enableItems = false;
        this.enableEvents = false;
    }


    public void loadAction() {
        this.randomSpeed = true;
        this.startSpeed = 64;
        this.speedMinDelay = 5000;
        this.speedMaxDelay = 12000;
        this.speedMinStepSize = 5;
        this.speedMaxStepSize = 15;
        this.enableReverse = true;

        //Chef Mode
        this.chefMode = false;
        this.chefRoulette = true;
        this.chefChangeDelay = 7000;
        this.chefHasShorterCooldown = true;

        this.enableItems = true;
        this.enableEvents = true;
    }

    public void setRandomSpeed(boolean randomSpeed) {
        this.randomSpeed = randomSpeed;
    }

    public boolean getRandomSpeed() {
        return this.randomSpeed;
    }

    public void setStartSpeed(byte startSpeed) {
        this.startSpeed = startSpeed;
    }

    public byte getStartSpeed() {
        return this.startSpeed;
    }

    public void setSpeedMinDelay(short speedMinDelay) {
        this.speedMinDelay = speedMinDelay;
    }

    public short getSpeedMinDelay() {
        return this.speedMinDelay;
    }

    public void setSpeedMaxDelay(short speedMaxDelay) {
        this.speedMaxDelay = speedMaxDelay;
    }

    public short getSpeedMaxDelay() {
        return this.speedMaxDelay;
    }

    public void setSpeedMinStepSize(byte speedMinStepSize) {
        this.speedMinStepSize = speedMinStepSize;
    }

    public byte getSpeedMinStepSize() {
        return this.speedMinStepSize;
    }

    public void setSpeedMaxStepSize(byte speedMaxStepSize) {
        this.speedMaxStepSize = speedMaxStepSize;
    }

    public byte getSpeedMaxStepSize() {
        return this.speedMaxStepSize;
    }

    public void setEnableReverse(boolean enableReverse) {
        this.enableReverse = enableReverse;
    }

    public boolean getEnableReverse() {
        return this.enableReverse;
    }

    public void setChefMode(boolean chefMode) {
        this.chefMode = chefMode;
    }

    public boolean getChefMode() {
        return this.chefMode;
    }

    public void setChefRoulette(boolean chefRoulette) {
        this.chefRoulette = chefRoulette;
    }

    public boolean getChefRoulette() {
        return this.chefRoulette;
    }

    public void setChefChangeDelay(short chefChangeDelay) {
        this.chefChangeDelay = chefChangeDelay;
    }

    public short getChefChangeDelay() {
        return this.chefChangeDelay;
    }

    public void setChefHasShorterCooldown(boolean chefHasShorterCooldown) {
        this.chefHasShorterCooldown = chefHasShorterCooldown;
    }

    public boolean getChefHasShorterCooldown() {
        return this.chefHasShorterCooldown;
    }

    public void setEnableItems(boolean enableItems) {
        this.enableItems = enableItems;
    }

    public boolean getEnableItems() {
        return this.enableItems;
    }

    public void setEnableEvents(boolean enableEvents) {
        this.enableEvents = enableEvents;
    }

    public boolean getEnableEvents() {
        return this.enableEvents;
    }


    public byte[] getSendArray() {
        byte[] result = new byte[20];

        result[0] = 'z'; //packet type

        //kodiere 7 booleans in 1 Byte
        byte bools = 0;
        if (randomSpeed) bools |= 1 << 7;
        if (enableReverse) bools |= 1 << 6;
        if (chefMode) bools |= 1 << 5;
        if (chefRoulette) bools |= 1 << 4;
        if (chefHasShorterCooldown) bools |= 1 << 3;
        if (enableItems) bools |= 1 << 2;
        if (enableEvents) bools |= 1 << 1;
        result[1] = bools;

        result[2] = startSpeed;

        result[3] = (byte) (speedMinDelay & 0x00FF);
        result[4] = (byte) ((speedMinDelay & 0xFF00) >> 8);

        result[5] = (byte) (speedMaxDelay & 0x00FF);
        result[6] = (byte) ((speedMaxDelay & 0xFF00) >> 8);

        result[7] = speedMinStepSize;

        result[8] = speedMaxStepSize;

        result[9] = (byte) (chefChangeDelay & 0x00FF);
        result[10] = (byte) ((chefChangeDelay & 0xFF00) >> 8);

        return result;
    }


}
