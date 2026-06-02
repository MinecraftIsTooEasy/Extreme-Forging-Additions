package com.inf1nlty.extremeforgingadditions.mixin.accessor;

import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ContainerForgingTable.class)
public interface ContainerForgingTablePositionAccessor {

    @Accessor("blockX")
    int efa$getBlockX();

    @Accessor("blockY")
    int efa$getBlockY();

    @Accessor("blockZ")
    int efa$getBlockZ();
}
