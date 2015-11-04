package de.renekolb.loopinglouieextreme;

import java.util.ArrayList;

public class Game {

    private int maxRounds;
    private int currentRound;
    private ArrayList<GamePlayer> gamePlayers ;
    private CustomGameSettings settings;

    public Game(){
        this.maxRounds = 1;
        this.currentRound = 0;
        this.gamePlayers = new ArrayList<>(4);
        this.settings = new CustomGameSettings();
    }

    public int getMaxRounds(){
        return this.maxRounds;
    }

    public void setMaxRounds(int maxRounds){
       this.maxRounds = maxRounds;
    }

    public int getCurrentRound(){
        return this.currentRound;
    }

    public void setCurrentRound(int currentRound){
        this.currentRound = currentRound;
    }

    public GamePlayer getGamePlayer(int index){
        if(index<0||index>3){
            return null;
            //throw new IndexOutOfBoundsException();
        }

        return this.gamePlayers.get(index);
    }

    public void addGamePlayer(GamePlayer player){
        this.gamePlayers.add(player);
    }

    public CustomGameSettings getGameSettings(){
        return this.settings;
    }

}
