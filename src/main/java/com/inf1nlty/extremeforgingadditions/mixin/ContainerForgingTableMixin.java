package com.inf1nlty.extremeforgingadditions.mixin;

import com.inf1nlty.extremeforgingadditions.api.EFAForgingTableContainer;
import com.inf1nlty.extremeforgingadditions.logic.EFAForgingTableContainerLogic;
import com.inf1nlty.extremeforgingadditions.mixin.accessor.ContainerForgingTableAccessor;
import net.xiaoyu233.mitemod.miteite.inventory.container.ContainerForgingTable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ContainerForgingTable.class)
public class ContainerForgingTableMixin implements EFAForgingTableContainer {

    @Override
    public void efa$switchMode() {
        EFAForgingTableContainerLogic.switchMode(((ContainerForgingTableAccessor) this).efa$getTileEntity());
    }
}
