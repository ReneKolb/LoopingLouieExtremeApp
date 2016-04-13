package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerEndGame implements Packet {


    public PacketServerEndGame(){
        //Require empty constructor for instanciation
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_END_GAME;
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
            Log.e("PacketSrvEndGame", "Error sending data", e);
        }
    }
}
