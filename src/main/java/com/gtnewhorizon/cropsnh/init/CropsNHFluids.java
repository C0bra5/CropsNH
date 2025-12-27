package com.gtnewhorizon.cropsnh.init;

import com.gtnewhorizon.cropsnh.reference.Reference;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.fluids.AlcoholImpure;

import cpw.mods.fml.common.LoaderException;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class CropsNHFluids {

    public static Fluid enrichedFertilizer;

    // alchool
    public final static AlcoholImpure FWheat = new AlcoholImpure(Reference.MOD_ID_LOWER + ":FWheat", 40, "potion.alcopops");
    public final static AlcoholImpure Korn = new AlcoholImpure(Reference.MOD_ID_LOWER + ":Korn", 320, "potion.vodka");
    public final static AlcoholImpure DKorn = new AlcoholImpure(Reference.MOD_ID_LOWER + ":DKorn", 380, "potion.vodka");
    public final static AlcoholImpure FReed = new AlcoholImpure(Reference.MOD_ID_LOWER + ":FReed", 50, "potion.alcopops");
    public final static AlcoholImpure SWhine = new AlcoholImpure(Reference.MOD_ID_LOWER + ":SWhine", 700, "potion.reedwater");
    public final static AlcoholImpure Mash = new AlcoholImpure(Reference.MOD_ID_LOWER + ":Mash", 25, "potion.reedwater");
    public final static AlcoholImpure Wash = new AlcoholImpure(Reference.MOD_ID_LOWER + ":Wash", 50, "potion.alcopops");
    public final static AlcoholImpure GHP = new AlcoholImpure(Reference.MOD_ID_LOWER + ":GHP", 700, "potion.vodka");
    public final static AlcoholImpure jagi = new AlcoholImpure(Reference.MOD_ID_LOWER + ":jagi", 100000, "potion.alcopops");
    public final static AlcoholImpure njagi = new AlcoholImpure(Reference.MOD_ID_LOWER + ":njagi", 350, "potion.alcopops");

    public static void preInit() {
        // could probably use a propper GT Material but this is faster
        CropsNHFluids.enrichedFertilizer = FluidUtils.addGTFluidNonMolten(
            Reference.MOD_ID_LOWER + ":EnrichedFertilizer",
            "Enriched Fertilizer",
            new short[] { 40, 229, 21, 100 },
            4,
            295,
            null,
            ItemList.Cell_Empty.get(1),
            1000,
            true);

        CropsNHItemList.enrichedFertilizerCell.set(
            FluidContainerRegistry.fillFluidContainer(
                new FluidStack(CropsNHFluids.enrichedFertilizer, 1000),
                ItemList.Cell_Empty.get(1)));
        try {
            CropsNHItemList.enrichedFertilizerCell.get(1);
        } catch (NullPointerException npe) {
            throw new LoaderException(npe);
        }
    }
}
