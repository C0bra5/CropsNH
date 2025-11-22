package com.gtnewhorizon.cropsnh.loaders.RecipeMapBackends;

import net.minecraft.item.ItemStack;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.loaders.CropsNHGTRecipeMaps;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;

public class SeedGeneratorBackend extends RecipeMapBackend {

    public final SetMultimap<ICropCard, GTRecipe> cropIndex = LinkedHashMultimap.create();
    public final SetMultimap<ICropCard, GTNEIDefaultHandler.CachedDefaultRecipe> cropCacheIndex = LinkedHashMultimap
        .create();

    public SeedGeneratorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe addToItemMap(GTRecipe recipe) {
        ICropCard cc = recipe.getMetadata(CropsNHGTRecipeMaps.CROPSNH_CROP_METADATAKEY);
        cropIndex.put(cc, recipe);
        return super.addToItemMap(recipe);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        ICropCard cc = CropRegistry.instance.get(item, false);
        return (cc != null && cropIndex.containsKey(cc)) || super.containsInput(item);
    }
}
