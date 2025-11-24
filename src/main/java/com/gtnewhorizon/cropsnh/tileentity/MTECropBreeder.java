package com.gtnewhorizon.cropsnh.tileentity;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_GLOW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.ICropMutation;
import com.gtnewhorizon.cropsnh.api.ISeedStats;
import com.gtnewhorizon.cropsnh.farming.SeedData;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.MutationRegistry;
import com.gtnewhorizon.cropsnh.init.CropsNHFluids;
import com.gtnewhorizon.cropsnh.init.CropsNHUITextures;
import com.gtnewhorizon.cropsnh.items.ItemGenericSeed;
import com.gtnewhorizon.cropsnh.loaders.CropsNHGTRecipeMaps;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTECropBreeder extends MTEBasicMachine {

    private static final int AMPERAGE = 1;
    private static final int MIN_INPUT_SLOT_COUNT = 3;
    private static final int MAX_INPUT_SLOT_COUNT = 6;
    private static final int OUTPUT_SLOT_COUNT = 1;

    private static int getInputSlotCount(int tier) {
        return tier < VoltageIndex.HV ? MIN_INPUT_SLOT_COUNT : MAX_INPUT_SLOT_COUNT;
    }

    public static final ConcurrentHashMap<Fluid, Integer> ALLOWED_LIQUID_FERTILIZER = new ConcurrentHashMap<>();

    public static void init() {
        // allowed liquid fertilizer
        ALLOWED_LIQUID_FERTILIZER.putIfAbsent(CropsNHFluids.enrichedFertilizer, 10000);
    }

    public MTECropBreeder(int aID, int aTier, String aNameRegional) {
        super(
            aID,
            String.format("basicmachine.cropbreeder.tier.%02d", aTier),
            aNameRegional,
            aTier,
            AMPERAGE,
            new String[] { "It can duplicate seeds!", "Uses 100L of Enriched Fertiliser per stat point on the seed." },
            getInputSlotCount(aTier),
            OUTPUT_SLOT_COUNT,
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_SCANNER_GLOW)
                    .glow()
                    .build()));
    }

    public MTECropBreeder(String mName, byte mTier, String[] mDescriptionArray, ITexture[][][] mTextures) {
        super(mName, mTier, AMPERAGE, mDescriptionArray, mTextures, getInputSlotCount(mTier), OUTPUT_SLOT_COUNT);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTECropBreeder(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return CropsNHGTRecipeMaps.fakeCropBreederRecipeMap;
    }

    @Override
    public int checkRecipe(boolean skipOC) {
        // ensure that the fertilizer fluid exists and that it's the right kind.
        if (this.mFluid == null || this.mFluid.getFluid() == null) {
            return DID_NOT_FIND_RECIPE;
        }

        // allow zero drain for future proofing.
        int drainPerTier = ALLOWED_LIQUID_FERTILIZER.getOrDefault(this.mFluid.getFluid(), -1);
        if (drainPerTier < 0) {
            return DID_NOT_FIND_RECIPE;
        }

        // try to identify a usable seed and catalyst
        HashMap<ICropCard, SeedData> breedingParents = new HashMap<>(4);
        ItemStack[] catalystSlots = new ItemStack[this.mInputSlotCount];
        for (int i = 0; i < this.mInputSlotCount; i++) {
            ItemStack tStack = this.getInputAt(i);
            var seedData = isValidSeeds(tStack);
            if (seedData != null) {
                if (!breedingParents.containsKey(seedData.crop)) {
                    breedingParents.put(seedData.crop, seedData);
                }
                catalystSlots[i] = null;
            } else {
                catalystSlots[i] = tStack;
            }
        }

        // mutations should never have less than 2 parents
        if (breedingParents.size() < 2) {
            return DID_NOT_FIND_RECIPE;
        }

        // find potential mutations
        ArrayList<ICropCard> parentList = new ArrayList<>(breedingParents.keySet());
        List<ICropMutation> deterministicMutations = MutationRegistry.instance
            .getPossibleDeterministicMutations(parentList);
        if (deterministicMutations == null || deterministicMutations.isEmpty()) {
            return DID_NOT_FIND_RECIPE;
        }

        // prioritize mutations with more parents ... may the largest harem win.
        deterministicMutations.sort(
            Comparator.comparing(ICropMutation::getParentCount)
                .reversed());

        // try to find the first matching recipe
        for (ICropMutation mutation : deterministicMutations) {
            // abort early if the amount of fluid in the tank can't create this crop.
            int amountOfFluidToConsume = drainPerTier * mutation.getOutput()
                .getTier();
            if (amountOfFluidToConsume > this.mFluid.amount) continue;

            // abort early if the output doesn't fit.
            ItemStack newSeed = mutation.getOutput()
                .getSeedItem(getNewSeedStats(mutation, breedingParents));
            newSeed.stackSize = 1;
            if (!this.canOutput(newSeed)) continue;

            // check if we can breed
            int[] itemsToConsume = mutation.canBreed(parentList, this.getBaseMetaTileEntity(), catalystSlots);
            if (itemsToConsume == null) continue;

            // check if we can run the recipe
            if (!skipOC) {
                this.calculateOverclockedNess(
                    mutation.getBreedingMachineRecipeEUt(),
                    mutation.getBreedingMachineRecipeDuration());
                // In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            }

            // everything seems good consume and return success
            this.mFluid.amount -= amountOfFluidToConsume;
            for (int i = 0; i < itemsToConsume.length; i++) {
                if (itemsToConsume[i] <= 0) continue;
                this.getInputAt(i).stackSize -= itemsToConsume[i];
            }
            for (ICropCard cc : mutation.getParents()) {
                breedingParents.get(cc).stack.stackSize -= 1;
            }
            this.mOutputItems[0] = newSeed;

            return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return DID_NOT_FIND_RECIPE;
    }

    private static ISeedStats getNewSeedStats(ICropMutation mutation, HashMap<ICropCard, SeedData> breedingParents) {
        int[] newStats = new int[] { 0, 0, 0 };
        for (ICropCard parent : mutation.getParents()) {
            ISeedStats parentStats = breedingParents.get(parent)
                .getStats();
            newStats[0] += parentStats.getGrowth();
            newStats[1] += parentStats.getGain();
            newStats[2] += parentStats.getResistance();
        }
        int parentCount = mutation.getParentCount();
        return new SeedStats(
            (byte) (newStats[0] / parentCount),
            (byte) (newStats[1] / parentCount),
            (byte) (newStats[2] / parentCount),
            true);
    }

    private static SeedData isValidSeeds(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack) || !(aStack.getItem() instanceof ItemGenericSeed)) return null;
        // check that it's a crop card and that it can cross.
        ICropCard cc = CropRegistry.instance.get(aStack);
        if (cc == null || cc.getCrossingThreshold() < 0.0f) return null;
        // fail if the crop isn't analyzed
        SeedStats stats = SeedStats.getStatsFromStack(aStack);
        if (stats == null || !stats.isAnalyzed()) return null;
        return new SeedData(cc, stats, aStack);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return super.isFluidInputAllowed(aFluid) && aFluid.getFluid() == CropsNHFluids.enrichedFertilizer;
    }

    @Override
    public int getCapacity() {
        return getCapacityForTier(mTier);
    }

    private static final FallbackableUITexture progressBarTexture = GTUITextures
        .fallbackableProgressbar("crop_replicator", GTUITextures.PROGRESSBAR_ARROW);

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(progressBarTexture)
            .build();
    }

    @Override
    protected SlotWidget createItemOutputSlot(int index, IDrawable[] backgrounds, Pos2d pos) {
        if (index == 0) {
            return (SlotWidget) super.createItemOutputSlot(index, backgrounds, pos)
                .setBackground(getGUITextureSet().getItemSlot(), CropsNHUITextures.OVERLAY_SLOT_SEED);
        }
        return (SlotWidget) super.createItemOutputSlot(index, backgrounds, pos)
            .setBackground(getGUITextureSet().getItemSlot());
    }
}
