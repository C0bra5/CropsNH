package com.gtnewhorizon.cropsnh.crops.stoneilies.etfuturum;

import com.gtnewhorizon.cropsnh.crops.abstracts.CropBaseStoneLily;
import com.gtnewhorizon.cropsnh.utility.OreDictHelper;
import gregtech.api.enums.Materials;
import net.minecraftforge.common.BiomeDictionary;

import java.awt.Color;

public class CropDeepslateLily extends CropBaseStoneLily {

    public CropDeepslateLily() {
        super("deepslate", new Color(57, 57, 57), new Color(87, 87, 87));

        this.addDrop(OreDictHelper.getCopiedOreStack("deepslate", 9), 100_00);

        this.addBlockUnderRequirement("deepslate");

        this.addLikedBiomes(BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS);
    }
}
