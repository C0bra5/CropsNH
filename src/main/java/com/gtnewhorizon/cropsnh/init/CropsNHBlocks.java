package com.gtnewhorizon.cropsnh.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.BlockCropSticks;
import com.gtnewhorizon.cropsnh.blocks.CropsNHBlocksCasing1;
import com.gtnewhorizon.cropsnh.utility.LogHelper;

public class CropsNHBlocks {

    public static Block blockCrop;
    public static Block blockCasings1;

    public static void preInit() {
        blockCrop = new BlockCropSticks();
        CropsNHItemList.cropSticks.set(new ItemStack(blockCrop, 1, 0));
        blockCasings1 = new CropsNHBlocksCasing1();
        LogHelper.debug("Blocks registered");
    }
}
