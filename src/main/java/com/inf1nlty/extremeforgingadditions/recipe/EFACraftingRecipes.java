package com.inf1nlty.extremeforgingadditions.recipe;

import com.inf1nlty.extremeforgingadditions.item.EFAItemRegistry;
import com.inf1nlty.extremeforgingadditions.item.ItemForgingTemplate;
import net.minecraft.Item;
import net.minecraft.ItemIngot;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;
import net.xiaoyu233.mitemod.miteite.item.material.Materials;

public final class EFACraftingRecipes {

    private static final Material[] TEMPLATE_MATERIALS = new Material[] {
            Material.copper,
            Material.silver,
            Material.gold,
            Material.iron,
            Material.ancient_metal,
            Material.mithril,
            Material.adamantium,
            Materials.vibranium
    };

    public static void registerRecipes(RecipeRegistryEvent register) {
        for (int i = 0; i < TEMPLATE_MATERIALS.length; i++) {
            Material material = TEMPLATE_MATERIALS[i];
            ItemForgingTemplate currentTemplate = EFAItemRegistry.getTemplateFor(material);
            Item currentIngot = Item.getMatchingItem(ItemIngot.class, material);
            if (currentTemplate == null || currentIngot == null) {
                continue;
            }

            register.registerShapedRecipe(new ItemStack(currentTemplate), true,
                    "###",
                    "#*#",
                    "###",
                    '#', Item.brick,
                    '*', currentIngot);

            if (i <= 2) {
                registerIronTemplateUpgrade(register, currentTemplate);
                continue;
            }

            if (i + 1 < TEMPLATE_MATERIALS.length) {
                Material nextMaterial = TEMPLATE_MATERIALS[i + 1];
                ItemForgingTemplate nextTemplate = EFAItemRegistry.getTemplateFor(nextMaterial);
                Item nextIngot = Item.getMatchingItem(ItemIngot.class, nextMaterial);
                if (nextTemplate != null && nextIngot != null) {
                    register.registerShapedRecipe(new ItemStack(nextTemplate), true,
                            "###",
                            "#*#",
                            "###",
                            '#', nextIngot,
                            '*', currentTemplate).extendsNBT();
                }
            }
        }
    }

    private static void registerIronTemplateUpgrade(RecipeRegistryEvent register, ItemForgingTemplate sourceTemplate) {
        ItemForgingTemplate ironTemplate = EFAItemRegistry.FORGING_TEMPLATE_IRON;
        Item ironIngot = Item.getMatchingItem(ItemIngot.class, Material.iron);

        register.registerShapedRecipe(new ItemStack(ironTemplate), true,
                "###",
                "#*#",
                "###",
                '#', ironIngot,
                '*', sourceTemplate).extendsNBT();
    }
}
