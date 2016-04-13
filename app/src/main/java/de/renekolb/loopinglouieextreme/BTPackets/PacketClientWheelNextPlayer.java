package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketClientWheelNextPlayer implements Packet {

    public PacketClientWheelNextPlayer(){
        //Require empty constructor for instanciation
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.CLIENT_WHEEL_NEXT_PLAYER;
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
            Log.e("PacketClWheelNextPlayer","Error sending data",e);
        }

    }
}
