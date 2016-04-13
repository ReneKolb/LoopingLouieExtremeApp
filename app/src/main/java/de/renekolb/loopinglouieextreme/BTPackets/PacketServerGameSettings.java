package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketServerGameSettings implements Packet{

    private int maxRounds;
    private boolean enableWheel;
    private boolean enableLoserWheel;

    public PacketServerGameSettings(){

    }

    public PacketServerGameSettings(int maxRounds, boolean enableWheel, boolean enableLoserWheel){
        this.maxRounds = maxRounds;
        this.enableWheel = enableWheel;
        this.enableLoserWheel = enableLoserWheel;
    }

    public int getMaxRounds(){
        return this.maxRounds;
    }

    public boolean getEnableWheel(){
        return this.enableWheel;
    }

    public boolean getEnableLoserWheel(){
        return this.enableLoserWheel;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_GAME_SETTINGS;
    }


//Form: j[#Rounds]:[WOF enabled]:[LoserWheel enabled].
        @Override
        public void readPacket(DataInputStream inStream) {
            try {
                maxRounds = inStream.readInt();
                enableWheel = inStream.readBoolean();
                enableLoserWheel = inStream.readBoolean();
            }catch (IOException e){
                Log.e("PacketSrvUpdateGameSet", "Error reading data", e);
            }
        }

        @Override
        public void sendPacket(DataOutputStream outStream){
            try {
                //write PacketType
                outStream.writeByte(getPacketType().getPacketIdentifier());

                outStream.writeInt(maxRounds);
                outStream.writeBoolean(enableWheel);
                outStream.writeBoolean(enableLoserWheel);
            }catch (IOException e){
                Log.e("PacketSrvUpdateGameSet","Error sending data",e);
            }

        }
}
