package com.gtnewhorizon.cropsnh.compatibility.NEI;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.loaders.CropsNHGTRecipeMaps;
import com.gtnewhorizon.cropsnh.loaders.RecipeMapBackends.SeedGeneratorBackend;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.recipe.RecipeCategory;
import gregtech.nei.GTNEIDefaultHandler;

public class CropsNHNEISeedGeneratorHandler extends GTNEIDefaultHandler {

    public CropsNHNEISeedGeneratorHandler(RecipeCategory recipeCategory) {
        super(recipeCategory);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new CropsNHNEISeedGeneratorHandler(recipeCategory);
    }

    @Override
    public void loadUsageRecipes(ItemStack aInput) {
        this.findValidRecipe(aInput, super::loadUsageRecipes);
    }

    @Override
    public void loadCraftingRecipes(ItemStack aResult) {
        this.findValidRecipe(aResult, super::loadCraftingRecipes);
    }

    private void findValidRecipe(ItemStack aStack, Consumer<ItemStack> superCall) {
        ICropCard cc = CropRegistry.instance.get(aStack, false);
        if (cc == null) {
            superCall.accept(aStack);
            return;
        }
        SeedStats seedStats = SeedStats.getStatsFromStack(aStack);
        if (seedStats == null || !seedStats.isAnalyzed()) {
            return;
        }

        // create the rapid lookup table if needed
        SeedGeneratorBackend backend = (SeedGeneratorBackend) recipeMap.getBackend();
        if (!backend.cached) {
            for (CachedDefaultRecipe recipe : getCache()) {
                backend.cropCacheIndex
                    .put(recipe.mRecipe.getMetadata(CropsNHGTRecipeMaps.CROPSNH_CROP_METADATAKEY), recipe);
            }
            backend.cached = true;
        }

        // identify the source recipes
        if (backend.cropCacheIndex.containsKey(cc)) {
            arecipes.addAll(backend.cropCacheIndex.get(cc));
        }
    }
}
