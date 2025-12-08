package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.reference.Names;
import com.gtnewhorizon.cropsnh.tileentity.multi.MTEIndustrialFarm;

import cpw.mods.fml.common.LoaderException;
import gregtech.api.enums.VoltageIndex;

public class BlockFertilizerUnit extends CropsNHBlockIndustrialFarmTiredComponent {

    public final static int MAX_UPGRADE_COUNT = 1;

    public BlockFertilizerUnit() {
        super(
            Names.Objects.fertilizerUnit,
            "Fertilizer Unit",
            CropsNHItemList.FertilizerUnit_MV,
            CropsNHItemList.FertilizerUnit_HV,
            CropsNHItemList.FertilizerUnit_EV,
            CropsNHItemList.FertilizerUnit_IV,
            CropsNHItemList.FertilizerUnit_LuV,
            CropsNHItemList.FertilizerUnit_ZPM,
            CropsNHItemList.FertilizerUnit_UV,
            CropsNHItemList.FertilizerUnit_UHV,
            CropsNHItemList.FertilizerUnit_UEV,
            CropsNHItemList.FertilizerUnit_UIV,
            CropsNHItemList.FertilizerUnit_UMV,
            CropsNHItemList.FertilizerUnit_UXV);
    }

    private final static int MIN_TIER = VoltageIndex.MV;
    private final static int MAX_TIER = VoltageIndex.UXV;
    /** The amount of ticks needed to consume all the fertilizer given to a crop, main scalar for consumption speed */
    private final static int CONSUMPTION_TIME = 2560;

    private final static int[] CONSUMPTION_LOOKUP;

    static {
        // runtime validation for future proofing
        if (MAX_TIER < MIN_TIER) {
            throw new LoaderException(
                String.format("MIN_TIER (%d) should be lower than MAX_TIER (%s)", MIN_TIER, MAX_TIER));
        }
        CONSUMPTION_LOOKUP = new int[MAX_TIER - MIN_TIER];
        // calculate the consumption rate scalar
        double consumptionScalar = (1.0d / ((double) CONSUMPTION_TIME / (double) MTEIndustrialFarm.CYCLE_DURATION));
        for (int i = 0; i < CONSUMPTION_LOOKUP.length; i++) {
            // scalar amount is computed from max seed capacity.
            CONSUMPTION_LOOKUP[i] = (int) Math.ceil(BlockSeedBed.getCapacity(i + MIN_TIER) * consumptionScalar);
        }
    }

    public static int getFertilizerConsumptionPerCycle(int tier) {
        if (tier < MIN_TIER || MAX_TIER < tier) {
            throw new IndexOutOfBoundsException(
                String.format("tier should be between %d and %d (was %d)", MIN_TIER, MAX_TIER, tier));
        }
        return CONSUMPTION_LOOKUP[tier - MIN_TIER];
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        // specific
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.0"));
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.1"));
        if (advancedTooltips) {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.2.adv"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.3.adv"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.4.adv"));
        } else {
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.2"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.3"));
            tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.growthAccelerationUnit.4"));
        }
        // generic
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.upgrade_must_match_seed_bed"));
        tooltip
            .add(StatCollector.translateToLocalFormatted("cropsnh_tooltip.upgrade_count_limited", MAX_UPGRADE_COUNT));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        super.registerBlockIcons(aIconRegister);
        this.mBottomIcon = this.mTopIcon = aIconRegister.registerIcon("cropsnh:industrialFarm/fertilizerUnit");
    }
}
