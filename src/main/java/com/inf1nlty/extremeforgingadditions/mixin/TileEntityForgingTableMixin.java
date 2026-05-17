package com.inf1nlty.extremeforgingadditions.mixin;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableModeLogic;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableOperationLogic;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.TileEntityForgingTableAccessor;
import net.minecraft.*;
import net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import net.xiaoyu233.mitemod.miteite.tileentity.TileEntityForgingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TileEntityForgingTable.class)
public abstract class TileEntityForgingTableMixin extends TileEntity implements EFAForgingTable {

    @Unique
    private int efa$forgingMode = EFAForgingTable.MODE_FORGING;
    @Unique
    private ItemStack efa$completionOutputOverride;

    @Shadow
    public abstract void onInventoryChanged();

    @Override
    public int efa$getForgingMode() {
        return this.efa$forgingMode;
    }

    @Override
    public void efa$setForgingMode(int mode) {
        this.efa$forgingMode = EFAForgingTableModeLogic.normalizeMode(mode);
        if (this.worldObj != null && !this.worldObj.isRemote) {
            EFAForgingTableModeLogic.syncMode(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.efa$forgingMode);
            this.onInventoryChanged();
        }
    }

    @Override
    public boolean efa$isForging() {
        return ((TileEntityForgingTableAccessor) this).efa$getIsForging();
    }

    @Inject(method = "openChest", at = @At("RETURN"))
    private void injectSyncModeWhenOpen(CallbackInfo ci) {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            EFAForgingTableModeLogic.syncModeOnOpen(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.efa$forgingMode);
        }
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void injectReadMode(NBTTagCompound nbt, CallbackInfo ci) {
        this.efa$forgingMode = EFAForgingTableModeLogic.readMode(nbt);
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void injectWriteMode(NBTTagCompound nbt, CallbackInfo ci) {
        EFAForgingTableModeLogic.writeMode(nbt, this.efa$forgingMode);
    }

    @Redirect(method = "completeForging", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;setForgingGrade(I)V"))
    private void redirectCompleteForgingMode(ItemStack toolItem, int grade) {
        this.efa$completionOutputOverride = EFAForgingTableOperationLogic.handleCompleteForging((TileEntityForgingTable) (Object) this, toolItem, grade, this.efa$forgingMode);
    }

    @Redirect(method = "completeForging", at = @At(value = "INVOKE", target = "Lnet/xiaoyu233/mitemod/miteite/inventory/container/ForgingTableSlots;setOutput(Lnet/minecraft/ItemStack;)V"))
    private void redirectCompletionOutput(ForgingTableSlots slots, ItemStack output) {
        ItemStack effectiveOutput = EFAForgingTableOperationLogic.resolveCompletionOutput((TileEntityForgingTable) (Object) this, output, this.efa$completionOutputOverride);
        slots.setOutput(effectiveOutput);
        this.efa$completionOutputOverride = null;
    }

    @SuppressWarnings("rawtypes")
    @Redirect(method = "failForging", at = @At(value = "INVOKE", target = "Lnet/xiaoyu233/mitemod/miteite/item/recipe/ForgingRecipe;faultFeedback()Ljava/util/List;"))
    private List redirectUniversalFailure(ForgingRecipe recipe) {
        return EFAForgingTableOperationLogic.redirectUniversalFailure((TileEntityForgingTable) (Object) this, recipe);
    }
}
