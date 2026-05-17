package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableContainer;
import com.inf1nlty.extremeforgingadditions.network.S2CUpdateForgingTableModeStatePacket;
import net.minecraft.Minecraft;
import net.minecraft.ServerPlayer;
import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;

public final class EFAForgingTablePacketLogic {

    public static void processSwitchForgingTableModePacket(ServerPlayer player) {
        if (player.openContainer instanceof ContainerForgingTable container && container instanceof EFAForgingTableContainer efaContainer) {
            efaContainer.efa$switchMode();
        }
    }

    public static void handleUpdateForgingTableModeState(Minecraft mc, S2CUpdateForgingTableModeStatePacket packet) {
        EFAForgingTableGuiLogic.handleModeStatePacket(mc, packet);
    }
}
