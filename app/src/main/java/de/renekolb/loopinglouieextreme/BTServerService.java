package de.renekolb.loopinglouieextreme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.renekolb.loopinglouieextreme.BTPackets.Packet;
import de.renekolb.loopinglouieextreme.BTPackets.PacketClientChangeItem;
import de.renekolb.loopinglouieextreme.BTPackets.PacketClientPlayerName;
import de.renekolb.loopinglouieextreme.BTPackets.PacketClientSpinWheel;
import de.renekolb.loopinglouieextreme.BTPackets.PacketClientWheelNextPlayer;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerSpinWheel;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerUpdatePlayerSettings;
import de.renekolb.loopinglouieextreme.BTPackets.PacketServerUpdateWheelSpinner;
import de.renekolb.loopinglouieextreme.CustomViews.ConnectedPlayerListItem;
import de.renekolb.loopinglouieextreme.ui.Constants;

public class BTServerService implements BTService {

    private static final String NAME = "Looping Louie BT";
    public static final UUID commUuid = UUID.fromString("883fd50d-9980-4035-9f2e-eea84e2d1a95");

    public static final UUID btAdapterCommUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    private final Handler mHandler;

    private AcceptThread acceptThread;

    private final HashMap<String, BTConnectedThread> clientCommThread;

    private final BluetoothAdapter mAdapter;

    private final FullscreenActivity fa;

    public BTServerService(FullscreenActivity fa, Handler handler) {
        this.fa = fa;
        this.mHandler = handler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        clientCommThread = new HashMap<>();
        //clientCommThread = new ArrayList<BTConnectedThread>(3);
        //for (int i = 0; i < 3; i++) {
//            clientCommThread.add(null);
//        }
    }

    //TODO: -Cleanup Array when connection is lost for make room for a new Connection!!
    //TODO: -Display connected Clients

    /**
     * Makes this device discoverable.
     */
    public void ensureDiscoverable() {
        if (mAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            fa.startActivity(discoverableIntent);
        }
    }

    public synchronized void start() {

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
        }
        if (!acceptThread.isAlive()) {
            acceptThread.start();
        }
    }

    public synchronized void stop() {
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        for (BTConnectedThread b : clientCommThread.values()) {
            b.cancel();
        }
        clientCommThread.clear();
        /*for (int i = 0; i < clientCommThread.size(); i++) {
            if (clientCommThread.get(i) != null) {
                clientCommThread.get(i).cancel();
                clientCommThread.set(i, null);
            }
        }*/
    }

    public int getConnectedDevices() {
        return clientCommThread.size();
        /*int cnt = 0;
        for (int i = 0; i < 3; i++) {
            if (clientCommThread.get(i) != null) {
                cnt++;
            }
        }
        return cnt;*/
    }
/*
    private int getIndex(BluetoothDevice dev) {
        for (int i = 0; i < 3; i++) {
            if (clientCommThread.get(i) != null && clientCommThread.get(i).getSocket().getRemoteDevice().getAddress().equalsIgnoreCase(dev.getAddress())) {
                return i;
            }
        }
        return -1;
    }*/

    public void sendMessageToAllBut(Packet packet, String exceptAddress) {
        for (BTConnectedThread b : clientCommThread.values()) {
            if (exceptAddress == null || !b.getSocket().getRemoteDevice().getAddress().equals(exceptAddress)) {
                //b.write(msg.getBytes());
                b.sendPacket(packet);
            }
        }
    }

    public void sendMessageToAll(Packet packet) {
        for (BTConnectedThread b : clientCommThread.values()) {
            //b.write(msg.getBytes());
            b.sendPacket(packet);
        }
        /*for (int i = 0; i < 3; i++) {
            if (clientCommThread.get(i) != null) {
                clientCommThread.get(i).write(msg.getBytes());
            }
        }*/
    }

    public void sendMessage(String address, Packet packet) {
        //clientCommThread.get(address).write(msg.getBytes());
        clientCommThread.get(address).sendPacket(packet);
        /*if(clientCommThread.get(index) != null){
            clientCommThread.get(index).write(msg.getBytes());
        }*/
    }

    public void disconnectClient(String address, boolean restartListening) {
        if (clientCommThread.get(address) != null) {
            clientCommThread.get(address).cancel(); //cancel if it is not cancelled yet
            clientCommThread.remove(address);

            //restart accept thread?
            if (restartListening && getConnectedDevices() < 3 && !acceptThread.isAlive()) {
                acceptThread.start();
            }
        }
    }

    public ArrayList<ConnectedPlayerListItem> getConnectedPlayers() {
        ArrayList<ConnectedPlayerListItem> result = new ArrayList<>();
        for (BTConnectedThread b : this.clientCommThread.values()) {
            result.add(new ConnectedPlayerListItem(b.getSocket().getRemoteDevice().getAddress(), b.getSocket().getRemoteDevice().getName()));
        }
        return result;
    }


    public void disconnectClients() {
        for (BTConnectedThread t : clientCommThread.values()) {
            t.cancel();
        }
        clientCommThread.clear();

        if (acceptThread != null && acceptThread.isAlive()) {
            acceptThread.cancel();
        }
    }

    /*private int getIndexForNewCon(BluetoothDevice dev) {
        if (getIndex(dev) == -1) {
            for (int i = 0; i < 3; i++) {
                if (clientCommThread.get(i) == null)
                    return i;
            }
        }
        return -1;
    }*/


    private synchronized void manageConnectedSocket(BluetoothSocket socket) {

        // Start the thread to manage the connection and perform transmissions
        BTConnectedThread mConnectedThread = new BTConnectedThread(socket, fa, this);
        mConnectedThread.start();
        // Add each connected thread to an array
        if (getConnectedDevices() >= 3)
        /*int index = getIndexForNewCon(socket.getRemoteDevice());
        if (index == -1) {*/
            //Error no free slot available
            return;
        //}
        clientCommThread.put(socket.getRemoteDevice().getAddress(), mConnectedThread);
        //clientCommThread.set(index, mConnectedThread);
        //Toast.makeText(activity, "new Client connected", Toast.LENGTH_SHORT).show();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler
                .obtainMessage(Constants.messages.BT_DEVICE_CONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_DEVICE_NAME, socket.getRemoteDevice().getName());
        bundle.putString(Constants.KEY_DEVICE_ADDRESS, socket.getRemoteDevice().getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Override
    public void onReceivePacket(String senderAddress, Packet packet) {
        Log.i("Server Service", "Receive Packet: " + packet.getPacketType());
        switch (packet.getPacketType()) {
            case CLIENT_CHANGE_ITEM:
                PacketClientChangeItem packetChangeItem = (PacketClientChangeItem) packet;


                final int slot = fa.getGame().getGamePlayerIndex(senderAddress);
                GamePlayer player = fa.getGame().getGamePlayer(slot);
                ItemType it = ItemType.fromInt(packetChangeItem.getItemID());
                player.setDefaultItemType(it);
                if (fa.playerSettingsFragment != null && fa.playerSettingsFragment.isVisible()) {
                    fa.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fa.playerSettingsFragment.updatePlayerSettings(slot);
                        }
                    });
                }
                sendMessageToAll(new PacketServerUpdatePlayerSettings(slot, player));
                //sendPlayerSettingsUpdate(slot); //send the change to all clients

                break;
            case CLIENT_PLAYER_NAME:
                PacketClientPlayerName packetPlayerName = (PacketClientPlayerName) packet;

                int slot2 = fa.getGame().getGamePlayerIndex(senderAddress);
                if (slot2 == -1) {
                    slot2 = fa.bindNewPlayer(senderAddress, "Test");
                }
                player = fa.getGame().getGamePlayer(slot2);
                player.setGuestName(packetPlayerName.getPlayerName());
                if (fa.playerSettingsFragment != null && fa.playerSettingsFragment.isVisible()) {
                    final int slot2t = slot2;
                    fa.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fa.playerSettingsFragment.updatePlayerSettings(slot2t);
                        }
                    });
                }
                sendMessageToAll(new PacketServerUpdatePlayerSettings(slot2, fa.getGame().getGamePlayer(slot2)));
                break;
            case CLIENT_SPIN_WHEEL:
                final PacketClientSpinWheel packetSpinWheel = (PacketClientSpinWheel) packet;
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fa.wheelOfFortuneHandler.startSpinning(packetSpinWheel.getViewStartRotation(), packetSpinWheel.getAnimationRot(), false);
                    }
                });
                sendMessageToAllBut(new PacketServerSpinWheel(packetSpinWheel.getViewStartRotation(), packetSpinWheel.getAnimationRot()), senderAddress);
                //sendWheelOfFortuneSpinToClients(viewRot, animRot, senderAddress);
                break;
            case CLIENT_WHEEL_NEXT_PLAYER:
                PacketClientWheelNextPlayer packetWheelNextPlayer = (PacketClientWheelNextPlayer) packet;
                fa.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fa.wheelOfFortuneHandler.updateCurrentPlayer(fa.wheelOfFortuneHandler.getCurrentPosition());
                    }
                });
                sendMessageToAll(new PacketServerUpdateWheelSpinner(fa.wheelOfFortuneHandler.getCurrentPosition()));

                //wheelOfFortuneHandler.updateCurrentPlayer(wheelOfFortuneHandler.getCurrentPosition());
                //sendWheelOfFortuneSpinnerToClients(wheelOfFortuneHandler.getCurrentPosition());
                break;
        }
    }


    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, commUuid);
                Log.i("BT Server Service: ", "listening...");
            } catch (IOException e) {
                Log.e("BT Server Service", "error when trying to listen", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                    Log.i("BT Server Service: ", "accept connection");
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    //mmServerSocket.close();
                    //break;
                }

                if (getConnectedDevices() >= 3) {
                    cancel();
                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("BT Server Service", "error closing socket", e);
            }
        }
    }


}
