package com.gtnewhorizon.cropsnh.loaders;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.cropsnh.farming.registries.FertilizerRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.HydrationRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.WeedEXRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;
import com.gtnewhorizon.cropsnh.init.CropsNHItems;

import forestry.plugins.PluginCore;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.core.item.ModItems;

public class FertilizerLoader {

    public static final int FERTILIZER_ITEM_POTENCY = 100;

    public static void postInit() {
        registerHydrationLiquids();
        registerWeedEXLiquids();
        registerLiquidFertilizers();
        registerItemFertilizers();
    }

    private static void registerHydrationLiquids() {
        HydrationRegistry.instance.register(FluidRegistry.WATER, 1);
        HydrationRegistry.instance.register(
            GTModHandler.getDistilledWater(1L)
                .getFluid(),
            2);
    }

    private static void registerWeedEXLiquids() {
        // the strong poisonous brew is the stuff contained in weed-ex cans
        WeedEXRegistry.instance.register(FluidRegistry.getFluid("potion.poison.strong"), 1);
        WeedEXRegistry.instance.register(Materials.WeedEX9000.mFluid, 10);
    }

    private static void registerLiquidFertilizers() {
        FertilizerRegistry.instance.register(ModItems.fluidFertBasic, 1);
        FertilizerRegistry.instance.register(CropsNHFluids.enrichedFertilizer, 10);
    }

    private static void registerItemFertilizers() {
        // vanilla bonemeal
        FertilizerRegistry.instance.register(Items.dye, 15, 5);

        // cropsNH
        FertilizerRegistry.instance
            .register(CropsNHItems.fertilizer, OreDictionary.WILDCARD_VALUE, FERTILIZER_ITEM_POTENCY);

        // forestry fertilizer
        if (Mods.Forestry.isModLoaded()) {
            FertilizerRegistry.instance.register(PluginCore.items.fertilizerCompound, OreDictionary.WILDCARD_VALUE, 25);
            FertilizerRegistry.instance.register(PluginCore.items.fertilizerBio, OreDictionary.WILDCARD_VALUE, 50);
        }

        // add crops nh fert to EIG
        MTEExtremeIndustrialGreenhouse.addFertilizerItem(CropsNHItemList.fertilizer.get(1));
    }

}
