package de.renekolb.loopinglouieextreme;

public class WheelOfFortuneField {

    private final int displayTextRessourceID;
    private final WheelFieldType fieldType;

    public WheelOfFortuneField(WheelFieldType type, int displayTextRessourceID) {
        this.fieldType = type;
        this.displayTextRessourceID = displayTextRessourceID;
    }

    public int getDisplayTextRessourceID() {
        return this.displayTextRessourceID;
    }

    public WheelFieldType getFieldType() {
        return this.fieldType;
    }
}
