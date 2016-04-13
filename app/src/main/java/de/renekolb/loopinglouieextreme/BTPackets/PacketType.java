package de.renekolb.loopinglouieextreme.BTPackets;

import android.util.Log;

public enum PacketType {

    //Packets sent by Server to Client
    SERVER_UPDATE_PLAYER_SETTINGS('a', PacketServerUpdatePlayerSettings.class),
    SERVER_GAME_SETTINGS('b', PacketServerGameSettings.class),
    SERVER_GAME_START('c', PacketServerGameStart.class),
    SERVER_GAME_RESULTS('d', PacketServerGameResults.class),
    SERVER_GOTO_WHEEL('e', PacketServerGoToWheel.class),
    SERVER_UPDATE_WHEEL_SPINNER('f', PacketServerUpdateWheelSpinner.class),
    SERVER_SPIN_WHEEL('g', PacketServerSpinWheel.class),
    SERVER_NEXT_ROUND('h', PacketServerNextRound.class),
    SERVER_END_GAME('i', PacketServerEndGame.class),


    //Packets sent by Client to Server
    CLIENT_PLAYER_NAME('A', PacketClientPlayerName.class),
    CLIENT_CHANGE_ITEM('B', PacketClientChangeItem.class),
    CLIENT_SPIN_WHEEL('C', PacketClientSpinWheel.class),
    CLIENT_WHEEL_NEXT_PLAYER('D', PacketClientWheelNextPlayer.class);
    //must be different chars than above

    private final char packetIdentifier;
    private final Class<? extends Packet> packetClass;

    PacketType(char packetIdentifier, Class<? extends Packet> packetClass){
        this.packetIdentifier = packetIdentifier;
        this.packetClass = packetClass;
    }

    public final char getPacketIdentifier(){
        return this.packetIdentifier;
    }

    public Packet getNewPacket(){
        try {
            return packetClass.newInstance();
        }catch (Exception e){
            Log.e("PacketType","Error while instanciating new Packet",e);
        }
        return null;
    }
}
