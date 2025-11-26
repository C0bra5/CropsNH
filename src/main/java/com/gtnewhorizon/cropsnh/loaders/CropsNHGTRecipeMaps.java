package com.gtnewhorizon.cropsnh.loaders;

import java.util.Locale;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ICropMutation;
import com.gtnewhorizon.cropsnh.init.CropsNHUITextures;
import com.gtnewhorizon.cropsnh.recipes.backends.CropBreederBackend;
import com.gtnewhorizon.cropsnh.recipes.backends.CropGeneExtractorBackend;
import com.gtnewhorizon.cropsnh.recipes.backends.SeedGeneratorBackend;
import com.gtnewhorizon.cropsnh.recipes.frontends.CropBreederFrontend;
import com.gtnewhorizon.cropsnh.recipes.frontends.CropGeneExtractorFrontend;
import com.gtnewhorizon.cropsnh.recipes.frontends.SeedGeneratorFrontend;
import com.gtnewhorizon.cropsnh.reference.Reference;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;

public class CropsNHGTRecipeMaps {

    public static final RecipeMetadataKey<ICropCard> CROPSNH_CROP_METADATAKEY = SimpleRecipeMetadataKey
        .create(ICropCard.class, "cropsnh_crop");
    public static final RecipeMetadataKey<ICropMutation> CROPSNH_CROP_MUTATION_METADATAKEY = SimpleRecipeMetadataKey
        .create(ICropMutation.class, "cropsnh_crop_mutation");

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

    public static final RecipeMap<CropBreederBackend> fakeCropBreederRecipeMap = RecipeMapBuilder
        .of(Reference.MOD_ID.toLowerCase(Locale.ROOT) + ".recipes.cropBreeder", CropBreederBackend::new)
        .maxIO(6, 1, 1, 0)
        .minInputs(2, 1)
        .frontend(CropBreederFrontend::new)
        .disableRegisterNEI()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput && !isFluid && index == 0) {
                return CropsNHUITextures.OVERLAY_SLOT_SEED;
            }
            return null;
        })
        .build();

    public static final RecipeMap<CropGeneExtractorBackend> fakeCropGeneExtractorRecipeMap = RecipeMapBuilder
        .of(Reference.MOD_ID.toLowerCase(Locale.ROOT) + ".recipes.cropGeneExtractor", CropGeneExtractorBackend::new)
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .useSpecialSlot()
        .frontend(CropGeneExtractorFrontend::new)
        .disableRegisterNEI()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isOutput && !isFluid && index == 0) {
                return CropsNHUITextures.OVERLAY_SLOT_SEED;
            }
            if (isOutput && !isFluid && index == 0) {
                return GTUITextures.OVERLAY_SLOT_DATA_ORB;
            }
            return null;
        })
        .build();
}
