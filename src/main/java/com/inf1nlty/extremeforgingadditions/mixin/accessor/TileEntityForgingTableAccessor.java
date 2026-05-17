package com.inf1nlty.extremeforgingadditions.mixin.accessor;

import net.minecraft.ItemStack;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TileEntityForgingTable.class)
public interface TileEntityForgingTableAccessor {

    @Accessor("items")
    ItemStack[] efa$getItems();

    @Accessor("slots")
    ForgingTableSlots efa$getSlots();

    @Accessor("usedRecipe")
    ForgingRecipe efa$getUsedRecipe();

    @Accessor("isForging")
    boolean efa$getIsForging();

    @Invoker("finishForging")
    void efa$finishForging();
}
