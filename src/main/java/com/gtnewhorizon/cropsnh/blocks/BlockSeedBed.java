package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
import com.gtnewhorizon.cropsnh.reference.Names;
import com.gtnewhorizon.cropsnh.tileentity.singleblock.MTECropManager;

public class BlockSeedBed extends CropsNHBlockIndustrialFarmTiredComponent {

    public BlockSeedBed() {
        super(
            Names.Objects.seedBed,
            "Seed Bed",
            CropsNHItemList.SeedBed_MV,
            CropsNHItemList.SeedBed_HV,
            CropsNHItemList.SeedBed_EV,
            CropsNHItemList.SeedBed_IV,
            CropsNHItemList.SeedBed_LuV,
            CropsNHItemList.SeedBed_ZPM,
            CropsNHItemList.SeedBed_UV,
            CropsNHItemList.SeedBed_UHV,
            CropsNHItemList.SeedBed_UEV,
            CropsNHItemList.SeedBed_UIV,
            CropsNHItemList.SeedBed_UMV,
            CropsNHItemList.SeedBed_UXV);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.seedBed.0"));
        tooltip.add(
            StatCollector
                .translateToLocalFormatted("cropsnh_tooltip.seedBed.1", getCapacity(Items.feather.getDamage(stack))));
    }

    public static int getCapacity(int aTier) {
        return MTECropManager.getHorizontalRadius(aTier) * 2 + 1;
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        return switch (aSide) {
            case 0 -> CropsNHBlocks.blockCasings1.getIcon(1, 0);
            case 1 -> Blocks.farmland.getIcon(1, 7);
            default -> super.getIcon(aSide, aMeta);
        };
    }
}
