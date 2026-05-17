package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;

public final class EFAForgingTableContainerLogic {

    public static void switchMode(TileEntityForgingTable tileEntity) {
        if (tileEntity instanceof EFAForgingTable forgingTable && !forgingTable.efa$isForging()) {
            forgingTable.efa$setForgingMode(forgingTable.efa$getForgingMode() + 1);
        }
    }
}
