package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;

import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;

public class CropLazulia extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropLazulia() {
        super("lazulia", new Color(0x142EAF), new Color(0x7497EA));

        this.addDrop(Materials.Lapis.getDust(1), 100_00);

        this.addBlockUnderRequirement("lapis");

        this.addDuplicationCatalyst("gemLapis", 1);
        this.addDuplicationCatalyst("dustLapis", 1);
    }

    @Override
    public int getTier() {
        return 7;
    }

    @Override
    public int getMachineBreedingRecipeTier() {
        return VoltageIndex.MV;
    }

    @Override
    public int getGrowthDuration() {
        return 2800;
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
