package com.inf1nlty.extremeforgingadditions.mixin.accessor;

import net.minecraft.Slot;
import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgingTableSlots.class)
public interface ForgingTableSlotsAccessor {

    @Accessor("up")
    Slot efa$getUp();

    @Accessor("downLeft")
    Slot efa$getDownLeft();

    @Accessor("downRight")
    Slot efa$getDownRight();

    @Accessor("left")
    Slot efa$getLeft();

    @Accessor("right")
    Slot efa$getRight();

    @Accessor("container")
    ContainerForgingTable efa$getContainer();

    @Accessor("tileEntityForgingTable")
    TileEntityForgingTable efa$getTileEntityForgingTable();
}
