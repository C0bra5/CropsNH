package com.gtnewhorizon.cropsnh.crops.ic2;

import com.gtnewhorizon.cropsnh.api.ICropStickTile;
import com.gtnewhorizon.cropsnh.api.ISeedShape;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.api.SeedShape;
import com.gtnewhorizon.cropsnh.crops.abstracts.NHCropCard;
import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.renderers.PlantRenderer;
import gregtech.api.enums.ItemList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.Color;

public class CropVenomilia extends NHCropCard {

    public CropVenomilia() {
        super("venomilia", new Color(0x440F69), new Color(0x8921CC));
        this.addDrop(ItemList.IC2_Grin_Powder.get(1L), 100_00);
        this.addDrop(new ItemStack(Items.dye, 1, 5), 5_00);
    }

    @Override
    public String getCreator() {
        return "raGan";
    }

    @Override
    public int getGrowthDuration() {
        return 1800;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public boolean spreadsWeeds(ICropStickTile te) {
        return super.spreadsWeeds(te);
    }

    @Override
    public int startsSpreadingWeedsAt() {
        return 8;
    }

    @Override
    public int getMaxGrowthStage() {
        return 6;
    }

    @Override
    public ISeedShape getSeedShape() {
        return SeedShape.FLOWER;
    }

    // TODO: MAKE VENOMILIA POISON ENTITY PASSING THOUGH IT
    // NOTHING HAPPENS IF SNEAKING (YOU USED TO HAVE A 1/50 TO STILL GET POISONED, THAT'S GETTING YEETED)
    // IF FULLY GROWN LOOSE 33% GROWTH PROGRESS
    // POISON DURATION IS (random.nextInt(10) + 5) * 20
}
