package com.gtnewhorizon.cropsnh.crops.material;

import java.awt.Color;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.api.ISeedShape;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.api.SeedShape;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;

import gregtech.api.enums.VoltageIndex;

public class CropStarwart extends NHCropCard {

    private final static ISoilList soilTypes = SoilRegistry.instance.get("stone");

    public CropStarwart() {
        super("starwart", new Color(0x8985BE), new Color(0xFFFFFF));

        this.addDrop(CropsNHItemList.starwart.get(1), 100_00);

        this.addBlockUnderRequirement("netherStar");

        this.addDuplicationCatalyst("dustNetherStar", 1);
        this.addDuplicationCatalyst("netherStar", 1);
        this.addDuplicationCatalyst(new ItemStack(Items.skull, 3, 1));
    }

    @Override
    public int getTier() {
        return 12;
    }

    @Override
    public int getMachineBreedingRecipeTier() {
        return VoltageIndex.EV;
    }

    @Override
    public int getGrowthDuration() {
        return 7200;
    }

    @Override
    public ISoilList getSoilTypes() {
        return soilTypes;
    }

    @Override
    public ISeedShape getSeedShape() {
        return SeedShape.spore;
    }

    @Override
    public int getMaxGrowthStage() {
        return 4;
    }

}
