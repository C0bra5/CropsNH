package com.gtnewhorizon.cropsnh.utility;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.farming.SeedData;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.items.ItemGenericSeed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;

public abstract class CropsNHUtils {

    public static void deduplicateItemList(List<ItemStack> stacks) {
        if (stacks.isEmpty()) return;
        // yank out null items for good measure
        stacks.removeIf(Objects::isNull);
        final HashSet<String> deduplicator = new HashSet<>();
        stacks.removeIf(x -> !deduplicator.add(x.toString()));
    }

    /**
     * Does its best to turn an item into a block.
     *
     * @param block the block to convert
     * @return The item or null if none is found.
     */
    public static Item getItemFromBlock(Block block) {
        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            try {
                // should catch things like the skull block
                item = block.getItem(null, 0, 0, 0);
            } catch (Exception ignored) {
                return null;
            }
        }
        return item;
    }

    /**
     * Does its best to turn a block into an item.
     *
     * @param stack The stack containing the item to convert.
     * @return The block or null if none is found.
     */
    public static Block getBlockFromItem(ItemStack stack) {
        if (stack == null) return null;
        return getBlockFromItem(stack.getItem());
    }

    /**
     * Does its best to turn a block into an item.
     *
     * @param item the block to convert
     * @return The block or null if none is found.
     */
    public static Block getBlockFromItem(Item item) {
        if (item == null) {
            return null;
        } else if (item instanceof ItemSkull) {
            return Blocks.skull;
        } else if (item instanceof ItemBlock) {
            return ((ItemBlock) item).field_150939_a;
        } else {
            return Block.getBlockFromItem(item);
        }
    }

    /**
     * Attempts to detect if the stack contains analyzed seeds.
     *
     * @param aStack The stack to validate
     * @return Null if nothing was found else the seed data for the stack.
     */
    public static @Nullable ISeedData getAnalyzedSeedData(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack) || aStack.stackSize <= 0 || !(aStack.getItem() instanceof ItemGenericSeed))
            return null;
        // check that it's a crop card and that it can cross.
        ICropCard cc = CropRegistry.instance.get(aStack);
        if (cc == null || cc.getCrossingThreshold() < 0.0f) return null;
        // fail if the crop isn't analyzed
        SeedStats stats = SeedStats.getStatsFromStack(aStack);
        if (stats == null || !stats.isAnalyzed()) return null;
        return new SeedData(cc, stats, aStack);
    }

    private static IIcon MISSING_TEXTURE = null;

    @SideOnly(Side.CLIENT)
    public static IIcon getMissingTexture() {
        if (MISSING_TEXTURE == null) {
            MISSING_TEXTURE = ((TextureMap) Minecraft.getMinecraft()
                .getTextureManager()
                .getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }
        return MISSING_TEXTURE;
    }

    public static boolean isStackValid(ItemStack aStack) {
        return (aStack != null) && aStack.getItem() != null && aStack.stackSize > 0;
    }

    public static boolean isStackInvalid(ItemStack aStack) {
        return aStack == null || aStack.getItem() == null || aStack.stackSize <= 0;
    }

    public static boolean isStackValid(FluidStack aStack) {
        return (aStack != null) && aStack.getFluid() != null && aStack.amount > 0;
    }

    public static boolean isStackInvalid(FluidStack aStack) {
        return aStack == null || aStack.getFluid() == null || aStack.amount <= 0;
    }

    /**
     * Copies the stack and returns a new stack with the given stack size.
     *
     * @param aStack The stack to copy.
     * @param aSize  The size of the new stack.
     * @return The copied stack with the requested size.
     */
    public static ItemStack copyStackWithSize(@Nonnull ItemStack aStack, int aSize) {
        if (isStackInvalid(aStack)) return null;
        ItemStack ret = aStack.copy();
        ret.stackSize = aSize;
        return ret;
    }

    public static int getItemMeta(ItemStack aStack) {
        return Items.feather.getDamage(aStack);
    }
}
