package de.renekolb.loopinglouieextreme;

/**
 * Created by Admi on 05.11.2015.
 */
public class WheelOfFortuneField {

    private int displayTextRessourceID;
    private WheelFieldType fieldType;

    public WheelOfFortuneField(WheelFieldType type, int displayTextRessourceID){
        this.fieldType = type;
        this.displayTextRessourceID = displayTextRessourceID;
    }

    public int getDisplayTextRessourceID(){
        return this.displayTextRessourceID;
    }

    public WheelFieldType getFieldType(){
        return this.fieldType;
    }
}
