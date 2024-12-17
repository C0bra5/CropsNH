package com.gtnewhorizon.cropsnh.init;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.gtnewhorizon.cropsnh.handler.ConfigurationHandler;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    /** Will be replaced with all the custom woods in CustomWood recipes */
    public static final ItemStack REFERENCE = new ItemStack(net.minecraft.init.Blocks.planks, 1);

    /** Holds all the custom woods for CustomWood items, will get filled on init() */
    private static List<ItemStack> woodList;

    public static void init() {
        // crop item
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(Blocks.blockCrop, ConfigurationHandler.cropsPerCraft),
                "ss",
                "ss",
                's',
                "stickWood"));
        if (ConfigurationHandler.cropsPerCraft == 3) {
            GameRegistry.addShapelessRecipe(
                new ItemStack(net.minecraft.init.Items.stick, 6 / ConfigurationHandler.cropsPerCraft),
                new ItemStack(Blocks.blockCrop),
                new ItemStack(Blocks.blockCrop));
        } else {
            GameRegistry.addShapelessRecipe(
                new ItemStack(net.minecraft.init.Items.stick, 4 / ConfigurationHandler.cropsPerCraft),
                new ItemStack(Blocks.blockCrop));
        }
        // TODO: add magnifying glass recipe (copy plant lens)
        // TODO: add spade recipe (copy crop++ spade)
    }
}