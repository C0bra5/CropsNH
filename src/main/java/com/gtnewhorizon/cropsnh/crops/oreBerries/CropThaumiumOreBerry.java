package com.gtnewhorizon.cropsnh.crops.oreBerries;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropOreBerry;
import com.gtnewhorizon.cropsnh.farming.growthrequirements.MaxLightLevelRequirement;

import gregtech.api.enums.Materials;

public class CropThaumiumOreBerry extends CropOreBerry {

    public CropThaumiumOreBerry() {
        super("thaumium", new Color(0x67458A), new Color(0x9664C8));
        this.addDrop(Materials.Thaumium.getNuggets(1), 100_00);
        this.addBlockUnderRequirement("thaumium");
        this.addGrowthRequirements(new MaxLightLevelRequirement(10));
    }

    @Override
    public int getTier() {
        return 7;
    }

    @Override
    public int getGrowthDuration() {
        return 3000;
    }

    @Override
    public String getCreator() {
        return "bartimaeusnek";
    }

}
