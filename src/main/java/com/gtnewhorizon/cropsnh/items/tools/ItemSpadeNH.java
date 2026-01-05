package com.gtnewhorizon.cropsnh.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizon.cropsnh.api.ICropLeftClickHandler;
import com.gtnewhorizon.cropsnh.api.ICropRightClickHandler;
import com.gtnewhorizon.cropsnh.api.ICropStickTile;
import com.gtnewhorizon.cropsnh.items.ItemCropsNH;
import com.gtnewhorizon.cropsnh.reference.Reference;
import com.gtnewhorizon.cropsnh.utility.LogHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Tool to uproot weeds.
 * Comes in a wooden and iron variant.
 */
public abstract class ItemSpadeNH extends ItemCropsNH implements ICropLeftClickHandler, ICropRightClickHandler {

    public ItemSpadeNH() {
        super();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        list.add(StatCollector.translateToLocal(Reference.MOD_ID_LOWER + "_tooltip." + this.getInternalName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        LogHelper.debug("registering icon for: " + this.getInternalName());
        this.itemIcon = reg.registerIcon(
            this.getUnlocalizedName()
                .substring(
                    this.getUnlocalizedName()
                        .indexOf('.') + 1));
    }

    @Override
    public boolean onLeftClick(ICropStickTile te, EntityPlayer player, ItemStack heldItem) {
        return this.doWork(te, true);
    }

    @Override
    public boolean onRightClick(World world, ICropStickTile te, EntityPlayer player, ItemStack heldItem) {
        if (world.isRemote) return true;
        return this.doWork(te, false);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    private boolean doWork(ICropStickTile te, boolean clearAfter) {
        if (te.isCrossCrop()) return false;
        // if it's weeds clear them
        if (te.hasWeed()) {
            // drop tall grass if it's mature
            if (te.isMature()) {
                te.dropItem(new ItemStack(Blocks.tallgrass, 1, 1));
            }
            te.clear();
            return true;
        }
        // else only drop seeds if we can harvest the crops.
        if (te.doPlayerHarvest()) {
            // get the seeds
            ItemStack seedDrop = te.getSeedStack();
            if (seedDrop != null) {
                seedDrop.stackSize = getSeedCount(
                    te.getSeed()
                        .getStats()
                        .getResistance());
                if (seedDrop.stackSize > 0) {
                    te.dropItem(seedDrop);
                }
            }
            if (clearAfter) te.clear();
            else te.setGrowthProgress(0);
            return true;
        }
        return true;
    }

    protected abstract int getSeedCount(int resist);
}
