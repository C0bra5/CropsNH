package com.gtnewhorizon.cropsnh.crops.cropnh;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.init.CropsNHItemList;

public class CropTransformium extends NHCropCard {

    public CropTransformium() {
        super("transformium", new Color(0x2A0040), new Color(0x6D1566));
        this.addDrop(CropsNHItemList.uuaBerry.get(1), 90_00);
        this.addDrop(CropsNHItemList.uumBerry.get(1), 10_00);
    }

    @Override
    public int getTier() {
        return 12;
    }

    @Override
    public int getGrowthDuration() {
        return 7200;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}
