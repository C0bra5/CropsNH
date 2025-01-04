package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.api.ISeedShape;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.api.SeedShape;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHItemList;
import com.gtnewhorizon.cropsnh.renderers.PlantRenderer;

public class CropOsmianth extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropOsmianth() {
        super("osmianth", new Color(0x2525BC), new Color(0x2E2EEC));
        this.addDrop(CropsNHItemList.osmianthFlower.get(1), 25_00);
        this.addBlockUnderRequirement("osmium");
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
    public ISoilList getSoilTypes() {
        return soilTypes;
    }

    @Override
    public ISeedShape getSeedShape() {
        return SeedShape.flower;
    }

    @Override
    public int getRenderShape() {
        return PlantRenderer.RENDER_X;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}
