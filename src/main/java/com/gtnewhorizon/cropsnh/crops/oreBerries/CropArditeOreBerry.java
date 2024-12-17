package com.gtnewhorizon.cropsnh.crops.oreBerries;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropOreBerry;
import com.gtnewhorizon.cropsnh.farming.growthrequirements.MaxLightLevelRequirement;

import gregtech.api.enums.Materials;

public class CropArditeOreBerry extends CropOreBerry {

    public CropArditeOreBerry() {
        super("ardite", new Color(0xAD5A00), new Color(0xFA8100));
        this.addDrop(Materials.Ardite.getNuggets(1), 100_00);
        this.addBlockUnderRequirement("ardite");
        this.addGrowthRequirements(new MaxLightLevelRequirement(10));
    }

    @Override
    public String getCreator() {
        return "bartimaeusnek";
    }
}
