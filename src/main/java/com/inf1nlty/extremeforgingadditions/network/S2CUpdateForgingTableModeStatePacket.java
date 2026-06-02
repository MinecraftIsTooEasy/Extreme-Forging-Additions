package com.inf1nlty.extremeforgingadditions.network;

import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTablePacketLogic;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;

public class S2CUpdateForgingTableModeStatePacket implements Packet {

    public static final ResourceLocation CHANNEL = new ResourceLocation("efa", "mode_state");

    private final int x;
    private final int y;
    private final int z;
    private final int state;

    public S2CUpdateForgingTableModeStatePacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public S2CUpdateForgingTableModeStatePacket(int x, int y, int z, int state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.state = state;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.state);
    }

    @Override
    public void apply(EntityPlayer player) {
        EFAForgingTablePacketLogic.handleUpdateForgingTableModeState(Minecraft.getMinecraft(), this);
    }

    @Override
    public ResourceLocation getChannel() {
        return CHANNEL;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getState() {
        return this.state;
    }
}
