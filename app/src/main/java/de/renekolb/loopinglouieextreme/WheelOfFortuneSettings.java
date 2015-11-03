package de.renekolb.loopinglouieextreme;

import java.util.ArrayList;

public class WheelOfFortuneSettings {

    private int resourceID;
    private ArrayList<Integer> displayTexts;

    public WheelOfFortuneSettings(int resourceID, int... resourceDisplayTexts ){
        this.resourceID = resourceID;
        this.displayTexts = new ArrayList<>();
        if(resourceDisplayTexts != null) {
            for (int s : resourceDisplayTexts) {
                this.displayTexts.add(s);
            }
        }
    }

    public int getFieldAmount(){
        return this.displayTexts.size();
    }

    public int getDisplayTextResourceID(int index){
        return (displayTexts.isEmpty()||index>=displayTexts.size())?-1:displayTexts.get(index);
    }

    public int getResourceID(){
        return this.resourceID;
    }

    public static final WheelOfFortuneSettings WINNER_WHEEL = new WheelOfFortuneSettings(R.drawable.wheel_of_fortune_winner, R.string.wof_winner_1, R.string.wof_winner_2, R.string.wof_winner_3, R.string.wof_winner_4, R.string.wof_winner_5, R.string.wof_winner_6);
    public static final WheelOfFortuneSettings LOSER_WHEEL = new WheelOfFortuneSettings(R.drawable.wheel_of_fortune_loser, R.string.wof_loser_1, R.string.wof_loser_2, R.string.wof_loser_3, R.string.wof_loser_4, R.string.wof_loser_5, R.string.wof_loser_6);
}
