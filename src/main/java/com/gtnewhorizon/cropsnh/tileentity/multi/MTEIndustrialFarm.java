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
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.cropsnh.blocks.CropsNHBlocksCasing1;
import com.gtnewhorizon.cropsnh.init.CropsNHBlocks;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEIndustrialFarm extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFarm>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_FIRST = "first";
    private static final String STRUCTURE_PIECE_LATER = "later";
    private static final String STRUCTURE_PIECE_LAST = "last";

    private int mCasing = 0;
    private int mSeedBedTier = -1;
    private int mGlassTier = -1;
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
                .buildAndChain(onElementPass(te -> te.mCasing++, ofBlock(CropsNHBlocks.blockCasings1, 0))))
        .addElement('c', onElementPass(t -> t.mCasing++, ofBlock(CropsNHBlocks.blockCasings1, 0)))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
        .addElement('U', ofChain(ofFrame(Materials.Wood)))
        .addElement(
            's',
            lazy(
                a -> ofBlocksTiered(
                    MTEIndustrialFarm::getSeedBlockTier,
                    getSeedBedBlocks(),
                    -1,
                    (te, t) -> te.mSeedBedTier = t,
                    te -> te.mSeedBedTier)))
        .build();

    @Nullable
    public static Integer getSeedBlockTier(Block block, int meta) {
        if (block instanceof CropsNHBlocksCasing1 seedBed) {
            return seedBed.getSeedBedTier(meta);
        }
        return null;
    }

    public static List<Pair<Block, Integer>> getSeedBedBlocks() {
        List<Pair<Block, Integer>> ret = new ArrayList<>();
        for (int tMeta = 1; tMeta <= 12; tMeta++) {
            ret.add(Pair.of(CropsNHBlocks.blockCasings1, tMeta));
        }
        return ret;
    }

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
        tt.addMachineType("Industrial Garden")
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
        return CheckRecipeResultRegistry.NO_RECIPE;
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
        for (int tSliceIndex = 1; tSliceIndex < tSlices; tSliceIndex++) {
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
        this.mSeedBedTier = -1;
        this.mGlassTier = -1;
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
        if (!tSuccess || this.mSeedBedTier < MIN_CASING_TIER || this.mGlassTier < MIN_CASING_TIER) return false;
        int tOriginalSeedBedTier = this.mSeedBedTier;
        int tSlices = GTUtility.clamp(this.mSeedBedTier - MIN_CASING_TIER + MIN_SLICES, MIN_SLICES, MAX_SLICES);
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
            if (!tSuccess || this.mSeedBedTier != tOriginalSeedBedTier || this.mGlassTier < MIN_CASING_TIER)
                return false;
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
        if (!tSuccess || this.mSeedBedTier != tOriginalSeedBedTier || this.mGlassTier < MIN_CASING_TIER) return false;

        if (this.mOutputBusses.size() < 1) return false;
        if (this.mInputHatches.size() < 1) return false;
        if (this.mMaintenanceHatches.size() != 1) return false;
        if (this.mEnergyHatches.size() < 1) return false;

        // check if exotic hatches are present
        if (this.mSeedBedTier < VoltageIndex.ZPM) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() != MTEHatch.ConnectionType.LASER) {
                    return false;
                }
                if (hatch.mTier > this.mGlassTier) {
                    return false;
                }
            }
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (mEnergyHatch.mTier > this.mGlassTier) {
                    return false;
                }
            }
        }
        return true;
    }
}
