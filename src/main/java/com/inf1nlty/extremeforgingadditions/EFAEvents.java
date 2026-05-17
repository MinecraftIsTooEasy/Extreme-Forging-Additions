package com.inf1nlty.extremeforgingadditions;

import com.google.common.eventbus.Subscribe;
import com.inf1nlty.extremeforgingadditions.item.EFAItemRegistry;
import com.inf1nlty.extremeforgingadditions.recipe.EFACraftingRecipes;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;

public class EFAEvents {

    @Subscribe
    public void registerItems(ItemRegistryEvent event) {
        EFAItemRegistry.registerItems(event);
    }

    @Subscribe
    public void registerRecipes(RecipeRegistryEvent event) {
        EFACraftingRecipes.registerRecipes(event);
    }
}
