package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerSpinWheel implements Packet {

    private float startViewRotation;
    private float rotateAnimation;

    public PacketServerSpinWheel(){
        //Require empty constructor for instanciation
    }

    public PacketServerSpinWheel(float startViewRotation, float rotateAnimation){
        this.startViewRotation = startViewRotation;
        this.rotateAnimation = rotateAnimation;
    }

    public float getStartViewRotation(){
        return this.startViewRotation;
    }

    public float getRotateAnimation(){
        return this.rotateAnimation;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_SPIN_WHEEL;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            startViewRotation = inStream.readFloat();
            rotateAnimation = inStream.readFloat();
        }catch (IOException e){
            Log.e("PacketSrvSpinWheel", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeFloat(startViewRotation);
            outStream.writeFloat(rotateAnimation);
        }catch (IOException e){
            Log.e("PacketSrvSpinWheel","Error sending data",e);
        }
    }
}
