package com.gtnewhorizon.cropsnh.crops.food;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.api.ISeedShape;
import com.gtnewhorizon.cropsnh.api.SeedShape;
import com.gtnewhorizon.cropsnh.crops.abstracts.CropFood;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class CropCoffee extends CropFood {

    public CropCoffee() {
        super("coffee", new Color(0x3D1E00), new Color(0x964F05));

        this.addDrop(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1), 10_000);
        // likes warm temperatures, usually found in the same area as rainforests.
        this.addLikedBiomes(BiomeDictionary.Type.HOT, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET);
    }

    @Override
    public ISeedShape getSeedShape() {
        return SeedShape.grains;
    }

    @Override
    public int getTier() {
        return 7;
    }

    @Override
    public int getGrowthDuration() {
        return 2800;
    }

    @Override
    public int getMaxGrowthStage() {
        return 5;
    }
}
