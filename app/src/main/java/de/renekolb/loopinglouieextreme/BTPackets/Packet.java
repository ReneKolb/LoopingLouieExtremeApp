package de.renekolb.loopinglouieextreme.BTPackets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Packet {

    PacketType getPacketType();

    void readPacket(DataInputStream inStream);

    void sendPacket(DataOutputStream outStream);
}
