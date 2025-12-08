package com.gtnewhorizon.cropsnh.blocks;

import java.util.List;

import com.gtnewhorizon.cropsnh.loaders.FertilizerLoader;
import com.gtnewhorizon.cropsnh.tileentity.TileEntityCrop;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
    public final static double BASE_POWER_INCREASE = 0.5d;
    public final static double GROWTH_SPEED_MULTIPLIER = 0.5d;
    public final static double HARVEST_ROUND_BONUS = 0.5d;

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
    /** The amount of liquid fertilizer a single fertilizer item should create */
    private final static int FERTILIZER_ITEM_LIQUID_OUTPUT = 144;

    private final static int[] CONSUMPTION_LOOKUP;

    static {
        // runtime validation for future proofing
        if (MAX_TIER < MIN_TIER) {
            throw new LoaderException(
                String.format("MIN_TIER (%d) should be lower than MAX_TIER (%s)", MIN_TIER, MAX_TIER));
        }
        CONSUMPTION_LOOKUP = new int[MAX_TIER - MIN_TIER + 1];
        // calculate the consumption rate scalar
        for (int i = 0; i < CONSUMPTION_LOOKUP.length; i++) {
            // scalar amount is computed from max seed capacity.
            CONSUMPTION_LOOKUP[i] = (int) Math.ceil(BlockSeedBed.getCapacity(i + MIN_TIER) * (double)FERTILIZER_ITEM_LIQUID_OUTPUT / (TileEntityCrop.TICK_RATE * FertilizerLoader.FERTILIZER_ITEM_POTENCY) * MTEIndustrialFarm.CYCLE_DURATION);
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
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.fertilizerUnit.0"));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "cropsnh_tooltip.fertilizerUnit.1",
                getFertilizerConsumptionPerCycle(Items.feather.getDamage(stack))));
        if (advancedTooltips) {
            tooltip.add(
                StatCollector
                    .translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.2.adv", GROWTH_SPEED_MULTIPLIER * 100));
            tooltip.add(
                StatCollector
                    .translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.3.adv", HARVEST_ROUND_BONUS));
            tooltip.add(
                StatCollector
                    .translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.4.adv", BASE_POWER_INCREASE * 100));
        } else {
            tooltip.add(
                StatCollector.translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.2", GROWTH_SPEED_MULTIPLIER * 100));
            tooltip.add(
                StatCollector.translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.3", HARVEST_ROUND_BONUS));
            tooltip.add(
                StatCollector.translateToLocalFormatted("cropsnh_tooltip.fertilizerUnit.4", BASE_POWER_INCREASE * 100));
        }
        // generic
        tooltip.add(StatCollector.translateToLocal("cropsnh_tooltip.upgradeTierMustMatchSeedBed"));
        tooltip.add(StatCollector.translateToLocalFormatted("cropsnh_tooltip.upgradeCountLimited", MAX_UPGRADE_COUNT));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        super.registerBlockIcons(aIconRegister);
        this.mBottomIcon = this.mTopIcon = aIconRegister.registerIcon("cropsnh:industrialFarm/fertilizerUnit");
    }
}
