package com.inf1nlty.extremeforgingadditions.config;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.ArrayList;
import java.util.List;

public class EFAConfig extends SimpleConfigs {

    public static final ConfigInteger MODIFIER_ROLL_COUNT_ONE_WEIGHT = new ConfigInteger(
            "extreme-forging-additions.modifierRollCountOneWeight", 1, 0, 10000,
            "Weight used when drawing one modifier gain on level-up");
    public static final ConfigInteger MODIFIER_ROLL_COUNT_TWO_WEIGHT = new ConfigInteger(
            "extreme-forging-additions.modifierRollCountTwoWeight", 0, 0, 10000,
            "Weight used when drawing two modifier gains on level-up");
    public static final ConfigInteger MODIFIER_ROLL_COUNT_THREE_WEIGHT = new ConfigInteger(
            "extreme-forging-additions.modifierRollCountThreeWeight", 0, 0, 10000,
            "Weight used when drawing three modifier gains on level-up");
    public static final ConfigInteger MODIFIER_ROLL_COUNT_FOUR_WEIGHT = new ConfigInteger(
            "extreme-forging-additions.modifierRollCountFourWeight", 0, 0, 10000,
            "Weight used when drawing four modifier gains on level-up");

    private static final EFAConfig INSTANCE;

    public static final List<ConfigBase<?>> VALUES;
    public static final List<ConfigBase<?>> ROLL_COUNT_VALUES;
    public static final List<ConfigHotkey> HOTKEYS;
    public static final List<ConfigTab> TABS;

    public EFAConfig(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values);
    }

    public static EFAConfig getInstance() {
        return INSTANCE;
    }

    static {
        VALUES = new ArrayList<>();
        ROLL_COUNT_VALUES = new ArrayList<>();
        HOTKEYS = List.of();
        TABS = new ArrayList<>();

        ROLL_COUNT_VALUES.add(MODIFIER_ROLL_COUNT_ONE_WEIGHT);
        ROLL_COUNT_VALUES.add(MODIFIER_ROLL_COUNT_TWO_WEIGHT);
        ROLL_COUNT_VALUES.add(MODIFIER_ROLL_COUNT_THREE_WEIGHT);
        ROLL_COUNT_VALUES.add(MODIFIER_ROLL_COUNT_FOUR_WEIGHT);

        VALUES.addAll(ROLL_COUNT_VALUES);
        VALUES.addAll(EFAModifierWeightConfigs.getToolValues());
        VALUES.addAll(EFAModifierWeightConfigs.getArmorValues());

        TABS.add(new ConfigTab("modifier_gain_count", ROLL_COUNT_VALUES));
        TABS.add(new ConfigTab("tool_modifier_weights", EFAModifierWeightConfigs.getToolValues()));
        TABS.add(new ConfigTab("armor_modifier_weights", EFAModifierWeightConfigs.getArmorValues()));

        INSTANCE = new EFAConfig("ExtremeForgingAdditions", HOTKEYS, VALUES);
    }

    @Override
    public List<ConfigTab> getConfigTabs() {
        return TABS;
    }
}
