package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableContainer;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.ContainerForgingTableAccessor;
import com.inf1nlty.extremeforgingadditions.network.S2CUpdateForgingTableModeStatePacket;
import moddedmite.rustedironcore.network.Network;
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
            syncModeToPlayer(player, x, y, z, forgingTable);
            return;
        }

        if (player.openContainer instanceof ContainerForgingTable container && container instanceof EFAForgingTableContainer efaContainer) {
            efaContainer.efa$switchMode();
            syncModeToPlayer(player, ((ContainerForgingTableAccessor) container).efa$getTileEntity());
        }
    }

    public static void processRequestForgingTableModePacket(ServerPlayer player, int x, int y, int z) {
        TileEntity tileEntity = player.worldObj == null ? null : player.worldObj.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForgingTable forgingTable) {
            syncModeToPlayer(player, x, y, z, forgingTable);
        } else if (player.openContainer instanceof ContainerForgingTable container) {
            syncModeToPlayer(player, ((ContainerForgingTableAccessor) container).efa$getTileEntity());
        }
    }

    public static void handleUpdateForgingTableModeState(Minecraft mc, S2CUpdateForgingTableModeStatePacket packet) {
        EFAForgingTableGuiLogic.handleModeStatePacket(mc, packet);
    }

    private static void syncModeToPlayer(ServerPlayer player, int x, int y, int z, TileEntityForgingTable forgingTable) {
        if (forgingTable instanceof EFAForgingTable efaForgingTable) {
            Network.sendToClient(player, new S2CUpdateForgingTableModeStatePacket(x, y, z, efaForgingTable.efa$getForgingMode()));
        }
    }

    private static void syncModeToPlayer(ServerPlayer player, TileEntityForgingTable forgingTable) {
        if (forgingTable != null) {
            syncModeToPlayer(player, forgingTable.xCoord, forgingTable.yCoord, forgingTable.zCoord, forgingTable);
        }
    }
}
