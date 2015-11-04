package de.renekolb.loopinglouieextreme;

public enum ItemType {
    TURBO("Turbo"), SLOW("Slow"), REVERSE("Reverse"), BLACKOUT("Black");

    private String displayName;
    //private int iconID;

    ItemType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    @Override
    public String toString(){
        return getDisplayName();
    }
}
