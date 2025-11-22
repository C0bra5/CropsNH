package com.gtnewhorizon.cropsnh.compatibility.waila;

import com.gtnewhorizon.cropsnh.tileentity.TileEntityCrop;

import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.Mods;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistry {

    public static void onInit() {
        if (!Mods.Waila.isModLoaded()) return;
        FMLInterModComms.sendMessage(
            Mods.Waila.ID,
            "register",
            "com.gtnewhorizon.cropsnh.compatibility.waila.WailaRegistry.initWaila");
    }

    public static void initWaila(IWailaRegistrar registry) {
        if (!Mods.Waila.isModLoaded()) return;
        // All blocks.
        IWailaDataProvider cropStickProvider = new CropStickWailaProvider();
        registry.registerBodyProvider(cropStickProvider, TileEntityCrop.class);
        registry.registerStackProvider(cropStickProvider, TileEntityCrop.class);
        registry.registerNBTProvider(cropStickProvider, TileEntityCrop.class);
    }

}
