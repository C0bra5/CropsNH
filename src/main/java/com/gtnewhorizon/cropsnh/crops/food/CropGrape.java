package com.gtnewhorizon.cropsnh.crops.food;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropFood;

import gregtech.api.enums.ItemList;

public class CropGrape extends CropFood {

    public CropGrape() {
        super("grape", new Color(0x4A1041), new Color(0x5D1451));
        this.addDrop(ItemList.Crop_Drop_Grapes.get(1), 100_00);
        this.addAlternateSeed("seedGrape");
        this.addAlternateSeed("cropGrape");
        // likes warm soils mostly
        this.addLikedBiomes(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SPARSE);
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getGrowthDuration() {
        return 800;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}
