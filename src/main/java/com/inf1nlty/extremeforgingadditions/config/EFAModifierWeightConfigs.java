package com.inf1nlty.extremeforgingadditions.config;

import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.xiaoyu233.mitemod.miteite.item.ArmorModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.ItemModifierTypes;
import net.xiaoyu233.mitemod.miteite.item.ToolModifierTypes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.ToIntFunction;

public final class EFAModifierWeightConfigs {

    private static final String TOOL_WEIGHT_PREFIX = "extreme-forging-additions.toolModifierWeight.";
    private static final String ARMOR_WEIGHT_PREFIX = "extreme-forging-additions.armorModifierWeight.";

    private static final List<ConfigBase<?>> TOOL_VALUES = new ArrayList<>();
    private static final List<ConfigBase<?>> ARMOR_VALUES = new ArrayList<>();
    private static final Map<String, ConfigInteger> TOOL_WEIGHTS = new LinkedHashMap<>();
    private static final Map<String, ConfigInteger> ARMOR_WEIGHTS = new LinkedHashMap<>();

    private EFAModifierWeightConfigs() {
    }

    static {
        loadOptionalModifierClasses();

        for (ToolModifierTypes modifierType : ToolModifierTypes.values()) {
            registerToolModifierWeight(modifierType);
        }

        for (ArmorModifierTypes modifierType : ArmorModifierTypes.values()) {
            registerArmorModifierWeight(modifierType);
        }
    }

    private static void loadOptionalModifierClasses() {
        loadOptionalModifierClass("cn.wensc.mitemod.extreme.register.EXArmorModifierTypes");
    }

    private static void loadOptionalModifierClass(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException | LinkageError ignored) {
        }
    }

    public static List<ConfigBase<?>> getToolValues() {
        return TOOL_VALUES;
    }

    public static List<ConfigBase<?>> getArmorValues() {
        return ARMOR_VALUES;
    }

    public static ToolModifierTypes getToolModifierWithWeight(List<ToolModifierTypes> modifierTypes, Random random) {
        return getModifierWithWeight(modifierTypes, random, EFAModifierWeightConfigs::getToolWeight);
    }

    public static ArmorModifierTypes getArmorModifierWithWeight(List<ArmorModifierTypes> modifierTypes, Random random) {
        return getModifierWithWeight(modifierTypes, random, EFAModifierWeightConfigs::getArmorWeight);
    }

    private static void registerToolModifierWeight(ToolModifierTypes modifierType) {
        String key = modifierType.getNbtName();
        ConfigInteger config = new ConfigInteger(
                TOOL_WEIGHT_PREFIX + key,
                getDefaultWeight(modifierType),
                0,
                10000,
                "Draw weight for this tool modifier on level-up");
        TOOL_WEIGHTS.put(key, config);
        TOOL_VALUES.add(config);
    }

    private static void registerArmorModifierWeight(ArmorModifierTypes modifierType) {
        String key = modifierType.getNbtName();
        ConfigInteger config = new ConfigInteger(
                ARMOR_WEIGHT_PREFIX + key,
                getDefaultWeight(modifierType),
                0,
                10000,
                "Draw weight for this armor modifier on level-up");
        ARMOR_WEIGHTS.put(key, config);
        ARMOR_VALUES.add(config);
    }

    private static int getToolWeight(ToolModifierTypes modifierType) {
        ConfigInteger config = TOOL_WEIGHTS.get(modifierType.getNbtName());
        return config == null ? getDefaultWeight(modifierType) : Math.max(0, config.get());
    }

    private static int getArmorWeight(ArmorModifierTypes modifierType) {
        ConfigInteger config = ARMOR_WEIGHTS.get(modifierType.getNbtName());
        return config == null ? getDefaultWeight(modifierType) : Math.max(0, config.get());
    }

    private static int getDefaultWeight(ItemModifierTypes modifierType) {
        return Math.max(0, Math.round(modifierType.getWeight()));
    }

    private static <T extends ItemModifierTypes> T getModifierWithWeight(List<T> modifierTypes, Random random, ToIntFunction<T> weightGetter) {
        int totalWeight = 0;
        for (T modifierType : modifierTypes) {
            totalWeight += Math.max(0, weightGetter.applyAsInt(modifierType));
        }

        if (totalWeight <= 0) {
            return null;
        }

        int selectedWeight = random.nextInt(totalWeight);
        for (T modifierType : modifierTypes) {
            selectedWeight -= Math.max(0, weightGetter.applyAsInt(modifierType));
            if (selectedWeight < 0) {
                return modifierType;
            }
        }

        return null;
    }
}
