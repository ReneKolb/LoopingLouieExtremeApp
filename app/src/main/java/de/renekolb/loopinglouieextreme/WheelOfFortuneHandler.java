package de.renekolb.loopinglouieextreme;

import android.util.Log;

import java.util.Random;

/**
 * Created by Admi on 10.03.2016.
 */
public class WheelOfFortuneHandler {

    private int[] playerIndex; //1st = winnerPlayer etc.
    private int currentPosition; //0..3: 0=firstPlayer etc.
    private boolean canSpin;
    private Random rnd;

    private WheelOfFortuneSettings wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;

    private float startViewRotation;
    private float animationPhi;

    private boolean isSpinning;

    private int playerAmount;

    private FullscreenActivity fa;

    private boolean debugMode;

    public WheelOfFortuneHandler(FullscreenActivity fa){
        this.fa = fa;
        this.playerIndex = new int[]{-1,-1,-1,-1};
        this.currentPosition = 0;
        this.rnd = new Random();
        this.isSpinning = false;
    }

    public void setPlayers(int firstPlayerIndex, int secondPlayerIndex, int thirdPlayerIndex, int fourthPlayerIndex){
        debugMode = firstPlayerIndex == -1;

        this.playerIndex[0] = firstPlayerIndex;
        this.playerIndex[1] = secondPlayerIndex;
        this.playerIndex[2] = thirdPlayerIndex;
        this.playerIndex[3] = fourthPlayerIndex;
        this.currentPosition = 0;

        playerAmount = 0;
        if(playerIndex[0] != -1) playerAmount++;
        if(playerIndex[1] != -1) playerAmount++;
        if(playerIndex[2] != -1) playerAmount++;
        if(playerIndex[3] != -1) playerAmount++;

        this.canSpin = getCanSpin(currentPosition);
        this.isSpinning = false;
        updateCurrentPlayer(0);
    }

    public void updateCurrentPlayer(int newPosition){
        if(debugMode){
            this.currentPosition = 0;
        }else {
            this.currentPosition = newPosition;
        }
        this.canSpin = getCanSpin(currentPosition);
        if(newPosition > 0) {
            this.wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
        }else{
            this.wofSettings = WheelOfFortuneSettings.WINNER_WHEEL;
        }

        if(debugMode){
            fa.getWheelOfFortuneFragment().setCurrentSpinner(null);
        }else {
            fa.getWheelOfFortuneFragment().setCurrentSpinner(fa.getGame().getGamePlayer(this.playerIndex[newPosition]));
        }
    }

    public void startSpinning(float startViewRotation, int direction){
        if(direction >= 0) direction = 1;
        else direction = -1;

        startSpinning(startViewRotation, direction * (1080f + rnd.nextFloat() * 1080f),true);
    }

    public void startSpinning(float startViewRotation, float rotateAnimation,boolean doSync){
        this.startViewRotation = startViewRotation;
        this.animationPhi = rotateAnimation;

        this.isSpinning = true;

        fa.getWheelOfFortuneFragment().startSpin(startViewRotation, rotateAnimation);
        if(doSync&&!debugMode) {
            if (fa.deviceRole == DeviceRole.CLIENT) {
                fa.sendWheelOfFortuneSpinToServer(startViewRotation,rotateAnimation);
            } else {
                fa.sendWheelOfFortuneSpinToClients(startViewRotation,rotateAnimation,null);
            }
        }
    }

    public void onSpinFinish(){
        this.isSpinning = false;

        float rot = (this.startViewRotation + this.animationPhi) % 360;
        if (rot < 0) rot += 360;
        int index = (int) ((360 - rot) / (360 / wofSettings.getFieldAmount()));

        int textRid = wofSettings.getDisplayTextResourceID(index);
        if (textRid == -1) {
            Log.e("Wheel of Fortune", "textRid = -1");
            fa.getWheelOfFortuneFragment().setResultText("Error...");
            canSpin = false;
            if(fa.deviceRole==DeviceRole.SERVER) {
                fa.getWheelOfFortuneFragment().setEnableNextButton(true);
            }
            return;
        }
        fa.getWheelOfFortuneFragment().setResultText(textRid);

        if(!wofSettings.getFieldType(index).equals(WheelFieldType.AGAIN)){
            canSpin = false;
            //next Player
            currentPosition++;
            if (currentPosition >= playerAmount && !debugMode) {
                if(fa.deviceRole==DeviceRole.SERVER) {
                    fa.getWheelOfFortuneFragment().setEnableNextButton(true);
                }
            }else{
               canSpin = getCanSpin(currentPosition);
                if(currentPosition>0){
                    wofSettings = WheelOfFortuneSettings.LOSER_WHEEL;
                }
                if(debugMode) {
                    fa.getWheelOfFortuneFragment().setCurrentSpinner(null);
                }else{
                    fa.getWheelOfFortuneFragment().setCurrentSpinner(fa.getGame().getGamePlayer(this.playerIndex[currentPosition]));
                }
                //fa.sendWheelOfFortuneSpinnner(currentPosition);
            }
        }else{
            canSpin = getCanSpin(currentPosition);
        }
    }

    public boolean canSpin(){
        return debugMode || this.canSpin;
    }

    private boolean getCanSpin(int currentPlayerPosition){
        if(debugMode)
            return true;

        if(fa.deviceRole == DeviceRole.CLIENT) {
            return fa.getGame().getGamePlayer(playerIndex[currentPlayerPosition]).getDisplayName().equals(fa.getCurrentPlayer().getPlayerName());
        }else if(fa.deviceRole == DeviceRole.SERVER){
            return fa.getGame().getGamePlayer(playerIndex[currentPlayerPosition]).getConnectionState().equals(ConnectionState.LOCAL);
        }else{
            //debug Mode
            return true;
        }
    }

    public WheelOfFortuneSettings getCurrentSettings(){
        return this.wofSettings;
    }

    public boolean isSpinning(){
        return this.isSpinning;
    }
}
