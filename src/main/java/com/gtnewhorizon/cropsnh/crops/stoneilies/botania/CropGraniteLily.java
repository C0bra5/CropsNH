package com.gtnewhorizon.cropsnh.crops.stoneilies.botania;

import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.CropBaseStoneLily;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.utility.OreDictHelper;

import java.awt.Color;

public class CropGraniteLily extends CropBaseStoneLily {

    public CropGraniteLily() {
        super("granite", new Color(0x82675B), new Color(0xBA9583));
        this.addDrop(OreDictHelper.getCopiedOreStack("stoneGranite", 1), 100_00);
        this.addBlockUnderRequirement("botaniaGranite");
    }
}
