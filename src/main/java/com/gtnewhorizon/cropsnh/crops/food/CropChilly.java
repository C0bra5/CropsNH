package com.gtnewhorizon.cropsnh.crops.food;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropFood;

import gregtech.api.enums.ItemList;

public class CropChilly extends CropFood {

    public CropChilly() {
        super("chilly", new Color(0x83090B), new Color(0xC8462A));
        // TODO: MOVE ITEM DROP TO CROPS NH OR SOMETHING
        this.addDrop(ItemList.Crop_Drop_Chilly.get(1L), 100_00);
        this.addAlternateSeed("cropChilli");
        this.addAlternateSeed("cropChilipepper");
        this.addAlternateSeed("seedChilipepper");
        // likes warm temperatures
        this.addLikedBiomes(BiomeDictionary.Type.HOT, BiomeDictionary.Type.LUSH);
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
