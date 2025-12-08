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

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.cropsnh.blocks.BlockAdvancedHarvestingUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockEnvironmentalEnhancementUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockFertilizerUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockOverclockedGrowthAccelerationUnit;
import com.gtnewhorizon.cropsnh.blocks.BlockSeedBed;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
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
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.GTStructureChannels;
import kubatech.api.eig.EIGDropTable;

public class MTEIndustrialFarm extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFarm>
    implements ISurvivalConstructable {

    /** The duration of the production cycle in seconds. */
    public static final int CYCLE_DURATION = 5 * SECONDS;

    /** How many times the output and water/fertilizer consumption of the multi should be doubled. */
    private int mExpectedOCs = 0;
    /** How much power each recipe is expected to use */
    private long mExpectedEUt = 0;
    /** The tier of glass used to build the multi, used to limit the power hatch tiers. */
    private int mGlassTier = -1;
    /** The tier of the upgrades applied to the multi. */
    private int mUpgradeTier = -1;
    /** The number of environmental enhancement units installed on the multi. */
    private int mEnvironmentalEnhancementUnitCount = 0;
    /** The number of growth acceleration units installed on the multi. */
    private int mGrowthAccelerationUnitCount = 0;
    /** The number of fertilizer units installed on the multi. */
    private int mFertilizerUnitCount = 0;
    /** The number of advanced harvesting units installed on the multi. */
    private int mAdvancedHarvestingUnitCount = 0;
    /** The number of overclocked growth acceleration units installed on the multi. */
    private int mOverclockedGrowthAccelerationUnitCount = 0;
    /** The number of seeds and under-blocks that can be stored in the controller. */
    private int mSeedCapacity = 0;
    /** The stack of seeds stored in the multi */
    private ItemStack mSeedStack = null;
    /** The stack of under-blocks stored in the multi */
    private ItemStack mBlockUnderStack = null;
    /** The tracker for the drop progress */
    private EIGDropTable mOutputTracker = new EIGDropTable();

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
    }

    public MTEIndustrialFarm(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFarm(this.mName);
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

    @Override
    public @Nonnull CheckRecipeResult checkProcessing() {
        this.lEUt = -this.mExpectedEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = 100;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

}
