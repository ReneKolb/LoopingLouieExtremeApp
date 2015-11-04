package de.renekolb.loopinglouieextreme;

public enum ItemType {
    TURBO(0,"Turbo"), SLOW(1,"Slow"), REVERSE(2,"Reverse"), BLACKOUT(3,"Black");

    private String displayName;
    private int itemID;
    //private int iconID;

    ItemType(int itemID, String displayName){
        this.itemID = itemID;
        this.displayName = displayName;
    }

    public int getItemID(){
        return this.itemID;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    @Override
    public String toString(){
        return getDisplayName();
    }
}
