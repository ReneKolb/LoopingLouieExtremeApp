package de.renekolb.loopinglouieextreme;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private int maxRounds;
    private int currentRound;
    private final ArrayList<GamePlayer> gamePlayers;
    private final CustomGameSettings settings;
    private boolean running;

    private boolean enableWheelOfFortune;
    private boolean enableLoserWheel;

    private int secondsRunning;
    private final Timer gameTimer;

    private boolean gameStarted; //is true since the moment one press "connect" or "host a game"

    //TODO: only temporary!!!!
    public int first;
    public int second;
    public int third;
    public int fourth;

    private final FullscreenActivity fa; //to display the seconds

    private TimerTask timerTask;

    public Game(FullscreenActivity fa) {
        this.maxRounds = 3;
        this.currentRound = 0;
        this.gamePlayers = new ArrayList<>(4);

        this.gamePlayers.add(new GamePlayer(PlayerColor.RED, false));
        this.gamePlayers.add(new GamePlayer(PlayerColor.PURPLE, false));
        this.gamePlayers.add(new GamePlayer(PlayerColor.YELLOW, false));
        this.gamePlayers.add(new GamePlayer(PlayerColor.GREEN, false));

        this.settings = new CustomGameSettings();

        this.enableWheelOfFortune = true;
        this.enableLoserWheel = true;

        this.fa = fa;

        this.gameTimer = new Timer();

        this.gameStarted = false;
    }

    public boolean getWheelOfFortuneEnabled() {
        return this.enableWheelOfFortune;
    }

    public void setWheelOfFortuneEnabled(boolean enabled) {
        this.enableWheelOfFortune = enabled;
    }

    public boolean getLoserWheelEnabled() {
        return this.enableLoserWheel;
    }

    public void setLoserWheelEnabled(boolean enabled) {
        this.enableLoserWheel = enabled;
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

    public void setGameStarted(boolean started){
        this.gameStarted = started;
        if(!started){
            if (fa.btLEService != null) {
                fa.btLEService.stopScanning();
                fa.btLEService.disconnect();
            }

            if (fa.btServer != null) {
                fa.btServer.stop();
                fa.btServer.disconnectClients();
            }

            if (fa.btClient != null && !fa.btClient.isConnecting()) {
                fa.btClient.stop();
            }
        }
    }

    public boolean isGameStarted(){
        return this.gameStarted;
    }

    public void switchGamePlayers(int index1, int index2){
        //switches players but keeps the slot colors!
        GamePlayer player1 = gamePlayers.get(index1);
        GamePlayer player2 = gamePlayers.get(index2);

        PlayerColor tmp = player1.getPlayerColor();
        player1.setPlayerColor(player2.getPlayerColor());
        player2.setPlayerColor(tmp);

        gamePlayers.set(index1,player2);
        gamePlayers.set(index2,player1);
    }

    public int getGamePlayerIndex(String address) {
        for (int i = 0; i < gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getRemoteAddress() != null && gamePlayers.get(i).getRemoteAddress().equals(address)) {
                return i;
            }
        }
        return -1;
    }

    public GamePlayer getGamePlayer(String address) {
        int index = getGamePlayerIndex(address);

        return index == -1 ? null : gamePlayers.get(index);
    }

    public int getNextOpenSlot() {
        for (int i = 0; i < gamePlayers.size(); i++) {
            if (gamePlayers.get(i).getConnectionState().equals(ConnectionState.OPEN)) {
                return i;
            }
        }
        return -1;
    }

    public void bindRemotePlayer(int slot, String address) {
        if (slot < 0 || slot > 3) {
            Log.e("Game", "invalid Slot");
            return;
        }

        if (gamePlayers.get(slot).getConnectionState() != ConnectionState.OPEN) {
            Log.e("Game", "The Slot is not Open");
            return;
        }

        gamePlayers.get(slot).setConnectionState(address);
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
        if (getGamePlayer(0).getDefaultItemType() != null && getGamePlayer(0).getDefaultItemType().getItemID() != -1)
            sb.append(getGamePlayer(0).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(1).getDefaultItemType() != null && getGamePlayer(1).getDefaultItemType().getItemID() != -1)
            sb.append(getGamePlayer(1).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(2).getDefaultItemType() != null && getGamePlayer(2).getDefaultItemType().getItemID() != -1)
            sb.append(getGamePlayer(2).getDefaultItemType().getItemID());
        else
            sb.append(0);

        if (getGamePlayer(3).getDefaultItemType() != null && getGamePlayer(3).getDefaultItemType().getItemID() != -1)
            sb.append(getGamePlayer(3).getDefaultItemType().getItemID());
        else
            sb.append(0);

        return sb.toString();
    }

    public String getEnabledPlayersSendData() {
        String result = "";

        for (int i = 0; i < gamePlayers.size(); i++) {
            //getGamePlayer(i) ??
            if (this.gamePlayers.get(i).getConnectionState() == ConnectionState.CLOSED) {
                result += "0";
            } else {
                result += "1";
            }
        }

        return result;
    }

    public GamePlayer getGameWinnerPlayer() {
        int max = 0;
        GamePlayer best = null;
        for (GamePlayer gp : this.gamePlayers) {
            if (gp.getPoints() > max) {
                max = gp.getPoints();
                best = gp;
            }
        }
        return best;
    }

    public GamePlayer getGameLoserPlayer() {
        int max = Integer.MAX_VALUE;
        GamePlayer best = null;
        for (GamePlayer gp : this.gamePlayers) {
            if (gp.getPoints() < max) {
                max = gp.getPoints();
                best = gp;
            }
        }
        return best;
    }

    public int getLoser() {
        if (fourth != -1)
            return fourth;
        else if (third != -1)
            return third;
        else if (second != -1)
            return second;
        else
            return -1; //the first one is the winner and cannot be the loser!
    }

    public ArrayList<GamePlayer> getPlayers() {
        return this.gamePlayers;
    }

}
