package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableGui;
import com.inf1nlty.extremeforgingadditions.network.C2SSwitchForgingTableModePacket;
import com.inf1nlty.extremeforgingadditions.network.S2CUpdateForgingTableModeStatePacket;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.TileEntity;
import net.xiaoyu233.mitemod.miteite.gui.GuiForgingTable;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;

public final class EFAForgingTableGuiLogic {

    private static final int CONTROL_X_OFFSET = 100;
    private static final int START_BUTTON_Y_OFFSET = 10;
    private static final int MODE_BUTTON_Y_OFFSET = 40;
    private static final int BUTTON_WIDTH = 40;
    private static final int BUTTON_HEIGHT = 20;

    public static void moveStartButton(GuiButton startButton, int width, int height) {
        if (startButton != null) {
            startButton.xPosition = width / 2 + CONTROL_X_OFFSET;
            startButton.yPosition = height / 2 + START_BUTTON_Y_OFFSET;
        }
    }

    public static GuiButton createModeButton(GuiButton previousButton, int width, int height) {
        GuiButton button = new GuiButton(1, width / 2 + CONTROL_X_OFFSET, height / 2 + MODE_BUTTON_Y_OFFSET, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.getString("gui.forgingTable.mode"));
        button.enabled = previousButton == null || previousButton.enabled;
        return button;
    }

    public static String getModeInfoText(int modeIndex) {
        return I18n.getString("gui.forgingTable.modeInfo") + ":" + getModeLabel(modeIndex);
    }

    public static String getModeLabel(int modeIndex) {
        return switch (EFAForgingTableModeLogic.normalizeMode(modeIndex)) {
            case EFAForgingTable.MODE_UPGRADING -> I18n.getString("gui.forgingTable.modeUpgrading");
            case EFAForgingTable.MODE_DEFUSING -> I18n.getString("gui.forgingTable.modeDefusing");
            case EFAForgingTable.MODE_APPLYING -> I18n.getString("gui.forgingTable.modeApplying");
            case EFAForgingTable.MODE_FORGING -> I18n.getString("gui.forgingTable.modeForging");
            default -> "";
        };
    }

    public static boolean handleModeButtonAction(GuiButton button, GuiButton modeButton, EntityPlayer player, int x, int y, int z) {
        if (button != modeButton) {
            return false;
        }

        Network.sendToServer(new C2SSwitchForgingTableModePacket(x, y, z));
        if (modeButton != null) {
            modeButton.enabled = false;
        }

        return true;
    }

    public static void enableModeButton(GuiButton modeButton) {
        if (modeButton != null) {
            modeButton.enabled = true;
        }
    }

    public static void applyModeToTile(Minecraft minecraft, int x, int y, int z, int mode) {
        if (minecraft == null || minecraft.theWorld == null) {
            return;
        }

        TileEntity tileEntity = minecraft.theWorld.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityForgingTable && tileEntity instanceof EFAForgingTable forgingTable) {
            forgingTable.efa$setForgingMode(EFAForgingTableModeLogic.normalizeMode(mode));
        }
    }

    public static void handleModeStatePacket(Minecraft minecraft, S2CUpdateForgingTableModeStatePacket packet) {
        if (minecraft == null || minecraft.theWorld == null) {
            return;
        }

        TileEntity tileEntity = minecraft.theWorld.getBlockTileEntity(packet.getX(), packet.getY(), packet.getZ());
        if (tileEntity instanceof TileEntityForgingTable && tileEntity instanceof EFAForgingTable forgingTable) {
            forgingTable.efa$setForgingMode(EFAForgingTableModeLogic.normalizeMode(packet.getState()));
        }

        GuiScreen currentScreen = minecraft.currentScreen;
        if (currentScreen instanceof GuiForgingTable && currentScreen instanceof EFAForgingTableGui gui
                && gui.efa$getBlockX() == packet.getX()
                && gui.efa$getBlockY() == packet.getY()
                && gui.efa$getBlockZ() == packet.getZ()) {
            gui.efa$setBlockMode(packet.getState());
            ((GuiForgingTable) currentScreen).enableButton();
        }
    }
}
