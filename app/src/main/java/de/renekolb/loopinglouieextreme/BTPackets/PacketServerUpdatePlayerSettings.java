package de.renekolb.loopinglouieextreme.BTPackets;


import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.renekolb.loopinglouieextreme.GamePlayer;

public class PacketServerUpdatePlayerSettings implements Packet {

    private int slot;
    private String displayName;
    private int connectionStateID;
    private int itemTypeID;
    private int chipsAmount;

    public PacketServerUpdatePlayerSettings(){
        //Require empty constructor for instanciation
    }

    public PacketServerUpdatePlayerSettings(int slot, String displayName, int connectionStateID, int itemTypeID, int chipsAmount){
        this.slot = slot;
        this.displayName = displayName;
        this.connectionStateID = connectionStateID;
        this.itemTypeID = itemTypeID;
        this.chipsAmount = chipsAmount;
    }

    public PacketServerUpdatePlayerSettings(int slot, GamePlayer gp){
        this.slot = slot;
        this.displayName = gp.getDisplayName();
        this.connectionStateID = gp.getConnectionState().getId();
        this.itemTypeID = gp.getDefaultItemType().getItemID();
        this.chipsAmount = gp.getCurrentChips();
    }

    public int getSlot(){
        return this.slot;
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public int getConnectionStateID(){
        return this.connectionStateID;
    }

    public int getItemTypeID(){
        return this.itemTypeID;
    }

    public int getChipsAmount(){
        return this.chipsAmount;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SERVER_UPDATE_PLAYER_SETTINGS;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            slot = inStream.readInt();
            displayName = PacketHandler.readString(inStream);
            connectionStateID = inStream.readInt();
            itemTypeID = inStream.readInt();
            chipsAmount = inStream.readInt();
        }catch (IOException e){
            Log.e("PacketServerUpdatePlSet","Error reading data",e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeInt(slot);
            PacketHandler.writeString(outStream, displayName);
            outStream.writeInt(connectionStateID);
            outStream.writeInt(itemTypeID);
            outStream.writeInt(chipsAmount);
        }catch (IOException e){
            Log.e("PacketServerUpdatePlSet","Error sending data",e);
        }

    }


}
