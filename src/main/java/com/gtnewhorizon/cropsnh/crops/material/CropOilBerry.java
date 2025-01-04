package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHItemList;

public class CropOilBerry extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropOilBerry() {
        super("oilBerry", new Color(0x0A0A0A), new Color(0x333333));
        this.addDrop(CropsNHItemList.oilBerry.get(1), 100_00);
    }

    @Override
    public String getCreator() {
        return "Spacetoad";
    }

    @Override
    public int getTier() {
        return 7;
    }

    @Override
    public int getGrowthDuration() {
        return 2000;
    }

    @Override
    public ISoilList getSoilTypes() {
        return soilTypes;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}