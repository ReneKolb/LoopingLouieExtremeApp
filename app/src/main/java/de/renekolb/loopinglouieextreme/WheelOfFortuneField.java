package de.renekolb.loopinglouieextreme;

public class WheelOfFortuneField {

    private final int displayTextResourceID;
    private final WheelFieldType fieldType;

    public WheelOfFortuneField(WheelFieldType type, int displayTextResourceID) {
        this.fieldType = type;
        this.displayTextResourceID = displayTextResourceID;
    }

    public int getDisplayTextResourceID() {
        return this.displayTextResourceID;
    }

    public WheelFieldType getFieldType() {
        return this.fieldType;
    }
}
