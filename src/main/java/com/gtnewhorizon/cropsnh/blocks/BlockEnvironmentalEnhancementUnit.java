package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.reference.Names;

public class BlockEnvironmentalEnhancementUnit extends CropsNHBlockIndustrialFarmTiredComponent {

    public final static int MAX_UPGRADE_COUNT = 2;

    public BlockEnvironmentalEnhancementUnit() {
        super(
            Names.Objects.environmentalEnhancementUnit,
            "Environmental Enhancement Unit",
            CropsNHItemList.EnvironmentalEnhancementUnit_MV,
            CropsNHItemList.EnvironmentalEnhancementUnit_HV,
            CropsNHItemList.EnvironmentalEnhancementUnit_EV,
            CropsNHItemList.EnvironmentalEnhancementUnit_IV,
            CropsNHItemList.EnvironmentalEnhancementUnit_LuV,
            CropsNHItemList.EnvironmentalEnhancementUnit_ZPM,
            CropsNHItemList.EnvironmentalEnhancementUnit_UV,
            CropsNHItemList.EnvironmentalEnhancementUnit_UHV,
            CropsNHItemList.EnvironmentalEnhancementUnit_UEV,
            CropsNHItemList.EnvironmentalEnhancementUnit_UIV,
            CropsNHItemList.EnvironmentalEnhancementUnit_UMV,
            CropsNHItemList.EnvironmentalEnhancementUnit_UXV);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        // specific
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.environmentalEnhancementUnit.0"));
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.environmentalEnhancementUnit.1"));
        if (advancedTooltips) {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.environmentalEnhancementUnit.2.adv"));
        } else {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.environmentalEnhancementUnit.2"));
        }
        // generic
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.upgrade_must_match_seed_bed"));
        tooltip
            .add(StatCollector.translateToLocalFormatted("cropsnh_tooltip.upgrade_count_limited", MAX_UPGRADE_COUNT));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        super.registerBlockIcons(aIconRegister);
        this.mBottomIcon = this.mTopIcon = aIconRegister
            .registerIcon("cropsnh:industrialFarm/environmentalEnhancementUnit");
    }
}
