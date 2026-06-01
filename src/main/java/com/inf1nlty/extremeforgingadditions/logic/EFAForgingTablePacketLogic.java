package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableContainer;
import com.inf1nlty.extremeforgingadditions.network.S2CUpdateForgingTableModeStatePacket;
import net.minecraft.Minecraft;
import net.minecraft.ServerPlayer;
import net.minecraft.TileEntity;
import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;

public final class EFAForgingTablePacketLogic {

    public static void processSwitchForgingTableModePacket(ServerPlayer player, int x, int y, int z) {
        TileEntity tileEntity = player.worldObj == null ? null : player.worldObj.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForgingTable forgingTable) {
            EFAForgingTableContainerLogic.switchMode(forgingTable);
            return;
        }

        if (player.openContainer instanceof ContainerForgingTable container && container instanceof EFAForgingTableContainer efaContainer) {
            efaContainer.efa$switchMode();
        }
    }

    public static void handleUpdateForgingTableModeState(Minecraft mc, S2CUpdateForgingTableModeStatePacket packet) {
        EFAForgingTableGuiLogic.handleModeStatePacket(mc, packet);
    }
}
