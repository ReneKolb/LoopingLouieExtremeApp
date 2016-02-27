package de.renekolb.loopinglouieextreme.PlayerProfiles;

/**
 * Created by Admi on 27.02.2016.
 */
public class Achievement {

    private String title;
    private String description;
    private int drawableID;
    //UnlockConditions

    Achievement(String title, String description, int drawableID/*, UnlockCondition*/ ){
        this.title = title;
        this.description = description;
        this.drawableID = drawableID;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public int getDrawableID(){
        return this.drawableID;
    }

}
