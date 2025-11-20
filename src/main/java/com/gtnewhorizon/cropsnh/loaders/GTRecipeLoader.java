package com.gtnewhorizon.cropsnh.loaders;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.handler.ConfigurationHandler;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.BaseGTRecipeLoader;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropRecipes;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropsPlusPlusRecipes;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.FertilizerRecipes;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public abstract class GTRecipeLoader extends BaseGTRecipeLoader {

    public static void PostInit() {
        CropRecipes.postInit();
        FertilizerRecipes.postInit();
        CropsPlusPlusRecipes.postInit();

        addPlantLensRecipe();
        AddSpadeRecipes();
        AddCropStickRecipes();
        AddCropManagerRecipes();
        AddSeedGeneratorRecipes();
        AddNanCertificateRecipe();
    }

    private static void addPlantLensRecipe() {
        GTModHandler.addCraftingRecipe(
            CropsNHItemList.spade.get(1),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { " fL", " Sr", "S  ", 'L', OrePrefixes.lens.get(Materials.Glass), 'S', "stickWood" });
    }

    private static void AddSpadeRecipes() {
        GTModHandler.addCraftingRecipe(
            CropsNHItemList.spade.get(1),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "fPh", "PHP", " S ", 'P', OrePrefixes.plateDense.get(Materials.Steel), 'H', "iron_shovel",
                'S', OrePrefixes.stickLong.get(Materials.Wood) });
    }

    private static void AddCropStickRecipes() {
        // TODO: REMOVE OLD CROP STICK RECIPES FROM GT5U

        GTModHandler.addCraftingRecipe(
            new ItemStack(CropsNHBlocks.blockCrop, ConfigurationHandler.cropsPerCraft),
            GTModHandler.RecipeBits.BITS,
            new Object[] { "S S", "S S", 'S', "stickWood" });

        ulvRecipe(2, 0)
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 8))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, ConfigurationHandler.cropsPerCraft * 4L))
            .addTo(RecipeMaps.assemblerRecipes);
    }

    private static void AddCropManagerRecipes() {
        for (int tier = 0; tier < CROP_MANAGERS.length; tier++) {
            GTModHandler.addMachineCraftingRecipe(
                CROP_MANAGERS[tier].get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "ASA",
                    "PHP",
                    "CIC",
                    'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'S', MTEBasicMachineWithRecipe.X.SENSOR,
                    'P', MTEBasicMachineWithRecipe.X.PLATE,
                    'H', MTEBasicMachineWithRecipe.X.HULL,
                    'C', MTEBasicMachineWithRecipe.X.CIRCUIT,
                    'I', INPUT_HATCHES[tier]
                    // spotless:on
                },
                tier + 1);
        }
    }

    private static void AddSeedGeneratorRecipes() {
        for (int tier = 0; tier < SEED_GENERATOR.length; tier++) {
            GTModHandler.addMachineCraftingRecipe(
                SEED_GENERATOR[tier].get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "SAW",
                    "CHC",
                    "BDB",
                    'S', CropsNHItemList.spade.get(1),
                    'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'W', Mods.ExtraUtilities.isModLoaded() ? new ItemStack(Block.getBlockFromName("ExtraUtilities:watering_can"), 1) : ItemList.Cell_Water.get(1L),
                    'C', MTEBasicMachineWithRecipe.X.CIRCUIT,
                    'H', MTEBasicMachineWithRecipe.X.HULL,
                    'B', MTEBasicMachineWithRecipe.X.WIRE,
                    // maybe consider replacing this with a seedbed later on.
                    'D', new ItemStack(Mods.RandomThings.isModLoaded() ? Block.getBlockFromName("RandomThings:fertilizedDirt") : Blocks.dirt, 1)
                    // spotless:on
                },
                tier + 1);
        }
    }

    private static void AddNanCertificateRecipe() {
        // NAN Certificate recipe
        recipe(8, 29826 * 3600 + 9 * 60 + 7, 35)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64))
            .itemOutputs(CropsNHItemList.nanCertificate.get(1))
            .addTo(extruderRecipes);
    }

    // region tier item lists

    private static final CropsNHItemList[] CROP_MANAGERS = new CropsNHItemList[] { CropsNHItemList.CropManager_LV,
        CropsNHItemList.CropManager_MV, CropsNHItemList.CropManager_HV, CropsNHItemList.CropManager_EV,
        CropsNHItemList.CropManager_IV, CropsNHItemList.CropManager_LuV, CropsNHItemList.CropManager_ZPM,
        CropsNHItemList.CropManager_UV, CropsNHItemList.CropManager_UHV, CropsNHItemList.CropManager_UEV,
        CropsNHItemList.CropManager_UIV, CropsNHItemList.CropManager_UMV };

    private static final CropsNHItemList[] SEED_GENERATOR = new CropsNHItemList[] { CropsNHItemList.SeedGenerator_LV,
        CropsNHItemList.SeedGenerator_MV, CropsNHItemList.SeedGenerator_HV, CropsNHItemList.SeedGenerator_EV,
        CropsNHItemList.SeedGenerator_IV, CropsNHItemList.SeedGenerator_LuV, CropsNHItemList.SeedGenerator_ZPM,
        CropsNHItemList.SeedGenerator_UV, CropsNHItemList.SeedGenerator_UHV, CropsNHItemList.SeedGenerator_UEV,
        CropsNHItemList.SeedGenerator_UIV, CropsNHItemList.SeedGenerator_UMV };

    // I should probably PR that capability into the MTEBasicMachineWithRecipe thing
    private static final ItemList[] INPUT_HATCHES = new ItemList[] { ItemList.Hatch_Input_LV, ItemList.Hatch_Input_MV,
        ItemList.Hatch_Input_HV, ItemList.Hatch_Input_EV, ItemList.Hatch_Input_IV, ItemList.Hatch_Input_LuV,
        ItemList.Hatch_Input_ZPM, ItemList.Hatch_Input_UV, ItemList.Hatch_Input_UHV, ItemList.Hatch_Input_UEV,
        ItemList.Hatch_Input_UIV, ItemList.Hatch_Input_UMV };

    // endregion tier item lists
}
