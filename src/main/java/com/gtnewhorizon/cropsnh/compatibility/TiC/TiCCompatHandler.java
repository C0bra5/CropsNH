package com.gtnewhorizon.cropsnh.compatibility.TiC;

import gregtech.api.enums.Mods;
import tconstruct.api.harvesting.CropHarvestHandlers;

public class TiCCompatHandler {

    public static void onInit() {
        if (!Mods.TinkerConstruct.isModLoaded()) return;
        CropHarvestHandlers.registerCropHarvestHandler(new CropsNHTiCHarvestHandler());
    }
}
