package de.renekolb.loopinglouieextreme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BTServerService {

    public static final String NAME = "Looping Louie BT";
    public static final UUID commUuid = UUID.fromString("883fd50d-9980-4035-9f2e-eea84e2d1a95");

    public static final UUID btAdapterCommUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    private Handler mHandler;

    private AcceptThread acceptThread;

    //private ArrayList<BTConnectedThread> clientCommThread;

    private HashMap<String, BTConnectedThread> clientCommThread;

    private BluetoothAdapter mAdapter;

    private FullscreenActivity activity;

    public BTServerService(FullscreenActivity activity, Handler handler) {
        this.activity = activity;
        this.mHandler = handler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        clientCommThread = new HashMap<String,BTConnectedThread>();
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
    private void ensureDiscoverable() {
        if (mAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.startActivity(discoverableIntent);
        }
    }

    public synchronized void start() {
        ensureDiscoverable(); // TODO: nur nötig für koppeln? nur sichtbar wenn User will bzw neue Spieler.
//wenn gerät schon gekoppelt, nur MAC-addresse nötig (die ist ja bekannt)

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public synchronized void stop() {
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        for(BTConnectedThread b : clientCommThread.values()){
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

    private int getConnectedDevices() {
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

    public void sendMessageToAll(String msg) {
        for(BTConnectedThread b : clientCommThread.values()){
            b.write(msg.getBytes());
        }
        /*for (int i = 0; i < 3; i++) {
            if (clientCommThread.get(i) != null) {
                clientCommThread.get(i).write(msg.getBytes());
            }
        }*/
    }

    public void sendMessage(String address, String msg){
        clientCommThread.get(address).write(msg.getBytes());
        /*if(clientCommThread.get(index) != null){
            clientCommThread.get(index).write(msg.getBytes());
        }*/
    }

    public void disconnectClient(String address) {
        clientCommThread.get(address).cancel(); //cancel if it is not cancelled yet
        clientCommThread.remove(address);

        //restart accept thread?
        if (getConnectedDevices() < 3 && !acceptThread.isAlive()) {
            acceptThread.start();
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

    public synchronized void manageConnectedSocket(BluetoothSocket socket) {

        // Start the thread to manage the connection and perform transmissions
        BTConnectedThread mConnectedThread = new BTConnectedThread(socket, mHandler);
        mConnectedThread.start();
        // Add each connected thread to an array
        if(getConnectedDevices()>=3)
        /*int index = getIndexForNewCon(socket.getRemoteDevice());
        if (index == -1) {*/
            //Error no free slot available
            return;
        //}
        clientCommThread.put(socket.getRemoteDevice().getAddress(),mConnectedThread);
        //clientCommThread.set(index, mConnectedThread);
        //Toast.makeText(activity, "new Client connected", Toast.LENGTH_SHORT).show();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler
                .obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, socket.getRemoteDevice().getName());
        bundle.putString(Constants.DEVICE_ADDRESS, socket.getRemoteDevice().getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
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
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
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
            }
        }
    }

    //TODO: Handle connection Lost!!!! Close Conn when Quit App

}
