package de.renekolb.loopinglouieextreme;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import de.renekolb.loopinglouieextreme.BTPackets.Packet;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerGameResults;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerGameSettings;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerGameStart;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerGoToWheel;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerNextRound;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerSpinWheel;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerUpdatePlayerSettings;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerUpdateWheelSpinner;
import de.renekolb.loopinglouieextreme.ui.Constants;
import de.renekolb.loopinglouieextreme.ui.GameFragment;
import de.renekolb.loopinglouieextreme.ui.GameResultFragment;
import de.renekolb.loopinglouieextreme.ui.MainMenuFragment;
import de.renekolb.loopinglouieextreme.ui.PlayerSettingsFragment;
import de.renekolb.loopinglouieextreme.ui.WheelOfFortuneFragment;

public class BTClientService implements BTService {

    //private final FullscreenActivity activity;
    //private final Handler mHandler;
    private FullscreenActivity fa;

    private boolean isConnecting;

    private final BluetoothAdapter mAdapter;

    private ConnectThread connectingThread;
    private BTConnectedThread connectedThread;

    public BTClientService(/*FullscreenActivity activity, Handler handler*/ FullscreenActivity fa) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        //this.mHandler = handler;
        this.fa = fa;
        //this.activity = activity;
        this.isConnecting = false;
    }

    public void connect(BluetoothDevice remoteDevice) {
        this.connectingThread = new ConnectThread(remoteDevice);
        this.connectingThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        this.connectedThread = new BTConnectedThread(socket, fa, this);
        this.connectedThread.start();


        Message msg = fa.ServiceMessageHandler
                .obtainMessage(Constants.messages.BT_DEVICE_CONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_DEVICE_NAME, socket.getRemoteDevice().getName());
        bundle.putString(Constants.KEY_DEVICE_ADDRESS, socket.getRemoteDevice().getAddress());
        msg.setData(bundle);
        fa.ServiceMessageHandler.sendMessage(msg);
    }

    /*public void sendMessage(String msg) {
        if (this.connectedThread != null && this.connectedThread.isAlive()) {
            this.connectedThread.write(msg.getBytes());
        }
    }*/
    public void sendPacket(Packet packet) {
        if (this.connectedThread != null && this.connectedThread.isAlive()) {
            Log.i("Client Service", "send Message");
            this.connectedThread.sendPacket(packet);
        } else {
            Log.w("Client Service", "Cannot send message");
        }
    }

    public void stop() {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (connectingThread != null) {
            connectingThread.cancel();
            connectingThread = null;
        }
    }

    public boolean isConnecting() {
        return this.isConnecting;
    }

    private void connectionFailed() {
        // Send a failure message back to the Activity
        fa.ServiceMessageHandler.sendEmptyMessage(Constants.messages.BT_CONNECTION_FAILED);
        isConnecting = false;
        /*Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/
    }

    @Override
    public void onReceivePacket(String senderAddress, Packet packet) {
        //FragmentTransaction ft;
        switch (packet.getPacketType()) {
            case SERVER_END_GAME:
                //final PacketServerEndGame packetEndGame = (PacketServerEndGame) packet;

                fa.getCurrentPlayer().updateTotalRoundsPlayed(1);
                fa.getCurrentPlayer().updateTotalGamesPlayed(1);

                if (fa.getGame().getGamePlayer(fa.getGame().first).getDisplayName().equals(fa.getCurrentPlayer().getPlayerName())) {
                    fa.getCurrentPlayer().updateTotalRoundsWon(1);
                }
                if (fa.getGame().getGameWinnerPlayer().getDisplayName().equals(fa.getCurrentPlayer().getPlayerName())) {
                    fa.getCurrentPlayer().updateTotalGamesWon(1);
                }
                fa.profileManager.saveProfile(fa.getCurrentPlayer().getProfileID());
                fa.getGame().setGameStarted(false);
                //btClient.stop();
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentUtils.disableAnimations = true;
                        fa.getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentUtils.disableAnimations = false;
                        FragmentTransaction ft = fa.getFragmentManager().beginTransaction();
                        if (fa.mainMenuFragment == null) {
                            fa.mainMenuFragment = MainMenuFragment.newInstance();
                        }
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        ft.replace(R.id.main_fragment, fa.mainMenuFragment);
                        ft.commit();
                    }
                });

                break;
            case SERVER_GAME_RESULTS:
                final PacketServerGameResults packetGameResults = (PacketServerGameResults) packet;

                fa.getGame().setRunning(false);

                final int first = fa.getGame().first = packetGameResults.getFirst();
                final int second = fa.getGame().second = packetGameResults.getSecond();
                final int third = fa.getGame().third = packetGameResults.getThird();
                final int fourth = fa.getGame().fourth = packetGameResults.getFourth();

                //int playerAmount = 2+(third == -1?0:1)+(fourth==-1?0:1);

                fa.getGame().getGamePlayer(first).addPoints(4);
                fa.getGame().getGamePlayer(second).addPoints(3);
                if (third != -1)
                    fa.getGame().getGamePlayer(third).addPoints(2);
                if (fourth != -1)
                    fa.getGame().getGamePlayer(first).addPoints(1);

                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = fa.getFragmentManager().beginTransaction();
                        if (fa.gameResultsFragment == null) {
                            fa.gameResultsFragment = GameResultFragment.newInstance(first, second, third, fourth);
                        } else {
                            fa.gameResultsFragment.setPlayers(first, second, third, fourth);
                        }
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, fa.gameResultsFragment); // DEFAULT VALUES
                        ft.commit();
                    }
                });
                break;
            case SERVER_GAME_SETTINGS:
                PacketServerGameSettings packetGameSettings = (PacketServerGameSettings) packet;

                fa.getGame().setMaxRounds(packetGameSettings.getMaxRounds());
                fa.getGame().setWheelOfFortuneEnabled(packetGameSettings.getEnableWheel());
                fa.getGame().setLoserWheelEnabled(packetGameSettings.getEnableLoserWheel());
                break;
            case SERVER_GAME_START:
                final PacketServerGameStart pacektGameStart = (PacketServerGameStart) packet;

                fa.getGame().nextRound();
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentUtils.disableAnimations = true;
                        fa.getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentUtils.disableAnimations = false;
                        FragmentTransaction ft = fa.getFragmentManager().beginTransaction();
                        if (fa.gameFragment == null) {
                            fa.gameFragment = GameFragment.newInstance();
                        }
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, fa.gameFragment);
                        ft.commit();
                    }
                });
                fa.getGame().setRunning(true);
                break;
            case SERVER_GOTO_WHEEL:
                final PacketServerGoToWheel packetGoToWheel = (PacketServerGoToWheel) packet;
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = fa.getFragmentManager().beginTransaction();
                        if (fa.wheelOfFortuneFragment == null) {
                            fa.wheelOfFortuneFragment = WheelOfFortuneFragment.newInstance(fa.wheelOfFortuneHandler);
                        }

                        //TODO: only temporary
                        //wheelOfFortuneFragment.setPlayerSpin(game.first, game.second, game.third, game.fourth);
                        fa.wheelOfFortuneHandler.setPlayers(fa.getGame().first, fa.getGame().getLoser(), -1, -1);
                        //wheelOfFortuneFragment.setPlayerSpin(game.first, game.getLoser(), -1, -1);

                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, fa.wheelOfFortuneFragment);
                        ft.commit();
                    }
                });
                break;
            case SERVER_NEXT_ROUND:
                final PacketServerNextRound packetNextRound = (PacketServerNextRound) packet;
                fa.getCurrentPlayer().updateTotalRoundsPlayed(1);

                if (fa.getGame().getGamePlayer(fa.getGame().first).getDisplayName().equals(fa.getCurrentPlayer().getPlayerName())) {
                    fa.getCurrentPlayer().updateTotalRoundsWon(1);
                }

                fa.profileManager.saveProfile(fa.getCurrentPlayer().getProfileID());

                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransaction ft = fa.getFragmentManager().beginTransaction();
                        if (fa.playerSettingsFragment == null) {
                            fa.playerSettingsFragment = PlayerSettingsFragment.newInstance();
                        }
                        fa.playerSettingsFragment.setPlayerNameEdible(true);
                        ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
                        //ft.addToBackStack(null);
                        ft.replace(R.id.main_fragment, fa.playerSettingsFragment);
                        ft.commit();
                    }
                });
                break;
            case SERVER_SPIN_WHEEL:
                final PacketServerSpinWheel packetSpinWheel = (PacketServerSpinWheel) packet;
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fa.wheelOfFortuneHandler.startSpinning(packetSpinWheel.getStartViewRotation(), packetSpinWheel.getRotateAnimation(), false);
                    }});
                break;
            case SERVER_UPDATE_PLAYER_SETTINGS:
                PacketServerUpdatePlayerSettings packetUpdatePlayerSettings = (PacketServerUpdatePlayerSettings) packet;

                ConnectionState state = ConnectionState.fromInt(packetUpdatePlayerSettings.getConnectionStateID());
                ItemType itemType = ItemType.fromInt(packetUpdatePlayerSettings.getItemTypeID());

                GamePlayer gp = fa.getGame().getGamePlayer(packetUpdatePlayerSettings.getSlot());
                if(gp == null){
                    return;
                }

                gp.setConnectionState(state); // first update connection State, because it overrides displayName with null!!
                gp.setGuestName(packetUpdatePlayerSettings.getDisplayName());
                gp.setDefaultItemType(itemType);
                gp.setCurrentChips(packetUpdatePlayerSettings.getChipsAmount());

                if (fa.playerSettingsFragment != null && fa.playerSettingsFragment.isVisible()) {
                    fa.playerSettingsFragment.updatePlayerSettings(packetUpdatePlayerSettings.getSlot());
                }
                break;
            case SERVER_UPDATE_WHEEL_SPINNER:
                final PacketServerUpdateWheelSpinner packetUpdateWheelSpinner = (PacketServerUpdateWheelSpinner) packet;

                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fa.wheelOfFortuneHandler.updateCurrentPlayer(packetUpdateWheelSpinner.getPosition());
                    }
                });
                break;
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(BTServerService.commUuid);
                isConnecting = true;
                Log.i("BT Client Service: ", "connecting...");
            } catch (IOException e) {
                isConnecting = false;
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.i("BT Client Service: ", "connected");
            } catch (IOException connectException) {
                isConnecting = false;
                // Unable to connect; close the socket and get out
                //send FAIL msg
                try {
                    Log.i("BT Client Service: ", "connecting failed");
                    mmSocket.close();
                    connectionFailed();
                } catch (IOException closeException) {
                    Log.e("BT Client Service", "error closing socket", closeException);
                }
                return;
            }
            isConnecting = false;
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            isConnecting = false;
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BT Client Service", "error closing socket", e);
            }
        }
    }
}
