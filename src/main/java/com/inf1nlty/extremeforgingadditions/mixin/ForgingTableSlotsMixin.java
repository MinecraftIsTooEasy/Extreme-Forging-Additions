package com.inf1nlty.extremeforgingadditions.mixin;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingSlots;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableSlotsLogic;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import java.util.List;

@Mixin(ForgingTableSlots.class)
public abstract class ForgingTableSlotsMixin implements EFAForgingSlots {

    @Shadow
    public abstract ItemStack getToolItem();

    @Redirect(method = "getRecipeFromTool", at = @At(value = "INVOKE", target = "Lnet/xiaoyu233/mitemod/miteite/item/recipe/ForgingTableRecipes;getRecipe(Lnet/minecraft/Material;I)Lnet/xiaoyu233/mitemod/miteite/item/recipe/ForgingRecipe;"))
    private ForgingRecipe redirectModeRecipeLookup(Material material, int forgingLevel) {
        return EFAForgingTableSlotsLogic.redirectModeRecipeLookup((ForgingTableSlots) (Object) this, material, forgingLevel);
    }

    @Inject(method = "getNeedItems", at = @At("HEAD"), cancellable = true)
    private void injectTemplateAwareNeedItems(@Nonnull ForgingRecipe recipe, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> needItems = EFAForgingTableSlotsLogic.getNeedItems((ForgingTableSlots) (Object) this, recipe);
        if (needItems != null) {
            cir.setReturnValue(needItems);
        }
    }

    @Inject(method = "costItems", at = @At("HEAD"), cancellable = true)
    private void injectTemplateAwareCostItems(ForgingRecipe recipe, CallbackInfo ci) {
        if (EFAForgingTableSlotsLogic.costItems((ForgingTableSlots) (Object) this, recipe)) {
            ci.cancel();
        }
    }

    @Inject(method = "getChanceOfFailure", at = @At("RETURN"), cancellable = true)
    private void injectExtendedModeFailureChance(@Nonnull ForgingRecipe recipe, CallbackInfoReturnable<Integer> cir) {
        Integer failureChance = EFAForgingTableSlotsLogic.getExtendedModeFailureChance((ForgingTableSlots) (Object) this, recipe);
        if (failureChance != null) {
            cir.setReturnValue(failureChance);
        }
    }

    @Override
    public ItemStack efa$getTemplate() {
        return EFAForgingTableSlotsLogic.getTemplate((ForgingTableSlots) (Object) this);
    }

    @Override
    public boolean efa$hasAndConsumeUniversalEnhanceStone() {
        return EFAForgingTableSlotsLogic.consumeUniversalEnhanceStone((ForgingTableSlots) (Object) this);
    }
}
