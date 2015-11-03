package de.renekolb.loopinglouieextreme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import de.renekolb.loopinglouieextreme.ui.Constants;

public class BTClientService {

    private FullscreenActivity activity;
    private Handler mHandler;

    private BluetoothAdapter mAdapter;

    private ConnectThread connectingThread;
    private BTConnectedThread connectedThread;

    public BTClientService(FullscreenActivity activity, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = handler;
        this.activity = activity;
    }

    public void connect(BluetoothDevice remoteDevice) {
        this.connectingThread = new ConnectThread(remoteDevice);
        this.connectingThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        this.connectedThread = new BTConnectedThread(socket, mHandler);
        this.connectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler
                .obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, socket.getRemoteDevice().getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        //Toast.makeText(activity, "connected to Server", Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(String msg) {
        this.connectedThread.write(msg.getBytes());
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

    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
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
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(BTServerService.commUuid);
            } catch (IOException e) {
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
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                //send FAIL msg
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    connectionFailed();
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
