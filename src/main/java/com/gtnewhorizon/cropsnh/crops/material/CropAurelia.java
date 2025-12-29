package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;

import gregtech.api.enums.VoltageIndex;

public class CropAurelia extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropAurelia() {
        super("aurelia", new Color(0xABA600), new Color(0xEFE913));

        this.addDrop(CropsNHItemList.aureliaLeaf.get(1), 10_000);

        this.addBlockUnderRequirement("gold");

        this.addDuplicationCatalyst("dustGold", 1);
        // mesa
        this.addLikedBiomes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY);

    }

    @Override
    public String getCreator() {
        return "IC2 Team";
    }

    @Override
    public int getTier() {
        return 8;
    }

    @Override
    public int getMachineBreedingRecipeTier() {
        return VoltageIndex.MV;
    }

    @Override
    public int getGrowthDuration() {
        return 3700;
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
