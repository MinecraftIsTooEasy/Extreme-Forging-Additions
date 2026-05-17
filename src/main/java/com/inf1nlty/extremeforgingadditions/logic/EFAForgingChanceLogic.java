package com.inf1nlty.extremeforgingadditions.logic;

import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.mitemod.miteite.api.ITEMaterial;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.material.Materials;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;

public final class EFAForgingChanceLogic {

    private static final int IRON_ENHANCE_STONE_FAILURE = 72;
    private static final int MITHRIL_ENHANCE_STONE_FAILURE = 108;
    private static final int ADAMANTIUM_ENHANCE_STONE_FAILURE = 144;
    private static final int VIBRANIUM_OR_UNIVERSAL_ENHANCE_STONE_FAILURE = 180;
    private static final int UPGRADING_FAILURE_REDUCTION = 36;
    private static final int APPLYING_FAILURE_BASE = 282;
    private static final int TOOL_FAILURE_REDUCTION_PER_HARVEST_LEVEL = 18;

    public static int getUpgradingRecipeFailure(Material enhancerMaterial) {
        return getEnhanceStoneFailure(enhancerMaterial) - UPGRADING_FAILURE_REDUCTION;
    }

    public static int getApplyingRecipeFailure(int templateFailure) {
        return APPLYING_FAILURE_BASE - templateFailure;
    }

    public static int getExtendedModeFailureChance(ForgingTableSlots slots, ForgingRecipe recipe) {
        int failureChance = recipe.chanceOfFailure() - getToolFailureReduction(slots.getHammerItem()) - getToolFailureReduction(slots.getAxeItem());
        return Math.max(Math.min(failureChance, 100), 0);
    }

    private static int getEnhanceStoneFailure(Material material) {
        if (material == Material.iron) {
            return IRON_ENHANCE_STONE_FAILURE;
        }
        if (material == Material.mithril) {
            return MITHRIL_ENHANCE_STONE_FAILURE;
        }
        if (material == Material.adamantium) {
            return ADAMANTIUM_ENHANCE_STONE_FAILURE;
        }
        if (material == Materials.vibranium) {
            return VIBRANIUM_OR_UNIVERSAL_ENHANCE_STONE_FAILURE;
        }
        return VIBRANIUM_OR_UNIVERSAL_ENHANCE_STONE_FAILURE;
    }

    private static int getToolFailureReduction(ItemStack tool) {
        if (tool == null || tool.getMaterialForRepairs() == null) {
            return 0;
        }
        return Math.max((getMinHarvestLevel(tool.getMaterialForRepairs()) - getMinHarvestLevel(Material.copper)) * TOOL_FAILURE_REDUCTION_PER_HARVEST_LEVEL, 0);
    }

    private static int getMinHarvestLevel(Material material) {
        return ((ITEMaterial) material).getMinHarvestLevel();
    }
}
