package com.gtnewhorizon.cropsnh.crops.food;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;

public class CropTea extends NHCropCard {

    public CropTea() {
        super("tea", new Color(0x7C8C58), new Color(0xD8C97D));
        this.addDrop(CropsNHItemList.teaLeaf.get(1), 100_00);
        // tends to prefer areas with high rains (min ~45 inches/year) and large hills to flow the cycle nutrients.
        this.addLikedBiomes(BiomeDictionary.Type.WET, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.HOT);
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getGrowthDuration() {
        return 2400;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}
