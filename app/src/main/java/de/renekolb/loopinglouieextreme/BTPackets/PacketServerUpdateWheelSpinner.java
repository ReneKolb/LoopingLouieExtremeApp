package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerUpdateWheelSpinner implements Packet{

    private int position;

    public PacketServerUpdateWheelSpinner(){
        //Require empty constructor for instanciation
    }

    public PacketServerUpdateWheelSpinner(int position){
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_UPDATE_WHEEL_SPINNER;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            position = inStream.readInt();
        }catch (IOException e){
            Log.e("PacketSrvUpdateSpinner", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeInt(position);
        }catch (IOException e){
            Log.e("PacketSrvUpdateSpinner","Error sending data",e);
        }
    }
}
