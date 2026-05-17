package com.inf1nlty.extremeforgingadditions.item;

import net.minecraft.*;
import net.xiaoyu233.mitemod.miteite.item.ArmorModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.MITEITEItemRegistryInit;
import net.xiaoyu233.mitemod.miteite.item.ToolModifierTypes;
import net.xiaoyu233.mitemod.miteite.util.StringUtil;

import java.util.List;

public class ItemForgingTemplate extends Item {

    private final ForgingTemplateType type;

    public ItemForgingTemplate(int id, ForgingTemplateType type) {
        super(id, "forging_template/" + type.textureName());
        this.type = type;
        this.setMaterial(type.material());
        this.addMaterial(Material.hardened_clay);
        this.setMaxStackSize(8);
        this.setCreativeTab(MITEITEItemRegistryInit.tabMITEITE);
        this.setCraftingDifficultyAsComponent(ItemRock.getCraftingDifficultyAsComponent(this.getHardestMetalMaterial()) * 1.5F);
    }

    public int getFailChance() {
        return this.type.failChance();
    }

    public Material getTemplateMaterial() {
        return this.type.material();
    }

    public boolean hasExpAndLevel() {
        return true;
    }

    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean extendedInfo, Slot slot) {
        super.addInformation(itemStack, player, info, extendedInfo, slot);
        info.add(" ");
        info.add(EnumChatFormatting.BLUE + I18n.getString("item.forging_template.desc"));
        if (!extendedInfo || !itemStack.hasTagCompound()) {
            return;
        }

        NBTTagCompound modifiers = itemStack.stackTagCompound.getCompoundTag("modifiers");
        if (modifiers == null || modifiers.hasNoTags()) {
            return;
        }

        info.add(I18n.getString("item.forging_template.modifiers"));
        for (ToolModifierTypes value : ToolModifierTypes.values()) {
            if (modifiers.hasKey(value.nbtName)) {
                info.add("  " + value.color + I18n.getString("modifier.tool." + value.unlocalizedName + ".name") + EnumChatFormatting.RESET + " " + StringUtil.intToRoman(modifiers.getInteger(value.nbtName)));
            }
        }

        for (ArmorModifierTypes value : ArmorModifierTypes.values()) {
            if (modifiers.hasKey(value.nbtName)) {
                info.add("  " + value.color + I18n.getString("modifier.armor." + value.unlocalizedName + ".name") + EnumChatFormatting.RESET + " " + StringUtil.intToRoman(modifiers.getInteger(value.nbtName)));
            }
        }
    }
}
