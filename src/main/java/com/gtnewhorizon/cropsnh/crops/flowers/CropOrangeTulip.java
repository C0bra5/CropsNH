package com.gtnewhorizon.cropsnh.crops.flowers;

import java.awt.Color;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropVanillaFlower;
import com.gtnewhorizon.cropsnh.init.CropsNHMutationPools;

public class CropOrangeTulip extends CropVanillaFlower {

    public CropOrangeTulip() {
        super("orangeTulip", new Color(0x92460C), new Color(0xF4B33F));
        this.addDrop(new ItemStack(Items.dye, 1, 14), 10_000);
        this.addAlternateSeed(new ItemStack(Blocks.red_flower, 1, 5));
    }

    @Override
    public void registerToPools() {
        super.registerToPools();
        CropsNHMutationPools.flower.register(this);
    }

    @Override
    public String getCreator() {
        return "Notch";
    }

    @Override
    public String getUnlocalizedName() {
        return "tile.flower2.tulipOrange.name";
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }
}