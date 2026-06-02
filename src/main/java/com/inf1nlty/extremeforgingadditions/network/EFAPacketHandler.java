package com.inf1nlty.extremeforgingadditions.network;

import moddedmite.rustedironcore.network.PacketReader;

public final class EFAPacketHandler {

    public static void init() {
        PacketReader.registerServerPacketReader(C2SSwitchForgingTableModePacket.CHANNEL, C2SSwitchForgingTableModePacket::new);
        PacketReader.registerServerPacketReader(C2SRequestForgingTableModePacket.CHANNEL, C2SRequestForgingTableModePacket::new);
        PacketReader.registerClientPacketReader(S2CUpdateForgingTableModeStatePacket.CHANNEL, S2CUpdateForgingTableModeStatePacket::new);
    }
}
