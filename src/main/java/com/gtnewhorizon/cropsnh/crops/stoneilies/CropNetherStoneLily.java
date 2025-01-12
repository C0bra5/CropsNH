package com.gtnewhorizon.cropsnh.crops.stoneilies;

import java.awt.Color;

import net.minecraftforge.common.BiomeDictionary;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropBaseStoneLily;
import com.gtnewhorizon.cropsnh.init.CropsNHMutationPools;

import gregtech.api.enums.Materials;

public class CropNetherStoneLily extends CropBaseStoneLily {

    public CropNetherStoneLily() {
        super("netherStone", new Color(0x911717), new Color(0xC21F1F));
        this.addDrop(Materials.Netherrack.getDust(9), 100_00);
        this.addBlockUnderRequirement("netherrack");
        this.addLikedBiomes(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
    }

    @Override
    public void registerToPools() {
        super.registerToPools();
        CropsNHMutationPools.stoneLilies.register(this);
        CropsNHMutationPools.nether.register(this);
    }
}
