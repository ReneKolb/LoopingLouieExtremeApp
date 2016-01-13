package de.renekolb.loopinglouieextreme;

import android.graphics.Color;

public enum PlayerColor {

    RED(0, Color.argb(255, 255, 0, 0)), PURPLE(1, Color.argb(255, 136, 0, 136)), YELLOW(2, Color.argb(255, 221, 221, 0)), GREEN(3, Color.argb(255, 0, 255, 0));

    private final int playerIndex;
    private final int color;

    PlayerColor(int playerIndex, int color) {
        this.playerIndex = playerIndex;
        this.color = color;
    }

    public int getPlayerIndex() {
        return this.playerIndex;
    }

    public int getColor() {
        return this.color;
    }

    public static PlayerColor fromPlayerIndex(int playerIndex) {
        for (PlayerColor pc : values()) {
            if (pc.playerIndex == playerIndex) {
                return pc;
            }
        }
        return null;
    }
}
