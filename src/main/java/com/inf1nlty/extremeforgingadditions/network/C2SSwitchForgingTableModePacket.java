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

    private final int x;
    private final int y;
    private final int z;

    public C2SSwitchForgingTableModePacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public C2SSwitchForgingTableModePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player instanceof ServerPlayer serverPlayer) {
            EFAForgingTablePacketLogic.processSwitchForgingTableModePacket(serverPlayer, this.x, this.y, this.z);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return CHANNEL;
    }
}
