package com.inf1nlty.extremeforgingadditions;

import com.inf1nlty.extremeforgingadditions.config.EFAConfig;
import com.inf1nlty.extremeforgingadditions.network.EFAPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import fi.dy.masa.malilib.config.ConfigManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;

public class ExtremeForgingAdditionsMod implements ModInitializer {

    public static final String MOD_ID = "extreme-forging-additions";
    public static final String MOD_NAME = "Extreme Forging Additions";

    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        EFAConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(EFAConfig.getInstance());
        MITEEvents.MITE_EVENT_BUS.register(new EFAEvents());
        EFAPacketHandler.init();
    }
}
