package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerGameStart implements Packet {

    public PacketServerGameStart(){
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_GAME_START;
    }


    @Override
    public void readPacket(DataInputStream inStream) {
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());
        }catch (IOException e){
            Log.e("PacketSrvGameStart","Error sending data",e);
        }

    }
}
