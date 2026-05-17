package com.inf1nlty.extremeforgingadditions.logic;

import com.google.common.collect.Lists;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingSlots;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.item.EFAItemRegistry;
import com.inf1nlty.extremeforgingadditions.item.ItemForgingTemplate;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.ForgingTableSlotsAccessor;
import com.inf1nlty.extremeforgingadditions.recipe.EFAForgingRecipe;
import com.inf1nlty.extremeforgingadditions.recipe.EFAForgingRecipes;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.minecraft.NBTTagCompound;
import net.minecraft.Slot;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingTableRecipes;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;

import java.util.List;

public final class EFAForgingTableSlotsLogic {

    public static ForgingRecipe redirectModeRecipeLookup(ForgingTableSlots slots, Material material, int forgingLevel) {
        TileEntityForgingTable tileEntity = ((ForgingTableSlotsAccessor) slots).efa$getTileEntityForgingTable();
        int mode = tileEntity instanceof EFAForgingTable forgingTable ? forgingTable.efa$getForgingMode() : EFAForgingTable.MODE_FORGING;
        if (mode == EFAForgingTable.MODE_DEFUSING) {
            ItemStack toolItem = slots.getToolItem();
            if (!hasDefusableModifiers(toolItem)) {
                return null;
            }
        }
        if (mode == EFAForgingTable.MODE_FORGING) {
            return ForgingTableRecipes.getRecipe(material, forgingLevel);
        }

        EFAForgingRecipe recipe = EFAForgingRecipes.getRecipe(material, forgingLevel, mode);
        return recipe == null ? null : recipe.recipe();
    }

    public static List<ItemStack> getNeedItems(ForgingTableSlots slots, ForgingRecipe recipe) {
        if (!isExtendedMode(slots)) {
            return null;
        }

        List<ItemStack> currentMaterials = getMaterialStacks(slots);
        List<ItemStack> materialsRequired = Lists.newArrayList(recipe.materialsToUpgrade());
        materialsRequired.removeIf(req -> currentMaterials.stream().anyMatch(current -> matchesRequired(slots, req, current) && current.stackSize >= req.stackSize));
        return materialsRequired;
    }

    public static boolean costItems(ForgingTableSlots slots, ForgingRecipe recipe) {
        if (!isExtendedMode(slots)) {
            return false;
        }

        List<Slot> currentMaterials = getMaterialSlots(slots);
        List<ItemStack> materialsRequired = Lists.newArrayList(recipe.materialsToUpgrade());
        for (Slot current : currentMaterials) {
            ItemStack currentStack = current.getStack();
            if (currentStack == null) {
                continue;
            }

            for (int i = 0; i < materialsRequired.size(); i++) {
                ItemStack req = materialsRequired.get(i);
                if (req != null && matchesRequired(slots, req, currentStack)) {
                    int resultSize = currentStack.stackSize - req.stackSize;
                    if (resultSize > 0) {
                        currentStack.setStackSize(resultSize);
                    } else {
                        current.putStack(null);
                    }

                    materialsRequired.set(i, null);
                    break;
                }
            }
        }

        return true;
    }

    public static Integer getExtendedModeFailureChance(ForgingTableSlots slots, ForgingRecipe recipe) {
        if (!isExtendedMode(slots)) {
            return null;
        }

        return EFAForgingChanceLogic.getExtendedModeFailureChance(slots, recipe);
    }

    public static ItemStack getTemplate(ForgingTableSlots slots) {
        if (!(slots instanceof EFAForgingSlots efaSlots)) {
            return null;
        }

        ItemStack toolItem = slots.getToolItem();
        if (toolItem == null) {
            return null;
        }

        for (Slot slot : getMaterialSlots(slots)) {
            ItemStack current = slot.getStack();
            if (EFAItemRegistry.isTemplateFor(current, toolItem.getItem().getMaterialForRepairs())) {
                ItemStack template = current.copy();
                template.stackSize = 1;
                int resultSize = current.stackSize - 1;
                if (resultSize > 0) {
                    current.setStackSize(resultSize);
                } else {
                    slot.putStack(null);
                }

                return template;
            }
        }

        return null;
    }

    public static boolean consumeUniversalEnhanceStone(ForgingTableSlots slots) {
        for (Slot slot : getMaterialSlots(slots)) {
            ItemStack current = slot.getStack();
            if (current != null && current.getItem() == net.xiaoyu233.mitemod.miteite.item.MITEITEItemRegistryInit.UNIVERSAL_ENHANCE_STONE) {
                int resultSize = current.stackSize - 1;
                if (resultSize > 0) {
                    current.setStackSize(resultSize);
                } else {
                    slot.putStack(null);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean hasDefusableModifiers(ItemStack toolItem) {
        if (toolItem == null || toolItem.stackTagCompound == null) {
            return false;
        }

        NBTTagCompound modifiers = toolItem.stackTagCompound.getCompoundTag("modifiers");
        return modifiers != null && !modifiers.hasNoTags();
    }

    private static boolean isExtendedMode(ForgingTableSlots slots) {
        TileEntityForgingTable tileEntity = ((ForgingTableSlotsAccessor) slots).efa$getTileEntityForgingTable();
        return tileEntity instanceof EFAForgingTable forgingTable && forgingTable.efa$getForgingMode() != EFAForgingTable.MODE_FORGING;
    }

    private static boolean matchesRequired(ForgingTableSlots slots, ItemStack required, ItemStack current) {
        if (required == null || current == null) {
            return false;
        }

        if (required.getItem() instanceof ItemForgingTemplate) {
            ItemStack toolItem = slots.getToolItem();
            return toolItem != null && EFAItemRegistry.isTemplateFor(current, toolItem.getItem().getMaterialForRepairs());
        }

        return ItemStack.areItemStacksEqual(required, current, true, false, false, true);
    }

    private static List<Slot> getMaterialSlots(ForgingTableSlots slots) {
        ForgingTableSlotsAccessor accessor = (ForgingTableSlotsAccessor) slots;
        return Lists.newArrayList(accessor.efa$getUp(), accessor.efa$getLeft(), accessor.efa$getRight(), accessor.efa$getDownLeft(), accessor.efa$getDownRight());
    }

    private static List<ItemStack> getMaterialStacks(ForgingTableSlots slots) {
        return getMaterialSlots(slots).stream().map(Slot::getStack).toList();
    }
}
