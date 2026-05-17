package com.inf1nlty.extremeforgingadditions.item;

import net.minecraft.Material;
import net.xiaoyu233.mitemod.miteite.item.material.Materials;

public enum ForgingTemplateType {

    COPPER(162, Material.copper, "copper"),
    SILVER(162, Material.silver, "silver"),
    GOLD(162, Material.gold, "gold"),
    IRON(144, Material.iron, "iron"),
    ANCIENT_METAL(126, Material.ancient_metal, "ancient_metal"),
    MITHRIL(108, Material.mithril, "mithril"),
    ADAMANTIUM(90, Material.adamantium, "adamantium"),
    VIBRANIUM(72, Materials.vibranium, "vibranium");

    private final int failChance;
    private final Material material;
    private final String textureName;

    ForgingTemplateType(int failChance, Material material, String textureName) {
        this.failChance = failChance;
        this.material = material;
        this.textureName = textureName;
    }

    public int failChance() {
        return this.failChance;
    }

    public Material material() {
        return this.material;
    }

    public String textureName() {
        return this.textureName;
    }
}
