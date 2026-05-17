package com.inf1nlty.extremeforgingadditions.logic;

import com.inf1nlty.extremeforgingadditions.config.EFAConfig;
import com.inf1nlty.extremeforgingadditions.config.EFAModifierWeightConfigs;
import net.minecraft.ChatMessageComponent;
import net.minecraft.EntityPlayer;
import net.minecraft.Item;
import net.minecraft.ItemArmor;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;
import net.minecraft.NBTTagCompound;
import net.xiaoyu233.mitemod.miteite.api.ITEItem;
import net.xiaoyu233.mitemod.miteite.item.ArmorModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.ItemModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.ModifierUtils;
import net.xiaoyu233.mitemod.miteite.item.ToolModifierTypes;

import java.util.List;
import java.util.Random;

public final class EFAModifierLevelUpLogic {

    private static final String MODIFIER_TAG = "modifiers";

    private EFAModifierLevelUpLogic() {
    }

    public static boolean handleItemLevelUp(Item item, NBTTagCompound tagCompound, EntityPlayer player, ItemStack stack) {
        if (item instanceof ItemTool) {
            applyToolModifiers((ItemTool) item, tagCompound, player, stack);
            return true;
        }

        if (item instanceof ItemArmor) {
            applyArmorModifiers((ItemArmor) item, tagCompound, player, stack);
            player.suppressNextStatIncrement();
            return true;
        }

        return false;
    }

    private static void applyToolModifiers(ItemTool item, NBTTagCompound tagCompound, EntityPlayer player, ItemStack stack) {
        NBTTagCompound modifiers = ensureModifiers(tagCompound);
        Random random = player.getRNG();
        ITEItem upgradableItem = (ITEItem) item;
        int rollCount = getModifierRollCount(random);

        for (int i = 0; i < rollCount; ++i) {
            List<ToolModifierTypes> modifierTypes = ModifierUtils.getAllCanBeAppliedToolModifiers(stack);
            ToolModifierTypes modifierType = EFAModifierWeightConfigs.getToolModifierWithWeight(modifierTypes, random);
            if (modifierType == null) {
                break;
            }

            int level = upgradableItem.addModifierLevelFor(modifiers, modifierType);
            sendModifierMessage(player, stack, modifierType, level);
        }
    }

    private static void applyArmorModifiers(ItemArmor item, NBTTagCompound tagCompound, EntityPlayer player, ItemStack stack) {
        NBTTagCompound modifiers = ensureModifiers(tagCompound);
        Random random = player.getRNG();
        ITEItem upgradableItem = (ITEItem) item;
        int rollCount = getModifierRollCount(random);

        for (int i = 0; i < rollCount; ++i) {
            List<ArmorModifierTypes> modifierTypes = ModifierUtils.getAllCanBeAppliedArmorModifiers(stack);
            ArmorModifierTypes modifierType = EFAModifierWeightConfigs.getArmorModifierWithWeight(modifierTypes, random);
            if (modifierType == null) {
                break;
            }

            int level = upgradableItem.addModifierLevelFor(modifiers, modifierType);
            sendModifierMessage(player, stack, modifierType, level);
        }
    }

    private static int getModifierRollCount(Random random) {
        int oneWeight = Math.max(0, EFAConfig.MODIFIER_ROLL_COUNT_ONE_WEIGHT.get());
        int twoWeight = Math.max(0, EFAConfig.MODIFIER_ROLL_COUNT_TWO_WEIGHT.get());
        int threeWeight = Math.max(0, EFAConfig.MODIFIER_ROLL_COUNT_THREE_WEIGHT.get());
        int fourWeight = Math.max(0, EFAConfig.MODIFIER_ROLL_COUNT_FOUR_WEIGHT.get());
        int totalWeight = oneWeight + twoWeight + threeWeight + fourWeight;
        if (totalWeight <= 0) {
            return 1;
        }

        int selected = random.nextInt(totalWeight);
        if (selected < oneWeight) {
            return 1;
        }
        selected -= oneWeight;
        if (selected < twoWeight) {
            return 2;
        }
        selected -= twoWeight;
        return selected < threeWeight ? 3 : 4;
    }

    private static <T extends ItemModifierTypes> void sendModifierMessage(EntityPlayer player, ItemStack stack, T modifierType, int level) {
        if (level > 1) {
            player.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(
                    "miteite.msg.modifier.level",
                    ChatMessageComponent.createFromTranslationKey(stack.getUnlocalizedName() + ".name"),
                    asDisplayName(modifierType),
                    level
            ));
        } else {
            player.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(
                    "miteite.msg.modifier.new",
                    ChatMessageComponent.createFromTranslationKey(stack.getUnlocalizedName() + ".name"),
                    asDisplayName(modifierType)
            ));
        }
    }

    private static ChatMessageComponent asDisplayName(ItemModifierTypes modifierType) {
        if (modifierType instanceof ToolModifierTypes toolModifierTypes) {
            return toolModifierTypes.getDisplayName().setColor(toolModifierTypes.color);
        }

        if (modifierType instanceof ArmorModifierTypes armorModifierTypes) {
            return armorModifierTypes.getDisplayName().setColor(armorModifierTypes.color);
        }

        return ChatMessageComponent.createFromText(modifierType.getNbtName());
    }

    private static NBTTagCompound ensureModifiers(NBTTagCompound tagCompound) {
        if (!tagCompound.hasKey(MODIFIER_TAG)) {
            tagCompound.setCompoundTag(MODIFIER_TAG, new NBTTagCompound());
        }

        return tagCompound.getCompoundTag(MODIFIER_TAG);
    }
}
