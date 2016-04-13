package de.renekolb.loopinglouieextreme;

import de.renekolb.loopinglouieextreme.BTPackets.Packet;

/**
 * Created by Admi on 10.04.2016.
 */
public interface BTService {
     void onReceivePacket(String senderAddress, Packet packet);
}
