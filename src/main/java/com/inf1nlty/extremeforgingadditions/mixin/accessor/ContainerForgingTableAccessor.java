package com.inf1nlty.extremeforgingadditions.mixin.accessor;

import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ContainerForgingTable.class)
public interface ContainerForgingTableAccessor {

    @Accessor("tileentity")
    TileEntityForgingTable efa$getTileEntity();
}
