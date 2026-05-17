package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.network.S2CUpdateForgingTableModeStatePacket;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.NBTTagCompound;
import net.minecraft.World;

public final class EFAForgingTableModeLogic {

    private static final String MODE_NBT_KEY = "EFAForgingMode";

    public static int normalizeMode(int mode) {
        return Math.floorMod(mode, EFAForgingTable.MODE_COUNT);
    }

    public static int readMode(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(MODE_NBT_KEY)) {
            return EFAForgingTable.MODE_FORGING;
        }

        return normalizeMode(nbt.getInteger(MODE_NBT_KEY));
    }

    public static void writeMode(NBTTagCompound nbt, int mode) {
        if (nbt != null) {
            nbt.setInteger(MODE_NBT_KEY, normalizeMode(mode));
        }
    }

    public static void syncMode(World world, int x, int y, int z, int mode) {
        if (world != null && !world.isRemote) {
            Network.sendToAllPlayers(new S2CUpdateForgingTableModeStatePacket(x, y, z, normalizeMode(mode)));
        }
    }

    public static void syncModeOnOpen(World world, int x, int y, int z, int mode) {
        syncMode(world, x, y, z, mode);
    }
}
