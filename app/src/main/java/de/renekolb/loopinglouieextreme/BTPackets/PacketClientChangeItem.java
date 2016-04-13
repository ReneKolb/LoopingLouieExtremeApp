package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Admi on 10.04.2016.
 */
public class PacketClientChangeItem implements Packet{

    private int itemID;

    public PacketClientChangeItem(){
        //Require empty constructor for instanciation
    }

    public PacketClientChangeItem(int itemID){
        this.itemID = itemID;
    }

    public int getItemID(){
        return this.itemID;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.CLIENT_CHANGE_ITEM;
    }

    @Override
    public void readPacket(DataInputStream inStream) {
        try {
            itemID = inStream.readInt();
        }catch (IOException e){
            Log.e("PacketClChangeItem", "Error reading data", e);
        }
    }

    @Override
    public void sendPacket(DataOutputStream outStream){
        try {
            //write PacketType
            outStream.writeByte(getPacketType().getPacketIdentifier());

            outStream.writeInt(itemID);
        }catch (IOException e){
            Log.e("PacketClChangeItem","Error sending data",e);
        }

    }
}
