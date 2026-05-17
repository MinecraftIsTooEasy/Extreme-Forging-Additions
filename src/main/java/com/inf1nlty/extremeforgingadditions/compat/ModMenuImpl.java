package com.inf1nlty.extremeforgingadditions.compat;

import com.inf1nlty.extremeforgingadditions.config.EFAConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> EFAConfig.getInstance().getConfigScreen(parent);
    }
}
