package com.gtnewhorizon.cropsnh.tileentity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.CombinedInvWrapper;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.cropsnh.api.IGrowthRequirement;
import com.gtnewhorizon.cropsnh.api.ISeedData;
import com.gtnewhorizon.cropsnh.blocks.BlockAdvancedHarvestingUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockEnvironmentalEnhancementUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockFertilizerUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockOverclockedGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockSeedBed;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.farming.requirements.BlockUnderRequirement;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
import com.gtnewhorizon.cropsnh.reference.Data;
import com.gtnewhorizon.cropsnh.utility.CropsNHUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import kubatech.api.eig.EIGDropTable;

public class MTEIndustrialFarm extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFarm>
    implements ISurvivalConstructable {

    /** The duration of the production cycle in seconds. */
    public static final int CYCLE_DURATION = 5 * SECONDS;

    private final static String NBT_INVENTORY_TAG = "mIFInventory";

    /** The default mode, used to insert seed and under-block */
    public static final int MODE_INPUT = 0;
    /** The mode that generates resources. */
    public static final int MODE_FARM = 1;
    /** Used to safely eject the seeds and under-blocks to the output bus. */
    public static final int MODE_OUTPUT = 2;

    /** How many times the output and water/fertilizer consumption of the multi should be doubled. */
    public int mExpectedOCs = 0;
    /** How much power each recipe is expected to use */
    public long mExpectedEUt = 0;
    /** The tier of glass used to build the multi, used to limit the power hatch tiers. */
    public int mGlassTier = -1;
    /** The tier of the upgrades applied to the multi. */
    public int mUpgradeTier = -1;
    /** The number of seeds and under-blocks that can be stored in the controller. */
    public int mSeedCapacity = 0;
    /** The number of environmental enhancement units installed on the multi. */
    public int mEnvironmentalEnhancementUnitCount = 0;
    /** The number of growth acceleration units installed on the multi. */
    public int mGrowthAccelerationUnitCount = 0;
    /** The number of fertilizer units installed on the multi. */
    public int mFertilizerUnitCount = 0;
    /** The number of advanced harvesting units installed on the multi. */
    public int mAdvancedHarvestingUnitCount = 0;
    /** The number of overclocked growth acceleration units installed on the multi. */
    public int mOverclockedGrowthAccelerationUnitCount = 0;

    /** The tracker for the drop progress */
    public EIGDropTable mOutputTracker = new EIGDropTable();
    /** ItemStack handler for the custom slots. */
    public MTEIndustrialFarmItemStackHandler mIFStackHandler = new MTEIndustrialFarmItemStackHandler(this);
    /** Multi-Inv wrapper since it needs to respond to both the controller slot and the custom slots. */
    private final IItemHandlerModifiable mInvWrapper;

    // region structure
    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";
    private static final int CASING_INDEX = 63;
    private static final int MIN_CASING_TIER = VoltageIndex.MV;
    private static final int MAX_CASING_TIER = VoltageIndex.UXV;
    private static final int MIN_SLICES = 1;
    private static final int MAX_SLICES = 1 + MAX_CASING_TIER - MIN_CASING_TIER;
    private static final IStructureDefinition<MTEIndustrialFarm> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialFarm>builder()
        .addShape(
            STRUCTURE_PIECE_FIRST,
            transpose(
                new String[][] {
                    // spotless:off
                { " cCc " },
                { "cCCCc" },
                { "cC~Cc" },
                { "c   c" }
                // spotless:on
                }))
        .addShape(
            STRUCTURE_PIECE_LATER,
            transpose(
                new String[][] {
                    // spotless:off
                { " gUg " },
                { "g   g" },
                { "csssc" },
                { "     " }
                // spotless:on
                }))
        .addShape(
            STRUCTURE_PIECE_LAST,
            transpose(
                new String[][] {
                    // spotless:off
                { " cCc " },
                { "cCCCc" },
                { "cCCCc" },
                { "c   c" }
                // spotless:on
                }))
        .addElement(
            'C',
            buildHatchAdder(MTEIndustrialFarm.class)
                .anyOf(InputBus, InputHatch, OutputBus, Maintenance, MultiAmpEnergy.or(Energy))
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(ofBlock(CropsNHBlocks.blockCasings1, 0)))
        .addElement('c', ofBlock(CropsNHBlocks.blockCasings1, 0))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
        .addElement(
            'U',
            ofChain(
                ofFrame(Materials.Wood),
                onElementPass(
                    te -> te.mEnvironmentalEnhancementUnitCount++,
                    chainAllTiredComponents(CropsNHBlocks.blockEnvironmentalEnhancementUnit)),
                onElementPass(
                    te -> te.mGrowthAccelerationUnitCount++,
                    chainAllTiredComponents(CropsNHBlocks.blockGrowthAccelerationUnit)),
                onElementPass(
                    te -> te.mFertilizerUnitCount++,
                    chainAllTiredComponents(CropsNHBlocks.blockFertilizerUnit)),
                onElementPass(
                    te -> te.mAdvancedHarvestingUnitCount++,
                    chainAllTiredComponents(CropsNHBlocks.blockAdvancedHarvestingUnit)),
                onElementPass(
                    te -> te.mOverclockedGrowthAccelerationUnitCount++,
                    chainAllTiredComponents(CropsNHBlocks.blockOverclockedGrowthAccelerationUnit))))
        .addElement('s', chainAllTiredComponents(CropsNHBlocks.blockSeedBed))
        .build();

    private static IStructureElement<MTEIndustrialFarm> chainAllTiredComponents(Block block) {
        Class c = block.getClass();
        return lazy(() -> ofBlocksTiered((aBlock, aMeta) -> {
            if (c.isInstance(aBlock) && aBlock instanceof CropsNHBlockIndustrialFarmTiredComponent tComponent) {
                return tComponent.getTier(aMeta);
            }
            return null;
        },
            ((CropsNHBlockIndustrialFarmTiredComponent) block).getStructureBlocks(),
            -1,
            MTEIndustrialFarm::setUpgradeTier,
            MTEIndustrialFarm::getUpgradeTier));
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
    }

    private static void setUpgradeTier(MTEIndustrialFarm te, Integer tier) {
        te.mUpgradeTier = tier;
    }

    private static Integer getUpgradeTier(MTEIndustrialFarm te) {
        return te.mUpgradeTier;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFarm> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        STRUCTURE_DEFINITION.buildOrHints(
            this,
            stackSize,
            STRUCTURE_PIECE_FIRST,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            0,
            hintsOnly);
        int tSlices = GTUtility.clamp(stackSize.stackSize, MIN_SLICES, MAX_SLICES);
        for (int tSliceIndex = 0; tSliceIndex < tSlices; tSliceIndex++) {
            STRUCTURE_DEFINITION.buildOrHints(
                this,
                stackSize,
                STRUCTURE_PIECE_LATER,
                this.getBaseMetaTileEntity()
                    .getWorld(),
                this.getExtendedFacing(),
                this.getBaseMetaTileEntity()
                    .getXCoord(),
                this.getBaseMetaTileEntity()
                    .getYCoord(),
                this.getBaseMetaTileEntity()
                    .getZCoord(),
                2,
                2,
                -tSliceIndex - 1,
                hintsOnly);
        }
        STRUCTURE_DEFINITION.buildOrHints(
            this,
            stackSize,
            STRUCTURE_PIECE_LAST,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            -tSlices - 1,
            hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int tBuilt = STRUCTURE_DEFINITION.survivalBuild(
            this,
            stackSize,
            STRUCTURE_PIECE_FIRST,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            0,
            elementBudget,
            env,
            false);
        if (tBuilt != -1) return tBuilt;
        int tSlices = GTUtility.clamp(stackSize.stackSize, MIN_SLICES, MAX_SLICES);
        for (int tSliceIndex = 0; tSliceIndex < tSlices; tSliceIndex++) {
            tBuilt = STRUCTURE_DEFINITION.survivalBuild(
                this,
                stackSize,
                STRUCTURE_PIECE_LATER,
                this.getBaseMetaTileEntity()
                    .getWorld(),
                this.getExtendedFacing(),
                this.getBaseMetaTileEntity()
                    .getXCoord(),
                this.getBaseMetaTileEntity()
                    .getYCoord(),
                this.getBaseMetaTileEntity()
                    .getZCoord(),
                2,
                2,
                -tSliceIndex - 1,
                elementBudget,
                env,
                false);
            if (tBuilt != -1) return tBuilt;
        }
        return STRUCTURE_DEFINITION.survivalBuild(
            this,
            stackSize,
            STRUCTURE_PIECE_LAST,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            -tSlices - 1,
            elementBudget,
            env,
            false);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mUpgradeTier = -1;
        this.mGlassTier = -1;
        this.mEnvironmentalEnhancementUnitCount = 0;
        this.mGrowthAccelerationUnitCount = 0;
        this.mFertilizerUnitCount = 0;
        this.mAdvancedHarvestingUnitCount = 0;
        this.mOverclockedGrowthAccelerationUnitCount = 0;
        this.mSeedCapacity = 0;
        boolean tSuccess = STRUCTURE_DEFINITION.check(
            this,
            STRUCTURE_PIECE_FIRST,
            getBaseMetaTileEntity().getWorld(),
            getExtendedFacing(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord(),
            2,
            2,
            0,
            !mMachine);
        if (!tSuccess) return false;

        // check the first slice
        tSuccess = STRUCTURE_DEFINITION.check(
            this,
            STRUCTURE_PIECE_LATER,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            -1,
            !mMachine);
        if (!tSuccess || this.mGlassTier < MIN_CASING_TIER) return false;

        int tSlices = GTUtility.clamp(this.mUpgradeTier - MIN_CASING_TIER + MIN_SLICES, MIN_SLICES, MAX_SLICES);
        for (int tSliceIndex = 1; tSliceIndex < tSlices; tSliceIndex++) {
            tSuccess = STRUCTURE_DEFINITION.check(
                this,
                STRUCTURE_PIECE_LATER,
                this.getBaseMetaTileEntity()
                    .getWorld(),
                this.getExtendedFacing(),
                this.getBaseMetaTileEntity()
                    .getXCoord(),
                this.getBaseMetaTileEntity()
                    .getYCoord(),
                this.getBaseMetaTileEntity()
                    .getZCoord(),
                2,
                2,
                -tSliceIndex - 1,
                !mMachine);
            if (!tSuccess || this.mGlassTier < MIN_CASING_TIER) return false;
        }

        tSuccess = STRUCTURE_DEFINITION.check(
            this,
            STRUCTURE_PIECE_LAST,
            this.getBaseMetaTileEntity()
                .getWorld(),
            this.getExtendedFacing(),
            this.getBaseMetaTileEntity()
                .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            2,
            2,
            -tSlices - 1,
            !mMachine);
        if (!tSuccess || this.mGlassTier < MIN_CASING_TIER) return false;

        if (this.mOutputBusses.size() < 1) return false;
        if (this.mInputHatches.size() < 1) return false;
        if (this.mMaintenanceHatches.size() != 1) return false;
        if (this.mEnergyHatches.size() + this.mExoticEnergyHatches.size() < 1) return false;

        // validate upgrade counts
        if (this.mEnvironmentalEnhancementUnitCount > BlockEnvironmentalEnhancementUnit.MAX_UPGRADE_COUNT
            || this.mFertilizerUnitCount > BlockFertilizerUnit.MAX_UPGRADE_COUNT
            || this.mAdvancedHarvestingUnitCount > BlockAdvancedHarvestingUnit.MAX_UPGRADE_COUNT
            || this.mOverclockedGrowthAccelerationUnitCount > BlockOverclockedGrowthAccelerationUnit.MAX_UPGRADE_COUNT
            || (this.mGrowthAccelerationUnitCount > 0 && this.mOverclockedGrowthAccelerationUnitCount > 0)) {
            return false;
        }

        // validate hatches depending on the presence of the oc upgrade.
        if (this.mOverclockedGrowthAccelerationUnitCount > 0) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
                // validate the tier while we're at it
                if (this.mGlassTier < VoltageIndex.UMV && hatch.mTier > this.mGlassTier) {
                    return false;
                }
            }
        } else if (this.mExoticEnergyHatches.size() != 0) {
            return false;
        }

        // validate normal energy hatch tiers
        for (MTEHatch hatch : this.mEnergyHatches) {
            // probably superfluous but eh. it's not like this will be the perf bottleneck of this machine
            if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                return false;
            }
            if (this.mGlassTier < VoltageIndex.UMV && hatch.mTier > this.mGlassTier) {
                return false;
            }
        }

        // calculate power usage
        // base eu/t should be based on the seedbed/upgrade tier.
        long basePower = GTValues.VP[this.mUpgradeTier], powerUsage = basePower;
        if (this.mEnvironmentalEnhancementUnitCount > 0) {
            powerUsage += basePower * BlockEnvironmentalEnhancementUnit.BASE_POWER_INCREASE
                * this.mEnvironmentalEnhancementUnitCount;
        }
        if (this.mGrowthAccelerationUnitCount > 0) {
            powerUsage += basePower * BlockGrowthAccelerationUnit.BASE_POWER_INCREASE
                * this.mGrowthAccelerationUnitCount;
        }
        if (this.mFertilizerUnitCount > 0) {
            powerUsage += basePower * BlockFertilizerUnit.BASE_POWER_INCREASE * this.mFertilizerUnitCount;
        }
        if (this.mAdvancedHarvestingUnitCount > 0) {
            powerUsage += basePower * BlockAdvancedHarvestingUnit.BASE_POWER_INCREASE
                * this.mAdvancedHarvestingUnitCount;
        }

        if (this.mOverclockedGrowthAccelerationUnitCount > 0) {
            OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(powerUsage)
                .setEUt(this.getMaxInputEu())
                .setDuration(Integer.MAX_VALUE)
                .calculate();
            this.mExpectedOCs = calculator.getPerformedOverclocks();
            this.mExpectedEUt = calculator.getConsumption();
        } else {
            this.mExpectedOCs = 0;
            this.mExpectedEUt = powerUsage;
        }

        this.mSeedCapacity = BlockSeedBed.getCapacity(this.mUpgradeTier);

        return true;
    }

    // endregion structure

    public MTEIndustrialFarm(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.mInvWrapper = new CombinedInvWrapper(this.mIFStackHandler, this.inventoryHandler);
    }

    public MTEIndustrialFarm(String aName) {
        super(aName);
        this.mInvWrapper = new CombinedInvWrapper(this.mIFStackHandler, this.inventoryHandler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFarm(this.mName);
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return this.mInvWrapper;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Industrial Farm")
            .addInfo("Used to grow a crops at an industrial scale.")
            .addInfo("The length of the machine depends on the tier of the seed bed.");

        tt.beginVariableStructureBlock(5, 5, 4, 4, 2 + MIN_SLICES, 2 + MAX_SLICES, false)
            .addGlassEnergyLimitInfo()
            .addInfo(StatCollector.translateToLocal("cropsnh_tooltip.MBTT.multiAmpsWithUpgrade"))
            .addCasingInfoRange("Agricultural Casing", 8 * 2 + MIN_SLICES * 2, 8 * 2 + MAX_SLICES * 2, false)
            .addEnergyHatch("Any Casing", 1)
            .addInputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture casingTexture = Textures.BlockIcons.casingTexturePages[0][CASING_INDEX];
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexture };
    }

    private final static int SLOT_SEED = 0;
    private final static int SLOT_BLOCK_UNDER = 1;
    private final static int SLOT_ENV_CARD_START = 2;

    public ItemStack getSeedStack() {
        return this.mIFStackHandler.getStackInSlot(SLOT_SEED);
    }

    public void setSeedStack(ItemStack aStack) {
        this.mIFStackHandler.setStackInSlot(SLOT_SEED, aStack);
    }

    public boolean canInsertIntoSeedSlot(ItemStack aStack) {
        return this.mIFStackHandler.isItemValid(SLOT_SEED, aStack);
    }

    public ItemStack getBlockUnderStack() {
        return this.mIFStackHandler.getStackInSlot(SLOT_BLOCK_UNDER);
    }

    public void setBlockUnderStack(ItemStack aStack) {
        this.mIFStackHandler.setStackInSlot(SLOT_BLOCK_UNDER, aStack);
    }

    public ItemStack getEnvironmentalModuleStack(int aSlot) {
        return this.mIFStackHandler.getStackInSlot(aSlot - SLOT_ENV_CARD_START);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey(NBT_INVENTORY_TAG, Data.NBTType._object)) {
            this.mIFStackHandler.deserializeNBT(aNBT.getCompoundTag(NBT_INVENTORY_TAG));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setTag(NBT_INVENTORY_TAG, this.mIFStackHandler.serializeNBT());
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public int nextMachineMode() {
        return switch (this.machineMode) {
            case MODE_INPUT -> MODE_FARM;
            case MODE_FARM -> MODE_OUTPUT;
            default -> MODE_INPUT;
        };
    }

    @Override
    public void setMachineMode(int aIndex) {
        switch (aIndex) {
            case MODE_INPUT, MODE_FARM, MODE_OUTPUT -> this.machineMode = aIndex;
            default -> this.machineMode = MODE_INPUT;
        }
    }

    @Override
    public String getMachineModeName() {
        return switch (this.machineMode) {
            case MODE_FARM -> StatCollector.translateToLocal("cropsnh_tooltip.industrialFarm.mode.farm");
            case MODE_OUTPUT -> StatCollector.translateToLocal("cropsnh_tooltip.industrialFarm.mode.output");
            default -> StatCollector.translateToLocal("cropsnh_tooltip.industrialFarm.mode.input");
        };
    }

    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEIndustrialFarmGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT,
            GTGuiTextures.OVERLAY_BUTTON_CYCLIC,
            GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT);
    }

    @Override
    public @Nonnull CheckRecipeResult checkProcessing() {
        return switch (this.machineMode) {
            case MODE_INPUT -> this.checkProcessingInputMode();
            case MODE_FARM -> this.checkProcessingFarmMode();
            case MODE_OUTPUT -> this.checkProcessingOutputMode();
            default -> CheckRecipeResultRegistry.NO_RECIPE;
        };
    }

    /** Can't insert a seed because the existing block under didn't match the new seed. */
    @Nonnull
    public static final CheckRecipeResult CHECK_RECIPE_RESULT_BLOCK_UNDER_MISMATCH = SimpleCheckRecipeResult
        .ofFailure("cropsnh.industrialFarm.blockUnderMismatch");
    /** Can't insert a seed because the required under-block wasn't found */
    @Nonnull
    public static final CheckRecipeResult CHECK_RECIPE_RESULT_BLOCK_UNDER_NOT_FOUND = SimpleCheckRecipeResult
        .ofFailure("cropsnh.industrialFarm.blockUnderNotFound");
    /** Can't insert any more seeds because the IF is full. */
    @Nonnull
    public static final CheckRecipeResult CHECK_RECIPE_RESULT_SEEDS_FULL = SimpleCheckRecipeResult
        .ofFailure("cropsnh.industrialFarm.seedsFull");
    /** Can't generate resources because the growth requires aren't met */
    @Nonnull
    public static final CheckRecipeResult CHECK_RECIPE_RESULT_CANNOT_GROW = SimpleCheckRecipeResult
        .ofFailure("cropsnh.industrialFarm.cannotGrow");

    private CheckRecipeResult checkProcessingInputMode() {
        if (this.mSeedCapacity <= 0) return CheckRecipeResultRegistry.NONE;
        ItemStack tExisting = this.getSeedStack();
        List<ItemStack> tInputs = this.getStoredInputs();

        // the path is going to differ if the multi already contains seeds.
        CheckRecipeResult tResult = isItemStackValid(tExisting) ? tryAddSeedsToExisting(tInputs, tExisting)
            : tryAddNewSeeds(tInputs);

        if (tResult.wasSuccessful()) {
            this.mMaxProgresstime = 5;
            this.lEUt = 0;
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            return tResult;
        }
        return tResult;
    }

    /**
     * It should not be possible to insert a seed with a block under requirement.
     * 
     * @param aInputs
     * @param aExisting
     * @return
     */
    private CheckRecipeResult tryAddSeedsToExisting(List<ItemStack> aInputs, ItemStack aExisting) {
        if (aInputs.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;
        // if it's full abort early
        if (aExisting.stackSize >= this.mSeedCapacity) return CHECK_RECIPE_RESULT_SEEDS_FULL;
        // Find how many matching seeds are in the inputs.
        int tAvailableSeeds = consumeMatchingStacks(aExisting, aInputs, 0, this.mSeedCapacity, true);
        if (tAvailableSeeds == 0) return CheckRecipeResultRegistry.NO_RECIPE;
        int tInsertionMax = aExisting.stackSize + tAvailableSeeds;
        // if we have an under-block check how many to consume
        ItemStack tBlockUnder = this.getBlockUnderStack();
        tBlockUnder = isItemStackValid(tBlockUnder) ? tBlockUnder : null;
        if (tBlockUnder != null && tInsertionMax - tBlockUnder.stackSize > 0) {
            int tBlockUndersToConsume = consumeMatchingStacks(tBlockUnder, aInputs, 0, tInsertionMax, true);
            if (tBlockUndersToConsume <= 0) {
                return CHECK_RECIPE_RESULT_BLOCK_UNDER_NOT_FOUND;
            }
            tInsertionMax = Math.min(tInsertionMax, tBlockUnder.stackSize + tBlockUndersToConsume);
        }

        // consume the items, and the relevant stacks should all get updated automatically.
        consumeMatchingStacks(aExisting, aInputs, 0, tInsertionMax, false);
        if (tBlockUnder != null) {
            consumeMatchingStacks(tBlockUnder, aInputs, 0, tInsertionMax, false);
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Consumes the blocks under from the list of inputs.
     * 
     * @param aInputs           The inputs to consume from
     * @param aBlockUnderSource A stack representing what should be consumed.
     * @param aMaxConsume       The amount of items to consume from the input.
     * @return The amount of items that weren't found.
     */
    private static int consumeBlockUnder(List<ItemStack> aInputs, ItemStack aBlockUnderSource, int aMaxConsume) {
        for (ItemStack tBlockUnderCandidate : aInputs) {
            // abort early if candidate is bad
            if (!isItemStackValidAndCanItStackWithExisting(tBlockUnderCandidate, aBlockUnderSource)) continue;
            int tBlockUnderConsumption = Math.min(tBlockUnderCandidate.stackSize, aMaxConsume);
            tBlockUnderCandidate.stackSize -= tBlockUnderConsumption;
            aMaxConsume -= tBlockUnderConsumption;
        }
        return aMaxConsume;
    }

    private CheckRecipeResult tryAddNewSeeds(List<ItemStack> aInputs) {
        int tSeedIndex = 0;
        int tBlockUnderIndex = 0;
        ItemStack tNewSeedStack = null;
        ItemStack tNewBlockUnderStack = null;
        for (; tSeedIndex < aInputs.size(); tSeedIndex++) {
            // the seed must be a valid item and an analyzed seed.
            final ItemStack tSeedCandidate = aInputs.get(tSeedIndex);
            final ISeedData tSeedData = CropsNHUtils.getAnalyzedSeedData(tSeedCandidate);
            if (tSeedData == null) continue;
            // if it has a block under try to consume it
            reqs: for (IGrowthRequirement tRequirement : tSeedData.getCrop()
                .getGrowthRequirements()) {
                if (tRequirement instanceof BlockUnderRequirement tBlockUnderReq) {
                    ItemStack tExistingBlockUnderStack = this.getBlockUnderStack();
                    if (isItemStackValid(tExistingBlockUnderStack)) {
                        // check if the existing block under matches the new crop.
                        if (!tBlockUnderReq.isValidBlockUnder(tExistingBlockUnderStack)) {
                            return CHECK_RECIPE_RESULT_BLOCK_UNDER_MISMATCH;
                        }
                        tNewBlockUnderStack = tExistingBlockUnderStack;
                    } else {
                        for (tBlockUnderIndex = 0; tBlockUnderIndex < aInputs.size(); tBlockUnderIndex++) {
                            ItemStack tBlockUnderCandidate = aInputs.get(tBlockUnderIndex);
                            // abort early if it's the seed candidate or the not a valid under-block.
                            if (tBlockUnderIndex == tSeedIndex || !isItemStackValid(tBlockUnderCandidate)
                                || !tBlockUnderReq.isValidBlockUnder(tBlockUnderCandidate)) continue;
                            // else save the stack for later.
                            tNewBlockUnderStack = tBlockUnderCandidate.copy();
                            tNewBlockUnderStack.stackSize = 0;
                            break reqs;
                        }
                    }

                    // under block not found, assume incorrect input instead of checking for other seeds.
                    return CHECK_RECIPE_RESULT_BLOCK_UNDER_NOT_FOUND;
                }
            }
            // put the seed searcher back by 1 slot and save the new search targets.
            tNewSeedStack = tSeedCandidate.copy();
            tNewSeedStack.stackSize = 0;
            break;
        }
        // if nothing is in the list, nothing was found
        if (tNewSeedStack == null) return CheckRecipeResultRegistry.NO_RECIPE;

        // find the maximum amount of items we can consume.
        if (tNewBlockUnderStack == null) {
            // just consume up to the capacity, no need for any other complex checks.
            consumeMatchingStacks(tNewSeedStack, aInputs, tSeedIndex, this.mSeedCapacity, false);
        } else {
            // check how many under-blocks will be in the machine if we try to consume everything.
            int tAdditionalBlockUnderAvailable = consumeMatchingStacks(
                tNewBlockUnderStack,
                aInputs,
                tBlockUnderIndex,
                this.mSeedCapacity,
                true);
            int tBlockUnderInMachineIfAllConsumed = tNewBlockUnderStack.stackSize + tAdditionalBlockUnderAvailable;
            // Check how many seeds are available
            int tAvailableSeeds = consumeMatchingStacks(
                tNewSeedStack,
                aInputs,
                tSeedIndex,
                tBlockUnderInMachineIfAllConsumed,
                true);
            // Update the max under-block consumption based on how many seeds we're inserting in case there were some
            // blocks already in the machine.
            int tMaxAmountAfterIngest = Math.min(tAvailableSeeds, tBlockUnderInMachineIfAllConsumed);
            // consume
            consumeMatchingStacks(tNewSeedStack, aInputs, tSeedIndex, tMaxAmountAfterIngest, false);
            consumeMatchingStacks(tNewBlockUnderStack, aInputs, tBlockUnderIndex, tMaxAmountAfterIngest, false);
        }

        // update the inventory
        this.setSeedStack(tNewSeedStack);
        if (tNewBlockUnderStack != null) {
            this.setBlockUnderStack(tNewBlockUnderStack);
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Attempts to transfer all matching items form the list until the existing stack reaches a given item limit.
     * 
     * @param aExisting    The item to look for and to add the amount to.
     * @param aProvider    A list of items to consume from.
     * @param startAt      Where to start reading the tracker from (optimisation)
     * @param aMaxCapacity The maximum of number of items that can be stored.
     * @param simulate     Set to true to prevent the transfer and validate the consumption.
     * @return The amount of items that were consumed.
     */
    private static int consumeMatchingStacks(@Nonnull ItemStack aExisting, @Nonnull List<ItemStack> aProvider,
        int startAt, int aMaxCapacity, boolean simulate) {
        // if the existing stack is already reached max, abort early.
        if (aExisting.stackSize >= aMaxCapacity) return 0;
        // else search for the matching stacks
        int tNewStackSize = aExisting.stackSize;
        for (int i = startAt; i < aProvider.size() && tNewStackSize < aMaxCapacity; i++) {
            ItemStack tStack = aProvider.get(i);
            // check if item is valid
            if (!isItemStackValidAndCanItStackWithExisting(tStack, aExisting)) continue;
            int toConsume = Math.min(aMaxCapacity - tNewStackSize, tStack.stackSize);
            tNewStackSize += toConsume;
            if (!simulate) {
                // IIRC setting the stack to null is bad, if it's zero the multi will deal with it correctly.
                tStack.stackSize -= toConsume;
            }
        }
        int consumed = tNewStackSize - aExisting.stackSize;
        if (!simulate) {
            aExisting.stackSize = tNewStackSize;
        }
        return consumed;
    }

    /**
     * Checks if an item is valid, has a stack size greater than 0, and if it can stack with another existing stack
     * 
     * @param aItemStack The item stack to validate
     * @param aExisting  The existing item stack
     * @return True if all checks pass
     */
    private static boolean isItemStackValidAndCanItStackWithExisting(ItemStack aItemStack,
        @Nonnull ItemStack aExisting) {
        return isItemStackValid(aItemStack) && GTUtility.areStacksEqual(aItemStack, aExisting, false);
    }

    /**
     * Checks if an item is valid, has a stack size greater than 0
     * 
     * @param aItemStack The item stack to validate
     * @return True if all checks pass
     */
    private static boolean isItemStackValid(ItemStack aItemStack) {
        return GTUtility.isStackValid(aItemStack) && aItemStack.stackSize > 0;
    }

    /**
     * @implNote The output mode should never void anything,
     *           this is mainly due to the fact that you can't
     *           extract seeds with under-blocks without output mode
     *           due to limitations in MUI2.
     */
    private CheckRecipeResult checkProcessingOutputMode() {
        ItemStack seedStack = this.getSeedStack();
        ItemStack blockUnderStack = this.getBlockUnderStack();
        List<ItemStack> simulated = new ArrayList<>(2);
        // add seed if present
        if (CropsNHUtils.isStackValid(seedStack)) {
            simulated.add(CropsNHUtils.copyStackWithSize(seedStack, 1));
        } else {
            seedStack = null;
        }
        // add block under if present
        if (CropsNHUtils.isStackValid(blockUnderStack)) {
            simulated.add(CropsNHUtils.copyStackWithSize(blockUnderStack, 1));
        } else {
            blockUnderStack = null;
        }
        // check if anything remains
        if (simulated.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        // calc max parallel based on min stack size.
        int maxParallels = (seedStack != null && blockUnderStack != null)
            ? Math.min(seedStack.stackSize, blockUnderStack.stackSize)
            : (seedStack != null ? seedStack.stackSize : blockUnderStack.stackSize);
        // do the output voiding checks
        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(getOutputBusses(), true);
        maxParallels = ejectionHelper.ejectItems(simulated, maxParallels);
        if (maxParallels <= 0) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }
        // consume from machine
        if (seedStack != null) {
            seedStack.stackSize -= maxParallels;
            this.setSeedStack(seedStack.stackSize <= 0 ? null : seedStack);
        }
        if (blockUnderStack != null) {
            blockUnderStack.stackSize -= maxParallels;
            this.setBlockUnderStack(blockUnderStack.stackSize <= 0 ? null : blockUnderStack);
        }
        // eject seeds and blocks
        ejectionHelper.commit();

        // notify success
        this.mMaxProgresstime = 5;
        this.lEUt = 0;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private CheckRecipeResult checkProcessingFarmMode() {
        if (this.getSeedStack() == null) return CheckRecipeResultRegistry.NO_RECIPE;
        this.lEUt = -this.mExpectedEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = 100;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

}
