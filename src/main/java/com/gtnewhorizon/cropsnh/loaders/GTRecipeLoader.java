package com.gtnewhorizon.cropsnh.loaders;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.BlockAdvancedHarvestingUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockEnvironmentalEnhancementUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockFertilizerUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockOverclockedGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockSeedBed;
import com.gtnewhorizon.cropsnh.handler.ConfigurationHandler;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.BaseGTRecipeLoader;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropBreederFakeRecipeLoader;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropGeneExtractorFakeRecipeLoader;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropRecipes;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropSynthesizerFakeRecipeLoader;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.CropsPlusPlusRecipes;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.FertilizerRecipes;
import com.gtnewhorizon.cropsnh.loaders.gtrecipes.SeedGeneratorFakeRecipeLoader;
import com.gtnewhorizon.cropsnh.utility.ModUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.VoltageIndex;
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
        SeedGeneratorFakeRecipeLoader.postInit();
        CropBreederFakeRecipeLoader.postInit();
        CropGeneExtractorFakeRecipeLoader.postInit();
        CropSynthesizerFakeRecipeLoader.postInit();

        addPlantLensRecipe();
        addSpadeRecipes();
        addCropStickRecipes();
        addPlantCureRecipe();
        addCropManagerRecipes();
        addSeedGeneratorRecipes();
        addCropBreederRecipes();
        addCropGeneExtractorRecipes();
        addCropSynthesizerRecipes();
        addIndustrialFarmRecipe();
        addSeedBedRecipes();
        addAdvHarvestingUnitRecipes();
        addEnvironmentalEnhancementUnitRecipes();
        addFertilizerUnitRecipes();
        addGrowthAccelerationUnits();
        addOverclockedGrowthAccelerationUnits();
        addNanCertificateRecipe();
    }

    private static void addPlantLensRecipe() {
        GTModHandler.addCraftingRecipe(
            CropsNHItemList.spade.get(1),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { " fL", " Sr", "S  ", 'L', OrePrefixes.lens.get(Materials.Glass), 'S', "stickWood" });
    }

    private static void addSpadeRecipes() {
        GTModHandler.addCraftingRecipe(
            CropsNHItemList.spade.get(1),
            GTModHandler.RecipeBits.BITSD,
            new Object[] { "fPh", "PHP", " S ", 'P', OrePrefixes.plateDense.get(Materials.Steel), 'H', "iron_shovel",
                'S', OrePrefixes.stickLong.get(Materials.Wood) });
    }

    public static void addPlantCureRecipe() {
        GTModHandler.addShapelessCraftingRecipe(
            CropsNHItemList.plantCure.get(1),
            new Object[] { ItemList.Spray_Empty.get(1), CropsNHItemList.enrichedFertilizerCell.get(1),
                CropsNHItemList.fertilizer.get(1), new ItemStack(Items.dye, 1, 15) });
    }

    private static void addCropStickRecipes() {
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

    private static void addCropManagerRecipes() {
        int tier = VoltageIndex.LV;
        for (int i = 0; i < CROP_MANAGERS.length; i++) {
            GTModHandler.addMachineCraftingRecipe(
                CROP_MANAGERS[i].get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "ASA",
                    "PHP",
                    "CIC",
                    'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'S', MTEBasicMachineWithRecipe.X.SENSOR,
                    'P', getPlate(tier),
                    'H', getHull(tier),
                    'C', getCable(tier),
                    'I', getInputHatch(i)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addSeedGeneratorRecipes() {
        int tier = VoltageIndex.LV;
        for (CropsNHItemList cropsNHItemList : SEED_GENERATOR) {
            GTModHandler.addMachineCraftingRecipe(
                cropsNHItemList.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "SAW",
                    "CHC",
                    "BDB",
                    'S', CropsNHItemList.spade.get(1),
                    'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'W', getWateringCan(),
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'B', getCable(tier),
                    // maybe consider replacing this with a tired seedbed later on.
                    'D', new ItemStack(Mods.RandomThings.isModLoaded() ? Block.getBlockFromName("RandomThings:fertilizedDirt") : Blocks.dirt, 1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addCropBreederRecipes() {
        int tier = VoltageIndex.LV;
        for (CropsNHItemList cropsNHItemList : CROP_BREEDER) {
            GTModHandler.addMachineCraftingRecipe(
                cropsNHItemList.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "SAS",
                    "CHC",
                    "WDW",
                    'S', CropsNHItemList.cropSticks.get(1),
                    'A', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'W', getCable(tier),
                    // maybe consider replacing this with a tired seedbed later on.
                    'D', new ItemStack(Mods.RandomThings.isModLoaded() ? Block.getBlockFromName("RandomThings:fertilizedDirt") : Blocks.dirt, 1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addCropGeneExtractorRecipes() {
        int tier = VoltageIndex.EV;
        String topLine = "SES";
        String bottomLine = "CSC";
        for (CropsNHItemList geneExtractor : CROP_GENE_EXTRACTORS) {
            GTModHandler.addMachineCraftingRecipe(
                geneExtractor.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    // TODO: remove this anti-collision stuff when crops++ gets the boot
                    Mods.CropsPlusPlus.isModLoaded() ? bottomLine : topLine,
                    "WHW",
                    Mods.CropsPlusPlus.isModLoaded() ? topLine : bottomLine,
                    'S', MTEBasicMachineWithRecipe.X.SENSOR,
                    'E', MTEBasicMachineWithRecipe.X.EMITTER,
                    'W', getCable(tier),
                    'H', getHull(tier),
                    'C', getBetterCircuit(tier),
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addCropSynthesizerRecipes() {
        int tier = VoltageIndex.EV;
        String topLine = "FCF";
        String bottomLine = "EEE";
        for (CropsNHItemList cropSynthesizer : CROP_SYNTHESIZERS) {
            GTModHandler.addMachineCraftingRecipe(
                cropSynthesizer.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    // TODO: remove this anti-collision stuff when crops++ gets the boot
                    Mods.CropsPlusPlus.isModLoaded() ? bottomLine : topLine,
                    "CHC",
                    Mods.CropsPlusPlus.isModLoaded() ? topLine : bottomLine,
                    'E', MTEBasicMachineWithRecipe.X.EMITTER,
                    'C', getBetterCircuit(tier),
                    'H', getHull(tier),
                    'F', MTEBasicMachineWithRecipe.X.FIELD_GENERATOR,
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addIndustrialFarmRecipe() {

        GTModHandler.addMachineCraftingRecipe(
            CropsNHItemList.IndustrialFarmController.get(1),
            GTModHandler.RecipeBits.BITSD,
            new Object[] {
                // TODO: remove this anti-collision stuff when crops++ gets the boot
                "PWS", "CMC", "ICI", 'P', MTEBasicMachineWithRecipe.X.ROBOT_ARM, 'W', getWateringCan(), 'S',
                MTEBasicMachineWithRecipe.X.SENSOR, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'M',
                CropsNHItemList.CropManager_MV.get(1),
                // seed/block under storage
                'I', ItemList.Hatch_Input_Bus_MV
            // spotless:on
            },
            VoltageIndex.MV);
    }

    private static void addSeedBedRecipes() {
        int tier = BlockSeedBed.MIN_TIER;
        ItemStack dirt = new ItemStack(
            Mods.RandomThings.isModLoaded() ? Block.getBlockFromName("RandomThings:fertilizedDirt") : Blocks.dirt,
            1);
        for (CropsNHItemList output : SEED_BEDS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // TODO: remove this anti-collision stuff when crops++ gets the boot
                    "DDD", "CHC", "PIP", 'D', dirt, 'C', getCircuit(tier), 'H', getHull(tier), 'P', getPipe(tier), 'I',
                    getInputHatch(tier),
                // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addAdvHarvestingUnitRecipes() {
        int tier = BlockAdvancedHarvestingUnit.MIN_TIER;
        for (CropsNHItemList output : ADV_HARVESTING_UNITS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "RSR",
                    "CHC",
                    "WBW",
                    'R', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'S', MTEBasicMachineWithRecipe.X.SENSOR,
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'W', getCable(tier),
                    'B', CropsNHItemList.BrickedAgriculturalCasing.get(1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addEnvironmentalEnhancementUnitRecipes() {
        int tier = BlockEnvironmentalEnhancementUnit.MIN_TIER;
        for (CropsNHItemList output : ENVIRONMENTAL_ENHANCEMENT_UNITS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "EEE",
                    "CHC",
                    "WBW",
                    'E', MTEBasicMachineWithRecipe.X.EMITTER,
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'W', getCable(tier),
                    'B', CropsNHItemList.BrickedAgriculturalCasing.get(1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addFertilizerUnitRecipes() {
        int tier = BlockFertilizerUnit.MIN_TIER;
        for (CropsNHItemList output : FERTILIZER_UNITS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "RPS",
                    "CHC",
                    "WBW",
                    'R', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'P', MTEBasicMachineWithRecipe.X.PUMP,
                    'S', MTEBasicMachineWithRecipe.X.SENSOR,
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'W', getCable(tier),
                    'B', CropsNHItemList.BrickedAgriculturalCasing.get(1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addGrowthAccelerationUnits() {
        int tier = BlockGrowthAccelerationUnit.MIN_TIER;
        for (CropsNHItemList output : GROWTH_ACCEL_UNITS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "RPE",
                    "CHC",
                    "WBW",
                    'R', MTEBasicMachineWithRecipe.X.ROBOT_ARM,
                    'P', MTEBasicMachineWithRecipe.X.PUMP,
                    'E', MTEBasicMachineWithRecipe.X.EMITTER,
                    'C', getCircuit(tier),
                    'H', getHull(tier),
                    'W', getCable(tier),
                    'B', CropsNHItemList.BrickedAgriculturalCasing.get(1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addOverclockedGrowthAccelerationUnits() {
        int tier = BlockOverclockedGrowthAccelerationUnit.MIN_TIER;
        for (CropsNHItemList output : OC_GROWTH_ACCEL_UNITS) {
            GTModHandler.addMachineCraftingRecipe(
                output.get(1),
                GTModHandler.RecipeBits.BITSD,
                new Object[] {
                    // spotless:off
                    "FAF",
                    "CGC",
                    "WBW",
                    'F', MTEBasicMachineWithRecipe.X.FIELD_GENERATOR,
                    'A', getWorldAccelerator(tier),
                    'E', MTEBasicMachineWithRecipe.X.EMITTER,
                    'C', getCircuit(tier),
                    'G', GROWTH_ACCEL_UNITS[tier - BlockGrowthAccelerationUnit.MIN_TIER],
                    'W', getCable(tier),
                    'B', CropsNHItemList.BrickedAgriculturalCasing.get(1)
                    // spotless:on
                },
                tier);
            tier++;
        }
    }

    private static void addNanCertificateRecipe() {
        // NAN Certificate recipe
        recipe(8, 29826 * 3600 + 9 * 60 + 7, 35)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64))
            .itemOutputs(CropsNHItemList.nanCertificate.get(1))
            .addTo(extruderRecipes);
    }

    // region tier item lists

    public static ItemStack getWateringCan() {
        ItemStack wateringCan = null;
        if (ModUtils.UtilitiesInExcess.isLoaded()) {
            Item item = GameRegistry.findItem(ModUtils.UtilitiesInExcess.modId, "watering_can_basic");
            if (item != null) wateringCan = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        }
        if (wateringCan == null && Mods.ExtraUtilities.isModLoaded()) {
            Item item = GameRegistry.findItem(Mods.ExtraUtilities.getID(), "watering_can");
            if (item != null) wateringCan = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        }
        if (wateringCan == null) {
            wateringCan = ItemList.Cell_Water.get(1L);
        }
        return wateringCan;
    }

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

    private static final CropsNHItemList[] CROP_BREEDER = new CropsNHItemList[] { CropsNHItemList.CropBreeder_LV,
        CropsNHItemList.CropBreeder_MV, CropsNHItemList.CropBreeder_HV, CropsNHItemList.CropBreeder_EV,
        CropsNHItemList.CropBreeder_IV, CropsNHItemList.CropBreeder_LuV, CropsNHItemList.CropBreeder_ZPM,
        CropsNHItemList.CropBreeder_UV, CropsNHItemList.CropBreeder_UHV, CropsNHItemList.CropBreeder_UEV,
        CropsNHItemList.CropBreeder_UIV, CropsNHItemList.CropBreeder_UMV };

    private static final CropsNHItemList[] CROP_GENE_EXTRACTORS = new CropsNHItemList[] {
        CropsNHItemList.CropGeneExtractor_EV, CropsNHItemList.CropGeneExtractor_IV,
        CropsNHItemList.CropGeneExtractor_LuV, CropsNHItemList.CropGeneExtractor_ZPM,
        CropsNHItemList.CropGeneExtractor_UV, CropsNHItemList.CropGeneExtractor_UHV,
        CropsNHItemList.CropGeneExtractor_UEV, CropsNHItemList.CropGeneExtractor_UIV,
        CropsNHItemList.CropGeneExtractor_UMV };

    private static final CropsNHItemList[] CROP_SYNTHESIZERS = new CropsNHItemList[] {
        CropsNHItemList.CropSynthesizer_EV, CropsNHItemList.CropSynthesizer_IV, CropsNHItemList.CropSynthesizer_LuV,
        CropsNHItemList.CropSynthesizer_ZPM, CropsNHItemList.CropSynthesizer_UV, CropsNHItemList.CropSynthesizer_UHV,
        CropsNHItemList.CropSynthesizer_UEV, CropsNHItemList.CropSynthesizer_UIV, CropsNHItemList.CropSynthesizer_UMV };

    private static final CropsNHItemList[] ADV_HARVESTING_UNITS = new CropsNHItemList[] {
        CropsNHItemList.AdvancedHarvestingUnit_MV, CropsNHItemList.AdvancedHarvestingUnit_HV,
        CropsNHItemList.AdvancedHarvestingUnit_EV, CropsNHItemList.AdvancedHarvestingUnit_IV,
        CropsNHItemList.AdvancedHarvestingUnit_LuV, CropsNHItemList.AdvancedHarvestingUnit_ZPM,
        CropsNHItemList.AdvancedHarvestingUnit_UV, CropsNHItemList.AdvancedHarvestingUnit_UHV,
        CropsNHItemList.AdvancedHarvestingUnit_UEV, CropsNHItemList.AdvancedHarvestingUnit_UIV,
        CropsNHItemList.AdvancedHarvestingUnit_UMV, CropsNHItemList.AdvancedHarvestingUnit_UXV };

    private static final CropsNHItemList[] ENVIRONMENTAL_ENHANCEMENT_UNITS = new CropsNHItemList[] {
        CropsNHItemList.EnvironmentalEnhancementUnit_MV, CropsNHItemList.EnvironmentalEnhancementUnit_HV,
        CropsNHItemList.EnvironmentalEnhancementUnit_EV, CropsNHItemList.EnvironmentalEnhancementUnit_IV,
        CropsNHItemList.EnvironmentalEnhancementUnit_LuV, CropsNHItemList.EnvironmentalEnhancementUnit_ZPM,
        CropsNHItemList.EnvironmentalEnhancementUnit_UV, CropsNHItemList.EnvironmentalEnhancementUnit_UHV,
        CropsNHItemList.EnvironmentalEnhancementUnit_UEV, CropsNHItemList.EnvironmentalEnhancementUnit_UIV,
        CropsNHItemList.EnvironmentalEnhancementUnit_UMV, CropsNHItemList.EnvironmentalEnhancementUnit_UXV };

    private static final CropsNHItemList[] FERTILIZER_UNITS = new CropsNHItemList[] { CropsNHItemList.FertilizerUnit_MV,
        CropsNHItemList.FertilizerUnit_HV, CropsNHItemList.FertilizerUnit_EV, CropsNHItemList.FertilizerUnit_IV,
        CropsNHItemList.FertilizerUnit_LuV, CropsNHItemList.FertilizerUnit_ZPM, CropsNHItemList.FertilizerUnit_UV,
        CropsNHItemList.FertilizerUnit_UHV, CropsNHItemList.FertilizerUnit_UEV, CropsNHItemList.FertilizerUnit_UIV,
        CropsNHItemList.FertilizerUnit_UMV, CropsNHItemList.FertilizerUnit_UXV };

    private static final CropsNHItemList[] GROWTH_ACCEL_UNITS = new CropsNHItemList[] {
        CropsNHItemList.GrowthAccelerationUnit_MV, CropsNHItemList.GrowthAccelerationUnit_HV,
        CropsNHItemList.GrowthAccelerationUnit_EV, CropsNHItemList.GrowthAccelerationUnit_IV,
        CropsNHItemList.GrowthAccelerationUnit_LuV, CropsNHItemList.GrowthAccelerationUnit_ZPM,
        CropsNHItemList.GrowthAccelerationUnit_UV, CropsNHItemList.GrowthAccelerationUnit_UHV,
        CropsNHItemList.GrowthAccelerationUnit_UEV, CropsNHItemList.GrowthAccelerationUnit_UIV,
        CropsNHItemList.GrowthAccelerationUnit_UMV, CropsNHItemList.GrowthAccelerationUnit_UXV };

    private static final CropsNHItemList[] OC_GROWTH_ACCEL_UNITS = new CropsNHItemList[] {
        CropsNHItemList.OverclockedGrowthAccelerationUnit_ZPM, CropsNHItemList.OverclockedGrowthAccelerationUnit_UV,
        CropsNHItemList.OverclockedGrowthAccelerationUnit_UHV, CropsNHItemList.OverclockedGrowthAccelerationUnit_UEV,
        CropsNHItemList.OverclockedGrowthAccelerationUnit_UIV, CropsNHItemList.OverclockedGrowthAccelerationUnit_UMV,
        CropsNHItemList.OverclockedGrowthAccelerationUnit_UXV };

    private static final CropsNHItemList[] SEED_BEDS = new CropsNHItemList[] { CropsNHItemList.SeedBed_MV,
        CropsNHItemList.SeedBed_HV, CropsNHItemList.SeedBed_EV, CropsNHItemList.SeedBed_IV, CropsNHItemList.SeedBed_LuV,
        CropsNHItemList.SeedBed_ZPM, CropsNHItemList.SeedBed_UV, CropsNHItemList.SeedBed_UHV,
        CropsNHItemList.SeedBed_UEV, CropsNHItemList.SeedBed_UIV, CropsNHItemList.SeedBed_UMV,
        CropsNHItemList.SeedBed_UXV };

    private static ItemList getInputHatch(int tier) {
        return switch (tier) {
            case VoltageIndex.ULV -> ItemList.Hatch_Input_ULV;
            case VoltageIndex.LV -> ItemList.Hatch_Input_LV;
            case VoltageIndex.MV -> ItemList.Hatch_Input_MV;
            case VoltageIndex.HV -> ItemList.Hatch_Input_HV;
            case VoltageIndex.EV -> ItemList.Hatch_Input_EV;
            case VoltageIndex.IV -> ItemList.Hatch_Input_IV;
            case VoltageIndex.LuV -> ItemList.Hatch_Input_LuV;
            case VoltageIndex.ZPM -> ItemList.Hatch_Input_ZPM;
            case VoltageIndex.UV -> ItemList.Hatch_Input_UV;
            case VoltageIndex.UHV -> ItemList.Hatch_Input_UHV;
            case VoltageIndex.UEV -> ItemList.Hatch_Input_UEV;
            case VoltageIndex.UIV -> ItemList.Hatch_Input_UIV;
            case VoltageIndex.UMV -> ItemList.Hatch_Input_UMV;
            case VoltageIndex.UXV -> ItemList.Hatch_Input_UXV;
            default -> ItemList.Hatch_Input_MAX;
        };
    }

    private static ItemList getWorldAccelerator(int tier) {
        return switch (tier) {
            case VoltageIndex.LV -> ItemList.AcceleratorLV;
            case VoltageIndex.MV -> ItemList.AcceleratorMV;
            case VoltageIndex.HV -> ItemList.AcceleratorHV;
            case VoltageIndex.EV -> ItemList.AcceleratorEV;
            case VoltageIndex.IV -> ItemList.AcceleratorIV;
            case VoltageIndex.LuV -> ItemList.AcceleratorLuV;
            case VoltageIndex.ZPM -> ItemList.AcceleratorZPM;
            default -> ItemList.AcceleratorUV;
        };
    }

    private static Object getCircuit(int tier) {
        return switch (tier) {
            case VoltageIndex.UEV -> OrePrefixes.circuit.get(Materials.UEV);
            case VoltageIndex.UIV -> OrePrefixes.circuit.get(Materials.UIV);
            case VoltageIndex.UMV -> OrePrefixes.circuit.get(Materials.UMV);
            case VoltageIndex.UXV -> OrePrefixes.circuit.get(Materials.UXV);
            default -> MTEBasicMachineWithRecipe.X.CIRCUIT;
        };
    }

    private static Object getBetterCircuit(int tier) {
        return switch (tier) {
            case VoltageIndex.UHV -> OrePrefixes.circuit.get(Materials.UEV);
            case VoltageIndex.UEV -> OrePrefixes.circuit.get(Materials.UIV);
            case VoltageIndex.UIV -> OrePrefixes.circuit.get(Materials.UMV);
            case VoltageIndex.UMV -> OrePrefixes.circuit.get(Materials.UXV);
            default -> MTEBasicMachineWithRecipe.X.CIRCUIT;
        };
    }

    private static Object getPipe(int tier) {
        return switch (tier) {
            case VoltageIndex.LuV -> OrePrefixes.pipeMedium.get(Materials.Enderium);
            case VoltageIndex.ZPM -> OrePrefixes.pipeMedium.get(Materials.Naquadah);
            case VoltageIndex.UV, VoltageIndex.UHV -> OrePrefixes.pipeMedium.get(Materials.Neutronium);
            case VoltageIndex.UEV -> OrePrefixes.pipeMedium.get(Materials.Infinity);
            case VoltageIndex.UIV -> OrePrefixes.pipeMedium.get(Materials.TranscendentMetal);
            case VoltageIndex.UMV, VoltageIndex.UXV -> OrePrefixes.pipeMedium.get(Materials.SpaceTime);
            default -> MTEBasicMachineWithRecipe.X.PIPE;
        };
    }

    private static Object getCable(int tier) {
        return switch (tier) {
            case VoltageIndex.LuV -> OrePrefixes.cableGt01.get(Materials.VanadiumGallium);
            case VoltageIndex.ZPM -> OrePrefixes.cableGt01.get(Materials.Naquadah);
            case VoltageIndex.UV -> OrePrefixes.cableGt01.get(Materials.ElectrumFlux);
            case VoltageIndex.UHV -> OrePrefixes.cableGt01.get(Materials.Bedrockium);
            case VoltageIndex.UEV -> OrePrefixes.cableGt01.get(Materials.Draconium);
            case VoltageIndex.UIV -> OrePrefixes.cableGt01.get(Materials.NetherStar);
            case VoltageIndex.UMV -> OrePrefixes.cableGt01.get(Materials.Quantium);
            case VoltageIndex.UXV -> OrePrefixes.wireGt01.get(Materials.SpaceTime);
            default -> MTEBasicMachineWithRecipe.X.WIRE;
        };
    }

    private static Object getHull(int tier) {
        return switch (tier) {
            case VoltageIndex.UEV -> ItemList.Hull_UEV;
            case VoltageIndex.UIV -> ItemList.Hull_UIV;
            case VoltageIndex.UMV -> ItemList.Hull_UMV;
            case VoltageIndex.UXV -> ItemList.Hull_UXV;
            default -> MTEBasicMachineWithRecipe.X.HULL;
        };
    }

    private static Object getPlate(int tier) {
        return switch (tier) {
            case VoltageIndex.LuV -> OrePrefixes.plate.get(Materials.get("Rhodium-PlatedPalladium"));
            case VoltageIndex.ZPM -> OrePrefixes.plate.get(Materials.Iridium);
            case VoltageIndex.UV -> OrePrefixes.plate.get(Materials.Osmium);
            case VoltageIndex.UHV -> OrePrefixes.plate.get(Materials.Neutronium);
            case VoltageIndex.UEV -> OrePrefixes.plate.get(Materials.Bedrockium);
            case VoltageIndex.UIV -> OrePrefixes.plate.get(Materials.CosmicNeutronium);
            case VoltageIndex.UMV -> OrePrefixes.plate.get(Materials.TranscendentMetal);
            case VoltageIndex.UXV -> OrePrefixes.plate.get(Materials.SpaceTime);
            default -> MTEBasicMachineWithRecipe.X.PLATE;
        };
    }
    // endregion tier item lists
}
