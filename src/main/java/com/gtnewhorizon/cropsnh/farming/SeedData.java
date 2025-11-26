package com.gtnewhorizon.cropsnh.farming;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.api.ISeedStats;

public class SeedData implements ISeedData {

    public ICropCard crop;
    public ISeedStats stats;
    public ItemStack stack;

    public SeedData(ICropCard crop, ISeedStats stats) {
        this(crop, stats, null);
    }

    public SeedData(ICropCard crop, ISeedStats stats, ItemStack stack) {
        this.crop = crop;
        this.stats = stats;
        this.stack = stack;
    }

    @Override
    public ICropCard getCrop() {
        return crop;
    }

    @Override
    public ISeedStats getStats() {
        return this.stats;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }
}
