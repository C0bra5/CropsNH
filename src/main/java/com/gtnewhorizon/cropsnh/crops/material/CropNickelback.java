package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.reference.Reference;

import gregtech.api.enums.VoltageIndex;

public class CropNickelback extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropNickelback() {
        super("nickelback", new Color(0x7E81AD), new Color(0xB7B8E9));

        this.addDrop(CropsNHItemList.nickelbackLeaf.get(1), 100_00);

        this.addBlockUnderRequirement("nickel");

        this.addDuplicationCatalyst("dustNickel", 1);
    }

    @Override
    public String getFlavourText() {
        return Reference.MOD_ID_LOWER + "_crops.nickelback.flavour";
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public int getMachineBreedingRecipeTier() {
        return VoltageIndex.MV;
    }

    @Override
    public int getGrowthDuration() {
        return 1000;
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
