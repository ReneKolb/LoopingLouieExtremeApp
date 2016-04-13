package de.renekolb.loopinglouieextreme;


import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.renekolb.loopinglouieextreme.BTPackets.Packet;
import de.renekolb.loopinglouieextreme.BTPackets.PacketHandler;
import de.renekolb.loopinglouieextreme.ui.Constants;


public class BTConnectedThread extends Thread {
    //private final Handler mMessageHandler;
    private FullscreenActivity fa;

    private final BluetoothSocket mmSocket;
    //private final InputStream mmInStream;
    private final DataInputStream dataInputStream;
    //private final OutputStream mmOutStream;
    private final DataOutputStream dataOutputStream;
    private String remoteAddress;

    PacketHandler packetHandler;

    public BTConnectedThread(BluetoothSocket socket, FullscreenActivity fa, BTService service) {
        this.fa = fa;
        packetHandler = new PacketHandler(fa, service);

        mmSocket = socket;
        //this.mMessageHandler = messageHandler;
        //InputStream tmpIn = null;
        DataInputStream tmpDataIn = null;
        //OutputStream tmpOut = null;
        DataOutputStream tmpDataOut = null;
        String tmpRemoteAddr = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            //tmpIn = socket.getInputStream();
            tmpDataIn = new DataInputStream(socket.getInputStream());
            //tmpOut = socket.getOutputStream();
            tmpDataOut = new DataOutputStream(socket.getOutputStream());
            tmpRemoteAddr = socket.getRemoteDevice().getAddress();
            Log.i("BT Connected: ", "opened comm Thread");
        } catch (IOException e) {
            Log.e("BT Connected", "error getting input/output streams", e);
        }

        //mmInStream = tmpIn;
        dataInputStream = tmpDataIn;
        //mmOutStream = tmpOut;
        dataOutputStream = tmpDataOut;
        remoteAddress = tmpRemoteAddr;
    }

    public BluetoothSocket getSocket() {
        return this.mmSocket;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                packetHandler.onDataIn(this.remoteAddress, dataInputStream);

/*                bytes = mmInStream.read(buffer); //TODO: replace with dataInStream
                // Send the obtained bytes to the UI activity
                Message msg = mMessageHandler.obtainMessage(Constants.messages.BT_READ);
                Bundle b = new Bundle();
                b.putString(Constants.KEY_DEVICE_ADDRESS, this.remoteAddress);
                b.putString(Constants.messages.KEY_BT_MESSAGE, new String(buffer, 0, bytes));
                msg.setData(b);
                msg.sendToTarget();*/
            } catch (IOException e) {
                connectionLost();
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
/*    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("BT Connected", "error sending data", e);
        }
    }*/

    public void sendPacket(Packet packet){
        packetHandler.sendPacket(dataOutputStream, packet);
    }


    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("BT Connected", "error closing socket", e);
        }
    }

    private void connectionLost() {
        Message msg = fa.ServiceMessageHandler.obtainMessage(Constants.messages.BT_CONNECTION_LOST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_DEVICE_ADDRESS, remoteAddress);
        msg.setData(bundle);
        fa.ServiceMessageHandler.sendMessage(msg);
        remoteAddress = null;
        /*
        Message msg = mMessageHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mMessageHandler.sendMessage(msg);
        */
    }
}

