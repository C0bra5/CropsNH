package com.gtnewhorizon.cropsnh.api;

import net.minecraft.item.ItemStack;

public interface ISeedData {

    ICropCard getCrop();

    ISeedStats getStats();

    ItemStack getStack();
}
