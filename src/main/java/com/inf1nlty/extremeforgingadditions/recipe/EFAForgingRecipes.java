package com.inf1nlty.extremeforgingadditions.recipe;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.item.EFAItemRegistry;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingChanceLogic;
import net.minecraft.*;
import net.xiaoyu233.mitemod.miteite.api.ITEMaterial;
import net.xiaoyu233.mitemod.miteite.item.MITEITEItemRegistryInit;
import net.xiaoyu233.mitemod.miteite.item.material.Materials;
import net.xiaoyu233.mitemod.miteite.item.recipe.DowngradeFeedback;
import net.xiaoyu233.mitemod.miteite.item.recipe.DurabilityFeedback;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingTableLevel;

public final class EFAForgingRecipes {

    private static final Table<RecipeKey, Integer, EFAForgingRecipe> RECIPES = HashBasedTable.create();

    public static void registerAll() {
        if (!RECIPES.isEmpty()) {
            return;
        }

        if (!EFAItemRegistry.areTemplatesRegistered()) {
            return;
        }

        registerMaterial(Material.copper, Material.iron, ForgingTableLevel.IRON, 14,
                new ItemStack(Item.ingotCopper, 1),
                new ItemStack(Item.dyePowder, 1, 4),
                new ItemStack(Item.ingotCopper, 4));
        registerMaterial(Material.silver, Material.iron, ForgingTableLevel.IRON, 14,
                new ItemStack(Item.ingotSilver, 1),
                new ItemStack(Item.dyePowder, 1, 4),
                new ItemStack(Item.ingotSilver, 4));
        registerMaterial(Material.gold, Material.iron, ForgingTableLevel.IRON, 14,
                new ItemStack(Item.ingotGold, 1),
                new ItemStack(Item.dyePowder, 1, 4),
                new ItemStack(Item.ingotGold, 4));
        registerMaterial(Material.iron, Material.iron, ForgingTableLevel.IRON, 14,
                new ItemStack(Item.ironNugget, 4),
                new ItemStack(Item.shardEmerald, 3),
                new ItemStack(Item.ingotIron, 4));
        registerMaterial(Material.ancient_metal, Material.ancient_metal, ForgingTableLevel.MITHRIL, 14,
                new ItemStack(Item.ancientMetalNugget, 4),
                new ItemStack(Item.shardEmerald, 3),
                new ItemStack(Item.ingotAncientMetal, 4));
        registerMaterial(Material.mithril, Material.mithril, ForgingTableLevel.MITHRIL, 14,
                new ItemStack(Item.mithrilNugget, 3),
                new ItemStack(Item.diamond, 1),
                new ItemStack(Item.ingotMithril, 4));
        registerMaterial(Material.adamantium, Material.adamantium, ForgingTableLevel.ADAMANTIUM, 14,
                new ItemStack(Item.adamantiumNugget, 3),
                new ItemStack(Block.blockRedstone, 1),
                new ItemStack(Item.ingotAdamantium, 4));
        registerMaterial(Materials.vibranium, Materials.vibranium, ForgingTableLevel.VIBRANIUM, 14,
                new ItemStack(MITEITEItemRegistryInit.VIBRANIUM_NUGGET, 2),
                new ItemStack(Block.blockRedstone, 2),
                new ItemStack(MITEITEItemRegistryInit.VIBRANIUM_INGOT, 4));
    }

    public static EFAForgingRecipe getRecipe(Material material, int forgingLevel, int mode) {
        registerAll();
        return RECIPES.get(new RecipeKey(material, forgingLevel), mode);
    }

    public static EFAForgingRecipe wrapVanillaRecipe(ForgingRecipe recipe) {
        return recipe == null ? null : new EFAForgingRecipe(recipe, EFAForgingTable.MODE_FORGING);
    }

    private static void registerMaterial(Material material, Material enhancerMaterial, ForgingTableLevel tableLevel, int maxForgingLevel, ItemStack upgradeMaterialA, ItemStack upgradeMaterialB, ItemStack applyIngot) {
        for (int level = 0; level <= maxForgingLevel; level++) {
            registerUpgradingRecipe(material, enhancerMaterial, level, tableLevel, upgradeMaterialA.copy(), upgradeMaterialB.copy());
            registerDefusingRecipe(material, level, tableLevel);
            registerApplyingRecipe(material, level, tableLevel, applyIngot.copy());
        }
    }

    private static void registerUpgradingRecipe(Material material, Material enhancerMaterial, int level, ForgingTableLevel tableLevel, ItemStack... items) {
        ForgingRecipe.Builder.of(material, level, tableLevel)
                .setChanceOfFailure(EFAForgingChanceLogic.getUpgradingRecipeFailure(enhancerMaterial))
                .setAxeDurabilityCost(getDurabilityCost(material, 200))
                .setHammerDurabilityCost(getDurabilityCost(material, 250))
                .setTimeReq(600 + level * 600)
                .setQualityReward(getQualityReward(level))
                .addFaultFeedback(DowngradeFeedback.of(level / 2))
                .addFaultFeedback(DurabilityFeedback.of(DurabilityFeedback.Type.ofPercentage(Math.max(level * 10 - tableLevel.getLevel() * 5, 0))))
                .addMaterials(items)
                .build(recipe -> putRecipe(recipe, EFAForgingTable.MODE_UPGRADING));
    }

    private static void registerDefusingRecipe(Material material, int level, ForgingTableLevel tableLevel) {
        com.inf1nlty.extremeforgingadditions.item.ItemForgingTemplate template = EFAItemRegistry.getTemplateFor(material);
        if (template == null) {
            return;
        }

        ForgingRecipe.Builder.of(material, level, tableLevel)
                .setChanceOfFailure(template.getFailChance())
                .setAxeDurabilityCost(getDurabilityCost(material, 800))
                .setHammerDurabilityCost(getDurabilityCost(material, 1000))
                .setTimeReq(600 + ((ITEMaterial) material).getMinHarvestLevel() * 200)
                .setQualityReward(getQualityReward(level))
                .addFaultFeedback(DowngradeFeedback.of(66))
                .addFaultFeedback(DurabilityFeedback.of(DurabilityFeedback.Type.ofPercentage(666)))
                .addMaterials(new ItemStack(template, 1))
                .build(recipe -> putRecipe(recipe, EFAForgingTable.MODE_DEFUSING));
    }

    private static void registerApplyingRecipe(Material material, int level, ForgingTableLevel tableLevel, ItemStack ingotStack) {
        com.inf1nlty.extremeforgingadditions.item.ItemForgingTemplate template = EFAItemRegistry.getTemplateFor(material);
        if (template == null) {
            return;
        }

        ForgingRecipe.Builder.of(material, level, tableLevel)
                .setChanceOfFailure(EFAForgingChanceLogic.getApplyingRecipeFailure(template.getFailChance()))
                .setAxeDurabilityCost(getDurabilityCost(material, 800))
                .setHammerDurabilityCost(getDurabilityCost(material, 1000))
                .setTimeReq(1200 + ((ITEMaterial) material).getMinHarvestLevel() * 300)
                .setQualityReward(getQualityReward(level))
                .addFaultFeedback(DowngradeFeedback.of(0))
                .addFaultFeedback(DurabilityFeedback.of(DurabilityFeedback.Type.ofPercentage(20)))
                .addMaterials(ingotStack, new ItemStack(template, 1))
                .build(recipe -> putRecipe(recipe, EFAForgingTable.MODE_APPLYING));
    }

    private static void putRecipe(ForgingRecipe recipe, int mode) {
        RECIPES.put(new RecipeKey(recipe.material(), recipe.levelToUpgrade()), mode, new EFAForgingRecipe(recipe, mode));
    }

    private static int getDurabilityCost(Material material, int factor) {
        return Math.max(1, (int) (((ITEMaterial) material).getDurability() * (float) factor));
    }

    private static EnumQuality getQualityReward(int level) {
        EnumQuality[] values = EnumQuality.values();
        return values[Math.min(values.length - 1, (level + 1) / 2 + 2)];
    }
}
