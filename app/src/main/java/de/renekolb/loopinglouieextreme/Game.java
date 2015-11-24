package de.renekolb.loopinglouieextreme;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private int maxRounds;
    private int currentRound;
    private ArrayList<GamePlayer> gamePlayers;
    private CustomGameSettings settings;
    private boolean running;

    private int secondsRunning;
    private Timer gameTimer;


    //TODO: only temporary!!!!
    public int first;
    public int second;
    public int third;
    public int fourth;

    private FullscreenActivity fa; //to display the seconds

    private TimerTask timerTask;

    public Game(FullscreenActivity fa) {
        this.maxRounds = 3;
        this.currentRound = 0;
        this.gamePlayers = new ArrayList<>(4);

        this.gamePlayers.add(new GamePlayer("Player 1", PlayerColor.RED, true));
        this.gamePlayers.add(new GamePlayer("Player 2", PlayerColor.PURPLE, false));
        this.gamePlayers.add(new GamePlayer("Player 3", PlayerColor.YELLOW, false));
        this.gamePlayers.add(new GamePlayer("Player 4", PlayerColor.GREEN, false));

        this.settings = new CustomGameSettings();

        this.fa = fa;

        this.gameTimer = new Timer();
    }

    public int getMaxRounds() {
        return this.maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public void nextRound() {
        this.currentRound++;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public GamePlayer getGamePlayer(int index) {
        if (index < 0 || index > 3) {
            return null;
            //throw new IndexOutOfBoundsException();
        }

        return this.gamePlayers.get(index);
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
        if (isRunning) {
            this.secondsRunning = 0;
            if (timerTask != null) {
                timerTask.cancel();
            }
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    secondsRunning++;
                   fa.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fa.getGameFragment().updateSeconds(secondsRunning);
                        }
                    });
                }
            };

            this.gameTimer.scheduleAtFixedRate(timerTask, 0, 1000);
        } else {
            fa.getGameFragment().updateSeconds(secondsRunning);
            this.timerTask.cancel();
            timerTask = null;
            //this.gameTimer.cancel();
        }
    }

    public long getSecondsRunning() {
        return this.secondsRunning;
    }

    public boolean isRunning() {
        return this.running;
    }

    /*
    public void setGamePlayer(int index, GamePlayer player){
        this.gamePlayers.set(index, player);
    }*/

    public CustomGameSettings getGameSettings() {
        return this.settings;
    }

    public String getDefaultItemsSendData() {
        StringBuilder sb = new StringBuilder();
        if (getGamePlayer(0).getDefaultItemType() != null)
            sb.append(getGamePlayer(0).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(1).getDefaultItemType() != null)
            sb.append(getGamePlayer(1).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(2).getDefaultItemType() != null)
            sb.append(getGamePlayer(2).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(3).getDefaultItemType() != null)
            sb.append(getGamePlayer(3).getDefaultItemType().getItemID());
        else
            sb.append(0);

        return sb.toString();
    }

    public int getLoser(){
        if(fourth != -1)
            return fourth;
        else if(third != -1)
            return third;
        else if(second!=-1)
            return second;
        else
            return -1; //the first one is the winner and cannot be the loser!
    }

}
