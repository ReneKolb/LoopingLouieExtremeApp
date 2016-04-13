package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.renekolb.loopinglouieextreme.BTService;
import de.renekolb.loopinglouieextreme.FullscreenActivity;


public class PacketHandler {
    private FullscreenActivity fa;
    private BTService service;

    public PacketHandler(FullscreenActivity fa, BTService service){
        this.fa = fa;
        this.service = service;
    }

    //Blocking Method!
    public void onDataIn(String senderAddress, DataInputStream inStream)throws IOException{
        int packetTypeBuffer = inStream.read(); //read 1 byte (PacketType)

        if(packetTypeBuffer >=0 && packetTypeBuffer <= 255){
            char packetType = (char) packetTypeBuffer;
            Packet packet = null;
            for(PacketType p : PacketType.values()){
                if(p.getPacketIdentifier() == packetType){
                    packet = p.getNewPacket();
                    break;
                }
            }
            if(packet != null) {
                packet.readPacket(inStream);
                service.onReceivePacket(senderAddress,packet);
            }else{
                Log.e("PacketHandler","Error while determining Packet Type");
            }
        }else{
            Log.w("PacketHandler","invalid packet / PacketType");
        }

    }

    public void sendPacket(DataOutputStream outStream, Packet packet){
        packet.sendPacket(outStream);
    }

    public static void writeString(DataOutputStream out, String str) throws IOException{
/*        byte[] data = str.getBytes();
        out.writeInt(data.length);
        out.write(data);*/
        out.writeUTF(str);
    }

    public static String readString(DataInputStream in) throws IOException{
        /*int strLen = in.readInt();
        byte[] inputBuffer = new byte[strLen];
        in.read(inputBuffer, 0, strLen);
        return new String(inputBuffer);*/
        return  in.readUTF();
    }
}
