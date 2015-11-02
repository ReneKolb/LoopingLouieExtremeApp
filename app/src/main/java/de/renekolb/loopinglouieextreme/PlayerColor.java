package de.renekolb.loopinglouieextreme;

public enum PlayerColor {

    RED(0), PURPLE(1), YELLOW(2), GREEN(3);

    private int playerIndex;

    PlayerColor(int playerIndex){
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex(){
        return this.playerIndex;
    }

    public static PlayerColor fromPlayerIndex(int playerIndex){
        for(PlayerColor pc:values()){
            if(pc.playerIndex == playerIndex){
                return pc;
            }
        }
        return null;
    }
}
