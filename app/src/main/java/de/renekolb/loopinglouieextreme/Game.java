package de.renekolb.loopinglouieextreme;

import java.util.ArrayList;

public class Game {

    private int maxRounds;
    private int currentRound;
    private ArrayList<GamePlayer> gamePlayers;
    private CustomGameSettings settings;
    private boolean running;

    public Game(){
        this.maxRounds = 3;
        this.currentRound = 0;
        this.gamePlayers = new ArrayList<>(4);

        this.gamePlayers.add(new GamePlayer("Player 1",PlayerColor.RED));
        this.gamePlayers.add(new GamePlayer("Player 2",PlayerColor.PURPLE));
        this.gamePlayers.add(new GamePlayer("Player 3",PlayerColor.YELLOW));
        this.gamePlayers.add(new GamePlayer("Player 4",PlayerColor.GREEN));

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

    public void nextRound(){
        this.currentRound++;
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

    public void setRunning(boolean isRunning){
        this.running = isRunning;
    }

    public boolean isRunning(){
        return this.running;
    }

    /*
    public void setGamePlayer(int index, GamePlayer player){
        this.gamePlayers.set(index, player);
    }*/

    public CustomGameSettings getGameSettings(){
        return this.settings;
    }

    public String getDefaultItemsSendData(){
        StringBuilder sb = new StringBuilder();
        if(getGamePlayer(0).getDefaultItemType()!=null)
            sb.append(getGamePlayer(0).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if(getGamePlayer(1).getDefaultItemType()!=null)
            sb.append(getGamePlayer(1).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if(getGamePlayer(2).getDefaultItemType()!=null)
            sb.append(getGamePlayer(2).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if(getGamePlayer(3).getDefaultItemType()!=null)
            sb.append(getGamePlayer(3).getDefaultItemType().getItemID());
        else
            sb.append(0);

        return sb.toString();
    }

}
