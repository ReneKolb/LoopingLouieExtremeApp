package de.renekolb.loopinglouieextreme;

import java.util.ArrayList;
import java.util.Collections;

public class WheelOfFortuneSettings {

    private final int resourceID;
    private final ArrayList<WheelOfFortuneField> displayTexts;

    private WheelOfFortuneSettings(int resourceID, WheelOfFortuneField... wheelFields) {
        this.resourceID = resourceID;
        this.displayTexts = new ArrayList<>();
        if (wheelFields != null) {
            Collections.addAll(this.displayTexts, wheelFields); //better performance
            /*for (WheelOfFortuneField s : wheelFields) {
                this.displayTexts.add(s);
            }*/
        }
    }

    public int getFieldAmount() {
        return this.displayTexts.size();
    }

    public int getDisplayTextResourceID(int index) {
        return (displayTexts.isEmpty() || index >= displayTexts.size()) ? -1 : displayTexts.get(index).getDisplayTextRessourceID();
    }

    public WheelFieldType getFieldType(int index) {
        return (displayTexts.isEmpty() || index >= displayTexts.size()) ? null : displayTexts.get(index).getFieldType();
    }

    public int getResourceID() {
        return this.resourceID;
    }

    /*public static final WheelOfFortuneSettings WINNER_WHEEL = new WheelOfFortuneSettings(R.drawable.wheel_of_fortune_winner,
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_1),
            new WheelOfFortuneField(WheelFieldType.JOKER, R.string.wof_winner_2),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_3),
            new WheelOfFortuneField(WheelFieldType.AGAIN, R.string.wof_winner_4),
            new WheelOfFortuneField(WheelFieldType.BLANK, R.string.wof_winner_5),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_6));*/

    public static final WheelOfFortuneSettings WINNER_WHEEL = new WheelOfFortuneSettings(R.drawable.wheel_of_fortune_winner_wheel,
            new WheelOfFortuneField(WheelFieldType.AGAIN, R.string.wof_winner_1),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_2),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_3),
            new WheelOfFortuneField(WheelFieldType.BLANK, R.string.wof_winner_4),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_5),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_6),
            new WheelOfFortuneField(WheelFieldType.AGAIN, R.string.wof_winner_7),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_8),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_9),
            new WheelOfFortuneField(WheelFieldType.JOKER, R.string.wof_winner_10),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_11),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_winner_12));

    public static final WheelOfFortuneSettings LOSER_WHEEL = new WheelOfFortuneSettings(R.drawable.wheel_of_fortune_loser_wheel,
            new WheelOfFortuneField(WheelFieldType.AGAIN, R.string.wof_loser_1),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_2),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_3),
            new WheelOfFortuneField(WheelFieldType.BLANK, R.string.wof_loser_4),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_5),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_6),
            new WheelOfFortuneField(WheelFieldType.AGAIN, R.string.wof_loser_7),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_8),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_9),
            new WheelOfFortuneField(WheelFieldType.JOKER, R.string.wof_loser_10),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_11),
            new WheelOfFortuneField(WheelFieldType.NORMAL, R.string.wof_loser_12));
}
