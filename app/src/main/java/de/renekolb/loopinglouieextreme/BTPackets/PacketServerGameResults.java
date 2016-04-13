package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerGameResults implements Packet {

    private int first;
    private int second;
    private int third;
    private int fourth;

    public PacketServerGameResults(){
    }

    public PacketServerGameResults(int first, int second,int third, int fourth){
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public int getFirst(){
        return this.first;
    }
    public int getSecond(){
        return this.second;
    }
    public int getThird(){
        return this.third;
    }
    public int getFourth(){
        return this.fourth;
    }


    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_GAME_RESULTS;
    }


    //Form: j[#Rounds]:[WOF enabled]:[LoserWheel enabled].
    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            first = inStream.readInt();
            second = inStream.readInt();
            third = inStream.readInt();
            fourth = inStream.readInt();
        }catch (IOException e){
            Log.e("PacketSrvGameResults", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeInt(first);
            outStream.writeInt(second);
            outStream.writeInt(third);
            outStream.writeInt(fourth);
        }catch (IOException e){
            Log.e("PacketSrvGameResults","Error sending data",e);
        }

    }
}
