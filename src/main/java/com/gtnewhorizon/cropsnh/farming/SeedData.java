package com.gtnewhorizon.cropsnh.farming;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.api.ISeedStats;
import net.minecraft.item.ItemStack;

public class SeedData implements ISeedData {

    public ICropCard crop;
    public ISeedStats stats;
    public ItemStack stack;

    public SeedData(ICropCard crop, ISeedStats stats) {
        this(crop,stats,null);
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
        return stats;
    }
}
