package com.gtnewhorizon.cropsnh.crops.stoneilies.etfuturum;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropBaseStoneLily;
import com.gtnewhorizon.cropsnh.utility.OreDictHelper;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import net.minecraftforge.common.BiomeDictionary;

import java.awt.Color;

public class CropTuffLily extends CropBaseStoneLily {

    public CropTuffLily() {
        super("tuff", new Color(0x57574D), new Color(0x95978D));

        this.addDrop(OreDictHelper.getCopiedOreStack("dustTuff", 9), 100_00);

        this.addBlockUnderRequirement("tuff");

        this.addLikedBiomes(BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS);
    }
}
