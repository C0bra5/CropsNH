package com.gtnewhorizon.cropsnh.farming.cropplant;

import net.minecraft.item.ItemSeeds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CropPlantOreDict extends CropPlantGeneric {

    public CropPlantOreDict(ItemSeeds seed) {
        super(seed);
    }

    @Override
    public int transformMeta(int growthStage) {
        return growthStage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsFlower() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getInformation() {
        return "cropsnh_journal." + getSeed().getUnlocalizedName();
    }
}
