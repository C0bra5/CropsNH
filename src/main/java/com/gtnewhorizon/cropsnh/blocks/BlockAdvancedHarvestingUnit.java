package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.reference.Names;

public class BlockAdvancedHarvestingUnit extends CropsNHBlockIndustrialFarmTiredComponent {

    public final static int MAX_UPGRADE_COUNT = 2;

    public BlockAdvancedHarvestingUnit() {
        super(
            Names.Objects.advancedHarvestingUnit,
            "Advanced Harvesting Unit",
            CropsNHItemList.AdvancedHarvestingUnit_MV,
            CropsNHItemList.AdvancedHarvestingUnit_HV,
            CropsNHItemList.AdvancedHarvestingUnit_EV,
            CropsNHItemList.AdvancedHarvestingUnit_IV,
            CropsNHItemList.AdvancedHarvestingUnit_LuV,
            CropsNHItemList.AdvancedHarvestingUnit_ZPM,
            CropsNHItemList.AdvancedHarvestingUnit_UV,
            CropsNHItemList.AdvancedHarvestingUnit_UHV,
            CropsNHItemList.AdvancedHarvestingUnit_UEV,
            CropsNHItemList.AdvancedHarvestingUnit_UIV,
            CropsNHItemList.AdvancedHarvestingUnit_UMV,
            CropsNHItemList.AdvancedHarvestingUnit_UXV);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        // specific
        if (advancedTooltips) {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.advancedHarvestingUnit.0.adv"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.advancedHarvestingUnit.1.adv"));
        } else {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.advancedHarvestingUnit.0"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.advancedHarvestingUnit.1"));
        }
        // generic
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.upgrade_must_match_seed_bed"));
        tooltip
            .add(StatCollector.translateToLocalFormatted("cropsnh_tooltip.upgrade_count_limited", MAX_UPGRADE_COUNT));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        super.registerBlockIcons(aIconRegister);
        this.mBottomIcon = this.mTopIcon = aIconRegister.registerIcon("cropsnh:industrialFarm/advancedHarvestingUnit");
    }
}
