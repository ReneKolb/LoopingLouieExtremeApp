package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerGoToWheel implements Packet {


    public PacketServerGoToWheel(){
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_GOTO_WHEEL;
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
            Log.e("PacketSrvGoToWheel", "Error sending data", e);
        }

    }
}
