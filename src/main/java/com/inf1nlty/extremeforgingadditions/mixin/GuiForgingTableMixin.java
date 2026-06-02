package com.inf1nlty.extremeforgingadditions.mixin;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTable;
import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableGui;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableGuiLogic;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableModeLogic;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.ContainerForgingTablePositionAccessor;
import net.minecraft.*;
import net.xiaoyu233.mitemod.miteite.gui.GuiForgingTable;
import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiForgingTable.class)
public abstract class GuiForgingTableMixin extends GuiContainer implements EFAForgingTableGui {

    @Shadow
    @Final
    private EntityPlayer player;
    @Shadow
    private GuiButton startButton;

    @Unique
    private GuiButton efa$modeButton;
    @Unique
    private boolean efa$requestedInitialMode;
    @Unique
    private int efa$blockX;
    @Unique
    private int efa$blockY;
    @Unique
    private int efa$blockZ;
    @Unique
    private int efa$modeIndex = EFAForgingTable.MODE_FORGING;

    private GuiForgingTableMixin(Container container) {
        super(container);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCoordinates(EntityPlayer player, int x, int y, int z, net.xiaoyu233.mitemod.miteite.inventory.container.ForgingTableSlots slots, CallbackInfo ci) {
        this.efa$blockX = x;
        this.efa$blockY = y;
        this.efa$blockZ = z;
        this.efa$refreshBlockCoordinates();
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("RETURN"))
    private void injectModeButton(CallbackInfo ci) {
        this.efa$refreshBlockCoordinates();
        EFAForgingTableGuiLogic.moveStartButton(this.startButton, this.width, this.height);
        this.efa$modeButton = EFAForgingTableGuiLogic.createModeButton(this.efa$modeButton, this.width, this.height);
        this.buttonList.add(this.efa$modeButton);
        if (!this.efa$requestedInitialMode) {
            this.efa$requestedInitialMode = true;
            EFAForgingTableGuiLogic.requestModeState(this.efa$blockX, this.efa$blockY, this.efa$blockZ);
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("RETURN"))
    private void injectModeText(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        int x = (this.width + this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawString(this.mc.fontRenderer, EFAForgingTableGuiLogic.getModeInfoText(this.efa$modeIndex), x, y, 0xFFFFFF);
    }

    @Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/xiaoyu233/mitemod/miteite/gui/GuiForgingTable;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V"))
    private void shiftForgingInfoDown(GuiForgingTable gui, FontRenderer fontRenderer, String text, int x, int y, int color) {
        int infoX = (this.width + this.xSize) / 2;
        int modeLineY = (this.height - this.ySize) / 2;
        if (x == infoX && y >= modeLineY) {
            y += 10;
        }
        this.drawString(fontRenderer, text, x, y, color);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void injectModeButtonAction(GuiButton button, CallbackInfo ci) {
        this.efa$refreshBlockCoordinates();
        if (EFAForgingTableGuiLogic.handleModeButtonAction(button, this.efa$modeButton, this.player, this.efa$blockX, this.efa$blockY, this.efa$blockZ)) {
            ci.cancel();
        }
    }

    @Inject(method = "enableButton", at = @At("RETURN"))
    private void injectEnableModeButton(CallbackInfo ci) {
        EFAForgingTableGuiLogic.enableModeButton(this.efa$modeButton);
    }

    @Override
    public int efa$getBlockX() {
        return this.efa$blockX;
    }

    @Override
    public int efa$getBlockY() {
        return this.efa$blockY;
    }

    @Override
    public int efa$getBlockZ() {
        return this.efa$blockZ;
    }

    @Override
    public void efa$refreshBlockCoordinates() {
        if (this.inventorySlots instanceof ContainerForgingTable container) {
            ContainerForgingTablePositionAccessor accessor = (ContainerForgingTablePositionAccessor) container;
            this.efa$blockX = accessor.efa$getBlockX();
            this.efa$blockY = accessor.efa$getBlockY();
            this.efa$blockZ = accessor.efa$getBlockZ();
        }
    }

    @Override
    public boolean efa$isAtBlock(int x, int y, int z) {
        this.efa$refreshBlockCoordinates();
        return this.efa$blockX == x && this.efa$blockY == y && this.efa$blockZ == z;
    }

    @Override
    public void efa$setBlockMode(int mode) {
        this.efa$refreshBlockCoordinates();
        this.efa$modeIndex = EFAForgingTableModeLogic.normalizeMode(mode);
        EFAForgingTableGuiLogic.applyModeToTile(this.mc, this.efa$blockX, this.efa$blockY, this.efa$blockZ, this.efa$modeIndex);
    }
}
