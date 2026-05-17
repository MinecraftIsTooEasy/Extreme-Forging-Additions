package com.inf1nlty.extremeforgingadditions.recipe;

import net.minecraft.EnumQuality;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingTableLevel;
import net.xiaoyu233.mitemod.miteite.item.recipe.IFaultFeedback;

import java.util.List;

public record EFAForgingRecipe(ForgingRecipe recipe, int mode) {

    public List<IFaultFeedback> faultFeedback() {
        return this.recipe.faultFeedback();
    }

    public List<ItemStack> materialsToUpgrade() {
        return this.recipe.materialsToUpgrade();
    }

    public Material material() {
        return this.recipe.material();
    }

    public int levelToUpgrade() {
        return this.recipe.levelToUpgrade();
    }

    public int timeReq() {
        return this.recipe.timeReq();
    }

    public int hammerDurabilityCost() {
        return this.recipe.hammerDurabilityCost();
    }

    public int axeDurabilityCost() {
        return this.recipe.axeDurabilityCost();
    }

    public int chanceOfFailure() {
        return this.recipe.chanceOfFailure();
    }

    public ForgingTableLevel forgingTableLevelReq() {
        return this.recipe.forgingTableLevelReq();
    }

    public EnumQuality qualityReward() {
        return this.recipe.qualityReward();
    }
}
