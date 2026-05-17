package com.inf1nlty.extremeforgingadditions.mixin;

import com.inf1nlty.extremeforgingadditions.logic.EFAModifierLevelUpLogic;
import net.minecraft.EntityPlayer;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;
import net.minecraft.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemTool.class, priority = 900)
public abstract class ItemToolLevelUpMixin {

    @Inject(method = "onItemLevelUp", at = @At("HEAD"), cancellable = true)
    private void injectOnItemLevelUp(NBTTagCompound tagCompound, EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        EFAModifierLevelUpLogic.handleItemLevelUp((Item) (Object) this, tagCompound, player, stack);
        ci.cancel();
    }
}
