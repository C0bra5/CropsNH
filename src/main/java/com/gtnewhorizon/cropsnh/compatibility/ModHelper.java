package com.gtnewhorizon.cropsnh.compatibility;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gtnewhorizon.cropsnh.blocks.BlockCropSticks;
import com.gtnewhorizon.cropsnh.compatibility.NEI.NEIHelper;
import com.gtnewhorizon.cropsnh.compatibility.waila.WailaHelper;
import com.gtnewhorizon.cropsnh.handler.ConfigurationHandler;
import com.gtnewhorizon.cropsnh.tileentity.TileEntityCrop;

import cpw.mods.fml.common.Loader;

public abstract class ModHelper {

    /** HashMap holding all ModHelpers, with the respective mod id as key */
    private static final HashMap<String, ModHelper> modHelpers = new HashMap<>();
    /** HashMap holding all custom tools, with the correct mod helper as value */
    private static final HashMap<Item, ModHelper> modTools = new HashMap<>();

    /** Method to create only one instance for each mod helper */
    private static ModHelper createInstance(Class<? extends ModHelper> clazz) {
        ModHelper helper = null;
        try {
            helper = clazz.newInstance();
        } catch (Exception e) {
            if (ConfigurationHandler.debug) {
                e.printStackTrace();
            }
        }
        if (helper != null) {
            modHelpers.put(helper.modId(), helper);
        }
        return helper;
    }

    /**
     * Checks if integration for this mod id is allowed, meaning the mod is present, and integration is allowed in the
     * config
     */
    public static boolean allowIntegration(String modId) {
        if (Loader.isModLoaded(modId)) {
            ModHelper helper = modHelpers.get(modId);
            if (helper != null) {
                return helper.allowIntegration();
            } else {
                return ConfigurationHandler.enableModCompatibility(modId);
            }
        }
        return false;
    }

    /**
     * Checks if integration for this mod id is allowed, meaning the mod is present, and integration is allowed in the
     * config
     */
    public final boolean allowIntegration() {
        String id = this.modId();
        return Loader.isModLoaded(id) && ConfigurationHandler.enableModCompatibility(id);
    }

    /** Checks if this item has custom behaviour when used on crops */
    public static boolean isRightClickHandled(Item tool) {
        return modTools.containsKey(tool);
    }

    /**
     * static method, called when the item contained in the ItemStack has custom behaviour when used on crops.
     * delegates the call to useTool(World world, int x, int y, int z, EntityPlayer player, ItemStack stack, BlockCrop
     * block, TileEntityCrop crop) on the correct ModHelper.
     *
     * @param world  the World object for the crop
     * @param x      the x-coordinate for the crop
     * @param y      the y-coordinate for the crop
     * @param z      the z-coordinate for the crop
     * @param player the EntityPlayer object interacting with the crop, might be null if done trough automation
     * @param stack  the ItemStack holding the Item
     * @param block  the BlockCrop instance
     * @param crop   the TileEntity being interacted with
     *
     * @return true to consume the right click
     */
    public static boolean handleRightClickOnCrop(World world, int x, int y, int z, EntityPlayer player, ItemStack stack,
        BlockCropSticks block, TileEntityCrop crop) {
        return isRightClickHandled(stack.getItem()) && modTools.get(stack.getItem())
            .useTool(world, x, y, z, player, stack, block, crop);
    }

    /**
     * called when the item contained in the ItemStack has custom behaviour when used on crops
     *
     * @param world  the World object for the crop
     * @param x      the x-coordinate for the crop
     * @param y      the y-coordinate for the crop
     * @param z      the z-coordinate for the crop
     * @param player the EntityPlayer object interacting with the crop, might be null if done trough automation
     * @param stack  the ItemStack holding the Item
     * @param block  the BlockCrop instance
     * @param crop   the TileEntity being interacted with
     *
     * @return true to consume the right click
     */
    protected boolean useTool(World world, int x, int y, int z, EntityPlayer player, ItemStack stack,
        BlockCropSticks block, TileEntityCrop crop) {
        return false;
    }

    /** returns a List containing every Item that should have custom behaviour when used on crops */
    protected List<Item> getTools() {
        return null;
    }

    /** called during the pre-initialization phase of FML's mod loading cycle */
    protected void onPreInit() {}

    /** called during the initialization phase of FML's mod loading cycle */
    protected void onInit() {}

    /**
     * called during the post-initialization phase of FML's mod loading cycle to register all CropPlants for this mod
     */
    protected void initPlants() {}

    /** called during the post-initialization phase of FML's mod loading cycle */
    protected void onPostInit() {}

    /** returns the mod id for this mod */
    protected abstract String modId();

    /** calls the init() method for all mod helpers which have their mod loaded and compatibility enabled */
    public static void initHelpers() {
        for (ModHelper helper : modHelpers.values()) {
            String id = helper.modId();
            boolean flag = Loader.isModLoaded(id) && ConfigurationHandler.enableModCompatibility(id);
            if (flag) {
                helper.onInit();
            }
        }
    }

    /** calls the onPreInit() method for all mod helpers which have their mod loaded and compatibility enabled */
    public static void preInit() {
        for (ModHelper helper : modHelpers.values()) {
            String id = helper.modId();
            boolean flag = Loader.isModLoaded(id) && ConfigurationHandler.enableModCompatibility(id);
            if (flag) {
                helper.onPreInit();
            }
        }
    }

    /** calls the initPlants() method for all mod helpers which have their mod loaded and compatibility enabled */
    public static void initModPlants() {
        for (ModHelper helper : modHelpers.values()) {
            String id = helper.modId();
            boolean flag = Loader.isModLoaded(id) && ConfigurationHandler.enableModCompatibility(id);
            if (flag) {
                helper.initPlants();
            }
        }
    }

    /** calls the postInit() method for all mod helpers which have their mod loaded and compatibility enabled */
    public static void postInit() {
        for (ModHelper helper : modHelpers.values()) {
            String id = helper.modId();
            boolean flag = Loader.isModLoaded(id) && ConfigurationHandler.enableModCompatibility(id);
            if (flag) {
                helper.onPostInit();
                List<Item> tools = helper.getTools();
                if (tools != null) {
                    for (Item tool : tools) {
                        if (tool != null) {
                            modTools.put(tool, helper);
                        }
                    }
                }
            }
        }
    }

    /** method holding all ModHelper classes */
    @SuppressWarnings("unchecked")
    public static void findHelpers() {
        Class[] classes = { WailaHelper.class, NEIHelper.class };
        for (Class<? extends ModHelper> clazz : classes) {
            if (ModHelper.class.isAssignableFrom(clazz)) {
                createInstance(clazz);
            }
        }
    }
}
