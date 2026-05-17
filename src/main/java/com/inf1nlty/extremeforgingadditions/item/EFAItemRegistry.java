package com.inf1nlty.extremeforgingadditions.item;

import com.inf1nlty.extremeforgingadditions.ExtremeForgingAdditionsMod;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;
import net.xiaoyu233.mitemod.miteite.item.material.Materials;

public final class EFAItemRegistry {

    private static final String MOD_DISPLAY_NAME = "Extreme-Forging-Additions";
    private static final String MOD_NAMESPACE = ExtremeForgingAdditionsMod.MOD_ID;
    private static final String RES_PREFIX = MOD_NAMESPACE + ":";

    public static ItemForgingTemplate FORGING_TEMPLATE_COPPER;
    public static ItemForgingTemplate FORGING_TEMPLATE_SILVER;
    public static ItemForgingTemplate FORGING_TEMPLATE_GOLD;
    public static ItemForgingTemplate FORGING_TEMPLATE_IRON;
    public static ItemForgingTemplate FORGING_TEMPLATE_ANCIENT_METAL;
    public static ItemForgingTemplate FORGING_TEMPLATE_MITHRIL;
    public static ItemForgingTemplate FORGING_TEMPLATE_ADAMANTIUM;
    public static ItemForgingTemplate FORGING_TEMPLATE_VIBRANIUM;

    public static ItemForgingTemplate getTemplateFor(Material material) {
        if (material == Material.copper) {
            return FORGING_TEMPLATE_COPPER;
        }
        if (material == Material.silver) {
            return FORGING_TEMPLATE_SILVER;
        }
        if (material == Material.gold) {
            return FORGING_TEMPLATE_GOLD;
        }
        if (material == Material.iron) {
            return FORGING_TEMPLATE_IRON;
        }
        if (material == Material.ancient_metal) {
            return FORGING_TEMPLATE_ANCIENT_METAL;
        }
        if (material == Material.mithril) {
            return FORGING_TEMPLATE_MITHRIL;
        }
        if (material == Material.adamantium) {
            return FORGING_TEMPLATE_ADAMANTIUM;
        }
        if (material == Materials.vibranium) {
            return FORGING_TEMPLATE_VIBRANIUM;
        }
        return null;
    }

    public static boolean isTemplateFor(ItemStack stack, Material material) {
        return stack != null && stack.getItem() == getTemplateFor(material);
    }

    public static boolean areTemplatesRegistered() {
        return FORGING_TEMPLATE_COPPER != null
                && FORGING_TEMPLATE_SILVER != null
                && FORGING_TEMPLATE_GOLD != null
                && FORGING_TEMPLATE_IRON != null
                && FORGING_TEMPLATE_ANCIENT_METAL != null
                && FORGING_TEMPLATE_MITHRIL != null
                && FORGING_TEMPLATE_ADAMANTIUM != null
                && FORGING_TEMPLATE_VIBRANIUM != null;
    }

    public static void registerItems(ItemRegistryEvent event) {
        FORGING_TEMPLATE_COPPER = registerTemplate(event, ForgingTemplateType.COPPER);
        FORGING_TEMPLATE_SILVER = registerTemplate(event, ForgingTemplateType.SILVER);
        FORGING_TEMPLATE_GOLD = registerTemplate(event, ForgingTemplateType.GOLD);
        FORGING_TEMPLATE_IRON = registerTemplate(event, ForgingTemplateType.IRON);
        FORGING_TEMPLATE_ANCIENT_METAL = registerTemplate(event, ForgingTemplateType.ANCIENT_METAL);
        FORGING_TEMPLATE_MITHRIL = registerTemplate(event, ForgingTemplateType.MITHRIL);
        FORGING_TEMPLATE_ADAMANTIUM = registerTemplate(event, ForgingTemplateType.ADAMANTIUM);
        FORGING_TEMPLATE_VIBRANIUM = registerTemplate(event, ForgingTemplateType.VIBRANIUM);
    }

    private static ItemForgingTemplate registerTemplate(ItemRegistryEvent event, ForgingTemplateType type) {
        String name = type.textureName();
        ItemForgingTemplate template = new ItemForgingTemplate(IdUtil.getNextItemID(), type);
        event.register(MOD_DISPLAY_NAME, RES_PREFIX + "forging_template/" + name, "forging_template/" + name, template);
        return template;
    }
}
