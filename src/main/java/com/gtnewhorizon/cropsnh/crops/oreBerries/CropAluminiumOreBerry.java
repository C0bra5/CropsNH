package com.gtnewhorizon.cropsnh.crops.oreberries;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropOreBerry;
import com.gtnewhorizon.cropsnh.farming.requirements.growth.MaxLightLevelGrowthRequirement;

import gregtech.api.enums.VoltageIndex;
import tconstruct.world.TinkerWorld;

public class CropAluminiumOreBerry extends CropOreBerry {

    public CropAluminiumOreBerry() {
        super("aluminium", new Color(0x5687A3), new Color(0x80C8F0));

        this.addDrop(new ItemStack(TinkerWorld.oreBerries, 6, 4), 100_00);

        this.addAlternateSeed(new ItemStack(TinkerWorld.oreBerries, 1, 4));

        this.addBlockUnderRequirement("aluminium");

        this.addGrowthRequirement(new MaxLightLevelGrowthRequirement(10));

        this.addDuplicationCatalyst("nuggetAluminium", 1);

        // tundra
        this.addLikedBiomes(BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY);
    }

    @Override
    public String getCreator() {
        return "bartimaeusnek";
    }

    @Override
    public int getMachineBreedingRecipeTier() {
        return VoltageIndex.MV;
    }
}
