package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.reference.Names;

public class BlockGrowthAccelerationUnit extends CropsNHBlockIndustrialFarmTiredComponent {

    public BlockGrowthAccelerationUnit() {
        super(
            Names.Objects.growthAccelerationUnit,
            "Growth Acceleration Unit",
            CropsNHItemList.GrowthAccelerationUnit_MV,
            CropsNHItemList.GrowthAccelerationUnit_HV,
            CropsNHItemList.GrowthAccelerationUnit_EV,
            CropsNHItemList.GrowthAccelerationUnit_IV,
            CropsNHItemList.GrowthAccelerationUnit_LuV,
            CropsNHItemList.GrowthAccelerationUnit_ZPM,
            CropsNHItemList.GrowthAccelerationUnit_UV,
            CropsNHItemList.GrowthAccelerationUnit_UHV,
            CropsNHItemList.GrowthAccelerationUnit_UEV,
            CropsNHItemList.GrowthAccelerationUnit_UIV,
            CropsNHItemList.GrowthAccelerationUnit_UMV,
            CropsNHItemList.GrowthAccelerationUnit_UXV);
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        // specific
        if (advancedTooltips) {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.0.adv"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.1.adv"));
        } else {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.0"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.1"));
        }
        // generic
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.upgradeTierMustMatchSeedBed"));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        super.registerBlockIcons(aIconRegister);
        this.mBottomIcon = this.mTopIcon = aIconRegister.registerIcon("cropsnh:industrialFarm/growthAccelerationUnit");
    }
}
