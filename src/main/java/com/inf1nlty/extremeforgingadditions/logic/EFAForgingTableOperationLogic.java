package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingSlots;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.item.EFAItemRegistry;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.TileEntityForgingTableAccessor;
import com.inf1nlty.extremeforgingadditions.recipe.EFAForgingRecipe;
import com.inf1nlty.extremeforgingadditions.recipe.EFAForgingRecipes;
import net.minecraft.*;
import net.xiaoyu233.mitemod.miteite.api.ITEMaterial;
import net.xiaoyu233.mitemod.miteite.api.ITEStack;
import net.xiaoyu233.mitemod.miteite.api.IUpgradableItem;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.ArmorModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.ModifierUtils;
import net.xiaoyu233.mitemod.miteite.item.ToolModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class EFAForgingTableOperationLogic {

    private static final String MODIFIER_TAG = "modifiers";

    public static ItemStack handleCompleteForging(TileEntityForgingTable tile, ItemStack toolItem, int grade, int mode) {
        ITEStack iteStack = asITEStack(toolItem);
        EFAForgingRecipe recipe = EFAForgingRecipes.getRecipe(toolItem.getItem().getMaterialForRepairs(), iteStack.getForgingGrade(), mode);
        if (mode == EFAForgingTable.MODE_FORGING) {
            iteStack.setForgingGrade(grade);
            return null;
        }

        Random random = getRandom(tile);
        if (recipe != null) {
            return completeExtendedForging(tile, toolItem, recipe, random);
        }

        ForgingRecipe usedRecipe = ((TileEntityForgingTableAccessor) tile).efa$getUsedRecipe();
        if (usedRecipe != null) {
            return completeExtendedForging(tile, toolItem, EFAForgingRecipes.wrapVanillaRecipe(usedRecipe), random);
        }

        return null;
    }

    public static ItemStack resolveCompletionOutput(TileEntityForgingTable tile, ItemStack originalOutput, ItemStack overrideOutput) {
        ForgingRecipe usedRecipe = ((TileEntityForgingTableAccessor) tile).efa$getUsedRecipe();
        ItemStack effectiveOutput = overrideOutput == null ? originalOutput : overrideOutput;
        if (overrideOutput != null && usedRecipe != null && usedRecipe.qualityReward() != null) {
            effectiveOutput.setQuality(usedRecipe.qualityReward());
        }

        return effectiveOutput;
    }

    @SuppressWarnings("rawtypes")
    public static List redirectUniversalFailure(TileEntityForgingTable tile, ForgingRecipe recipe) {
        ForgingTableSlots slots = ((TileEntityForgingTableAccessor) tile).efa$getSlots();
        if (slots instanceof EFAForgingSlots efaSlots && efaSlots.efa$hasAndConsumeUniversalEnhanceStone()) {
            return Collections.emptyList();
        }

        return recipe.faultFeedback();
    }

    public static ItemStack completeExtendedForging(TileEntityForgingTable tile, ItemStack toolItem, EFAForgingRecipe recipe, Random random) {
        if (recipe == null) {
            return null;
        }

        if (recipe.mode() == EFAForgingTable.MODE_UPGRADING) {
            addExpForTool(tile, toolItem);
            return null;
        }

        if (recipe.mode() == EFAForgingTable.MODE_DEFUSING) {
            return defuseModifier(toolItem, random);
        }

        if (recipe.mode() == EFAForgingTable.MODE_APPLYING) {
            applyTemplate(tile, toolItem);
        }

        return null;
    }

    private static void addExpForTool(TileEntityForgingTable tile, ItemStack toolItem) {
        if (toolItem.stackTagCompound == null) {
            asITEStack(toolItem).fixNBT();
        }

        int exp = (int) Math.pow(3, ((ITEMaterial) toolItem.getMaterialForRepairs()).getMinHarvestLevel()) * 10;
        EntityPlayer player = getClosestPlayer(tile);
        if (player != null) {
            ((IUpgradableItem) toolItem.getItem()).addExpForTool(toolItem, player, exp);
        }
    }

    private static EntityPlayer getClosestPlayer(TileEntityForgingTable tile) {
        World world = tile.getWorldObj();
        EntityPlayer player = world.getClosestPlayer((double) tile.xCoord + 0.5D, (double) tile.yCoord + 0.5D, (double) tile.zCoord + 0.5D, 16.0D, false);
        if (player != null) {
            return player;
        }

        return world.getClosestPlayer((double) tile.xCoord + 0.5D, (double) tile.yCoord + 0.5D, (double) tile.zCoord + 0.5D, 64.0D, false);
    }

    private static Random getRandom(TileEntityForgingTable tile) {
        World world = tile.getWorldObj();
        return world == null ? new Random() : world.rand;
    }

    private static ItemStack defuseModifier(ItemStack toolItem, Random random) {
        if (!EFAForgingTableSlotsLogic.hasDefusableModifiers(toolItem)) {
            return null;
        }

        Item templateItem = EFAItemRegistry.getTemplateFor(toolItem.getItem().getMaterialForRepairs());
        if (templateItem == null) {
            return null;
        }

        ItemStack templateStack = new ItemStack(templateItem);
        ensureModifierTag(templateStack);
        NBTTagCompound templateModifiers = getModifiers(templateStack);
        if (toolItem.getItem() instanceof ItemTool) {
            List<ToolModifierTypes> obtained = new ArrayList<>();
            NBTTagCompound toolModifiers = getModifiers(toolItem);
            for (ToolModifierTypes value : ToolModifierTypes.values()) {
                if (value.canApplyTo(toolItem) && toolModifiers.hasKey(value.nbtName)) {
                    obtained.add(value);
                }
            }

            if (!obtained.isEmpty()) {
                ToolModifierTypes modifier = obtained.get(random.nextInt(obtained.size()));
                templateModifiers.setInteger(modifier.nbtName, 1);
            }
        } else if (toolItem.getItem() instanceof ItemArmor) {
            List<ArmorModifierTypes> obtained = new ArrayList<>();
            NBTTagCompound armorModifiers = getModifiers(toolItem);
            for (ArmorModifierTypes value : ArmorModifierTypes.values()) {
                if (value.canApplyTo(toolItem) && armorModifiers.hasKey(value.nbtName)) {
                    obtained.add(value);
                }
            }

            if (!obtained.isEmpty()) {
                ArmorModifierTypes modifier = obtained.get(random.nextInt(obtained.size()));
                templateModifiers.setInteger(modifier.nbtName, 1);
            }
        }

        return templateStack;
    }

    private static void applyTemplate(TileEntityForgingTable tile, ItemStack toolItem) {
        ForgingTableSlots slots = ((TileEntityForgingTableAccessor) tile).efa$getSlots();
        if (!(slots instanceof EFAForgingSlots efaSlots)) {
            return;
        }

        ItemStack template = efaSlots.efa$getTemplate();
        if (template == null) {
            return;
        }

        ensureModifierTag(toolItem);
        ensureModifierTag(template);
        NBTTagCompound toolModifiers = getModifiers(toolItem);
        NBTTagCompound templateModifiers = getModifiers(template);

        if (toolItem.getItem() instanceof ItemTool) {
            List<ToolModifierTypes> templateTypes = getToolTemplateModifiers(toolItem, templateModifiers);
            List<ToolModifierTypes> allTypes = ModifierUtils.getAllCanBeAppliedToolModifiers(toolItem);
            int cap = Math.min(ToolModifierTypes.values().length, 4 + asITEStack(toolItem).getForgingGrade() / 5);
            if (countOwnedToolModifiers(toolModifiers, allTypes) + templateTypes.size() <= cap) {
                for (ToolModifierTypes type : templateTypes) {
                    toolModifiers.setInteger(type.nbtName, Math.max(1, templateModifiers.getInteger(type.nbtName)));
                }
            }
        } else if (toolItem.getItem() instanceof ItemArmor) {
            List<ArmorModifierTypes> templateTypes = getArmorTemplateModifiers(toolItem, templateModifiers);
            List<ArmorModifierTypes> allTypes = ModifierUtils.getAllCanBeAppliedArmorModifiers(toolItem);
            int cap = Math.min(ArmorModifierTypes.values().length, 4 + asITEStack(toolItem).getForgingGrade() / 5);
            if (countOwnedArmorModifiers(toolModifiers, allTypes) + templateTypes.size() <= cap) {
                for (ArmorModifierTypes type : templateTypes) {
                    toolModifiers.setInteger(type.nbtName, Math.max(1, templateModifiers.getInteger(type.nbtName)));
                }
            }
        }
    }

    private static NBTTagCompound getModifiers(ItemStack stack) {
        ensureModifierTag(stack);
        return stack.stackTagCompound.getCompoundTag(MODIFIER_TAG);
    }

    private static void ensureModifierTag(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }

        if (!stack.stackTagCompound.hasKey(MODIFIER_TAG)) {
            stack.stackTagCompound.setCompoundTag(MODIFIER_TAG, new NBTTagCompound());
        }
    }

    private static List<ToolModifierTypes> getToolTemplateModifiers(ItemStack toolItem, NBTTagCompound templateModifiers) {
        List<ToolModifierTypes> result = new ArrayList<>();
        for (ToolModifierTypes type : ModifierUtils.getAllCanBeAppliedToolModifiers(toolItem)) {
            if (templateModifiers.hasKey(type.nbtName)) {
                result.add(type);
            }
        }

        return result;
    }

    private static List<ArmorModifierTypes> getArmorTemplateModifiers(ItemStack toolItem, NBTTagCompound templateModifiers) {
        List<ArmorModifierTypes> result = new ArrayList<>();
        for (ArmorModifierTypes type : ModifierUtils.getAllCanBeAppliedArmorModifiers(toolItem)) {
            if (templateModifiers.hasKey(type.nbtName)) {
                result.add(type);
            }
        }

        return result;
    }

    private static int countOwnedToolModifiers(NBTTagCompound modifiers, List<ToolModifierTypes> allTypes) {
        int count = 0;
        for (ToolModifierTypes type : allTypes) {
            if (modifiers.hasKey(type.nbtName)) {
                count++;
            }
        }

        return count;
    }

    private static int countOwnedArmorModifiers(NBTTagCompound modifiers, List<ArmorModifierTypes> allTypes) {
        int count = 0;
        for (ArmorModifierTypes type : allTypes) {
            if (modifiers.hasKey(type.nbtName)) {
                count++;
            }
        }

        return count;
    }

    private static ITEStack asITEStack(ItemStack stack) {
        return (ITEStack) (Object) stack;
    }
}
