package com.gtnewhorizon.cropsnh.loaders;

import java.util.Locale;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.init.CropsNHUITextures;
import com.gtnewhorizon.cropsnh.loaders.RecipeMapBackends.SeedGeneratorBackend;
import com.gtnewhorizon.cropsnh.loaders.RecipeMapFrontends.SeedGeneratorFrontend;
import com.gtnewhorizon.cropsnh.reference.Reference;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;

public class CropsNHGTRecipeMaps {

    public static final RecipeMetadataKey<ICropCard> CROPSNH_CROP_METADATAKEY = SimpleRecipeMetadataKey
        .create(ICropCard.class, "cropsnh_crop");

    public static final RecipeMap<SeedGeneratorBackend> fakeSeedGeneratorRecipes = RecipeMapBuilder
        .of(Reference.MOD_ID.toLowerCase(Locale.ROOT) + ".recipes.seedGenerator", SeedGeneratorBackend::new)
        .maxIO(2, 1, 1, 0)
        .minInputs(1, 1)
        .frontend(SeedGeneratorFrontend::new)
        .disableRegisterNEI()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isOutput && !isFluid && index == 0) {
                return CropsNHUITextures.OVERLAY_SLOT_SEED;
            }
            return null;
        })
        .build();
}
