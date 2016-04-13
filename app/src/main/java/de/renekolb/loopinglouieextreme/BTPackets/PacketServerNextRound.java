package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerNextRound implements Packet {

    public PacketServerNextRound(){
        //Require empty constructor for instanciation
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_NEXT_ROUND;
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
            Log.e("PacketSrvNextRound","Error sending data",e);
        }
    }
}
