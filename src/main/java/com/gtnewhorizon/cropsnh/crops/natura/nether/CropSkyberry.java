package com.gtnewhorizon.cropsnh.crops.natura.nether;

import java.awt.Color;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHMutationPools;

import mods.natura.common.NContent;

public class CropSkyberry extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("netherrack");

    public CropSkyberry() {
        super("skyberry", new Color(0x2E8BA7), new Color(0x45E0FF));
        this.addDrop(new ItemStack(NContent.netherBerryItem, 2, 2), 100_00);
        this.addAlternateSeed(new ItemStack(NContent.netherBerryItem, 1, 2));
    }

    @Override
    public void registerToPools() {
        super.registerToPools();
        CropsNHMutationPools.nether.register(this);
    }

    @Override
    public String getCreator() {
        return "bartimaeusnek";
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getGrowthDuration() {
        return 300;
    }

    @Override
    public ISoilList getSoilTypes() {
        return soilTypes;
    }

    @Override
    public int getMaxGrowthStage() {
        return 3;
    }
}
