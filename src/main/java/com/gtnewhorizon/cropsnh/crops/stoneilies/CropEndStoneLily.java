package com.gtnewhorizon.cropsnh.crops.stoneilies;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropBaseStoneLily;

import gregtech.api.enums.Materials;

public class CropEndStoneLily extends CropBaseStoneLily {

    public CropEndStoneLily() {
        super("endStone", new Color(0xABA67E), new Color(0xDFD9A5));
        this.addDrop(Materials.Endstone.getDust(2), 100_00);
        this.addBlockUnderRequirement("endStone");
    }

    @Override
    public int getGrowthDuration() {
        return 850;
    }
}