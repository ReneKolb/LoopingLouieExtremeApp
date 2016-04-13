package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketClientPlayerName implements Packet{

    private String playerName;

    public PacketClientPlayerName(){
        //Require empty constructor for instanciation
    }

    public PacketClientPlayerName(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.CLIENT_PLAYER_NAME;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        Log.i("CLPlayerName","read Packet");
        try {
            playerName = PacketHandler.readString(inStream);
        }catch (IOException e){
            Log.e("PacketClPlayerName", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            PacketHandler.writeString(outStream, playerName);
        }catch (IOException e){
            Log.e("PacketClPlayerName","Error sending data",e);
        }
    }
}
