package com.gtnewhorizon.cropsnh.crops.food.natura;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropFood;

import mods.natura.common.NContent;

public class CropBlackberry extends CropFood {

    public CropBlackberry() {
        super("blackberry", new Color(0x141C22), new Color(0x4D5A66));
        this.addDrop(new ItemStack(NContent.berryItem, 3, 2), 100_00);
        this.addAlternateSeed("cropBlackberry");
        this.addAlternateSeed("seedBlackberry");
        // needs a good dose of water and loves to grow in very dense bushy areas
        this.addLikedBiomes(BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.LUSH);
    }

    @Override
    public String getCreator() {
        return "bartimaeusnek";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getGrowthDuration() {
        return 200;
    }

    @Override
    public int getMaxGrowthStage() {
        return 3;
    }
}
