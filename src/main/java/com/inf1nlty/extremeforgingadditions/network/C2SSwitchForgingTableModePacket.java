package com.inf1nlty.extremeforgingadditions.network;

import com.inf1nlty.extremeforgingadditions.ExtremeForgingAdditionsMod;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTablePacketLogic;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;
import net.minecraft.ServerPlayer;

public class C2SSwitchForgingTableModePacket implements Packet {

    public static final ResourceLocation CHANNEL = new ResourceLocation(ExtremeForgingAdditionsMod.MOD_ID, "switch_forging_table_mode");

    public C2SSwitchForgingTableModePacket() {}

    public C2SSwitchForgingTableModePacket(PacketByteBuf buf) {}

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public void apply(EntityPlayer player) {
        if (player instanceof ServerPlayer serverPlayer) {
            EFAForgingTablePacketLogic.processSwitchForgingTableModePacket(serverPlayer);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return CHANNEL;
    }
}
