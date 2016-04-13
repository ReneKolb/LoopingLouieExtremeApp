package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketClientSpinWheel implements Packet {

    private float viewRot;
    private float animRot;

    public PacketClientSpinWheel(){
        //Require empty constructor for instanciation
    }

    public PacketClientSpinWheel(float viewRot, float animRot){
        this.viewRot = viewRot;
        this.animRot = animRot;
    }

    public float getViewStartRotation(){
        return viewRot;
    }

    public float getAnimationRot(){
        return animRot;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.CLIENT_SPIN_WHEEL;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            viewRot = inStream.readFloat();
            animRot = inStream.readFloat();
        }catch (IOException e){
            Log.e("PacketClSpinWheel", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeFloat(viewRot);
            outStream.writeFloat(animRot);
        }catch (IOException e){
            Log.e("PacketClSpinWheel","Error sending data",e);
        }

    }
}
