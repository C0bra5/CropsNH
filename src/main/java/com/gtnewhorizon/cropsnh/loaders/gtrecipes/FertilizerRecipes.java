package com.gtnewhorizon.cropsnh.loaders.gtrecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import mods.natura.common.NContent;

public abstract class FertilizerRecipes extends BaseGTRecipeLoader {

    public static void postInit() {
        addFertilizerItemRecipes();
        addEnrichedFertilizerRecipes();
    }

    private static void addEnrichedFertilizerRecipes() {
        // enriched fertilizer
        // ez logistics puzzle where you either flood both with fertilizer items or use a solution with system like
        // function like item conduits
        lvRecipe(5, 0).itemInputs(CropsNHItemList.fertilizer.get(4))
            .fluidInputs(CropsNHUtils.getFertilizerFluid(1000))
            .fluidOutputs(new FluidStack(CropsNHFluids.enrichedFertilizer, 1000))
            .addTo(GTRecipeConstants.UniversalChemical);

        // cell only recipe for sb
        lvRecipe(1, 0)
            .itemInputs(
                CropsNHItemList.fertilizer.get(4),
                FluidContainerRegistry
                    .fillFluidContainer(CropsNHUtils.getFertilizerFluid(1000), ItemList.Cell_Empty.get(1)))
            .itemOutputs(CropsNHItemList.enrichedFertilizerCell.get(1))
            .addTo(RecipeMaps.chemicalReactorRecipes);
    }

    private static void addFertilizerItemRecipes() {
        // TODO: REMOVE RECIPES FROM NH CORE MOD
        lvRecipe(5, 0)
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                new ItemStack(Blocks.sand, 4, 0))
            .itemOutputs(CropsNHItemList.fertilizer.get(4))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1000))
            .addTo(RecipeMaps.mixerRecipes);

        lvRecipe(5, 0).itemInputs(CropsNHItemList.fertilizer.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1))
            .fluidOutputs(new FluidStack(FluidRegistry.WATER, 1000))
            .addTo(RecipeMaps.electrolyzerRecipes);

        recipe(16, 2, 0).circuit(1)
            .itemInputs(CropsNHItemList.fertilizer.get(1))
            .fluidInputs(CropsNHUtils.getFertilizerFluid(144))
            .addTo(RecipeMaps.centrifugeRecipes);

        // TODO: REMOVE EXISTING RECIPES FROM GT5U
        for (Fluid tFluid : new Fluid[] { FluidRegistry.WATER, GTModHandler.getDistilledWater(1L)
            .getFluid() }) {

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(5, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
                .itemOutputs(CropsNHItemList.fertilizer.get(1))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(5, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(1))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(20, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(4))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(20, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(4))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(20, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(4))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(20, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(4))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(15, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(3))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            lvRecipe(10, 0)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
                .itemOutputs(CropsNHItemList.fertilizer.get(2))
                .fluidInputs(new FluidStack(tFluid, 1000))
                .addTo(GTRecipeConstants.UniversalChemical);

            if (Mods.Natura.isModLoaded()) {

                mvRecipe(10, 0)
                    .itemInputs(
                        getModItem(Mods.Natura.ID, "florasapling", 2, 6),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L))
                    .itemOutputs(
                        CropsNHItemList.fertilizer.get(2),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L))
                    .fluidInputs(new FluidStack(tFluid, 1000))
                    .addTo(GTRecipeConstants.UniversalChemical);

                for (int meta = 0; meta <= 2; meta++) {
                    ItemStack leafStack = getModItem(Mods.Natura.ID, "Dark Leaves", 2, meta);
                    mvRecipe(10, 0)
                        .itemInputs(leafStack, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L))
                        .itemOutputs(
                            CropsNHItemList.fertilizer.get(2),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L))
                        .fluidInputs(new FluidStack(tFluid, 1000))
                        .addTo(GTRecipeConstants.UniversalChemical);
                }

                mvRecipe(10, 0)
                    .itemInputs(
                        getModItem(Mods.Natura.ID, "Dark Tree", 2, 0),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L))
                    .itemOutputs(
                        CropsNHItemList.fertilizer.get(8),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L))
                    .fluidInputs(new FluidStack(tFluid, 1000))
                    .addTo(GTRecipeConstants.UniversalChemical);

                mvRecipe(10, 0)
                    .itemInputs(
                        new ItemStack(NContent.potashApple, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L))
                    .itemOutputs(
                        CropsNHItemList.fertilizer.get(32),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L))
                    .fluidInputs(new FluidStack(tFluid, 1000))
                    .addTo(GTRecipeConstants.UniversalChemical);
            }
        }

        mvRecipe(5, 0).itemInputs(getModItem(Mods.Natura.ID, "florasapling", 2, 6))
            .itemOutputs(CropsNHItemList.fertilizer.get(1))
            .addTo(RecipeMaps.extractorRecipes);

        for (int meta = 0; meta <= 2; meta++) {
            ItemStack leafStack = getModItem(Mods.Natura.ID, "Dark Leaves", 8, meta);
            mvRecipe(5, 0).itemInputs(leafStack)
                .itemOutputs(CropsNHItemList.fertilizer.get(1))
                .addTo(RecipeMaps.extractorRecipes);
        }

        mvRecipe(5, 0).itemInputs(getModItem(Mods.Natura.ID, "Dark Tree", 2, 0))
            .itemOutputs(CropsNHItemList.fertilizer.get(1))
            .addTo(RecipeMaps.extractorRecipes);
        mvRecipe(5, 0).itemInputs(getModItem(Mods.Natura.ID, "Nether.netherFood", 1, 0))
            .itemOutputs(CropsNHItemList.fertilizer.get(4))
            .addTo(RecipeMaps.extractorRecipes);
    }

}
