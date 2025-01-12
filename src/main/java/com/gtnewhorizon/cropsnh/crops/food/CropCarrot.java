package com.gtnewhorizon.cropsnh.crops.food;

import java.awt.Color;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropFood;
import com.gtnewhorizon.cropsnh.init.CropsNHMutationPools;

public class CropCarrot extends CropFood {

    public CropCarrot() {
        super("carrot", new Color(0xE58700), new Color(0xEEAE7D));
        this.addDrop(new ItemStack(Items.carrot, 1), 10_000);
        this.addAlternateSeed(new ItemStack(Items.carrot, 1));
    }

    @Override
    public void registerToPools() {
        super.registerToPools();
        CropsNHMutationPools.food.register(this);
    }

    @Override
    public String getCreator() {
        return "Notch";
    }

    @Override
    public String getUnlocalizedName() {
        return "item.carrots.name";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getGrowthDuration() {
        return 800;
    }

    @Override
    public int getMinGrowthStage() {
        return 0;
    }

    @Override
    public int getMaxGrowthStage() {
        return 3;
    }

    @Override
    protected String getBaseTexturePath() {
        return "carrots_stage_";
    }

}