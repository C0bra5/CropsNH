package com.gtnewhorizon.cropsnh.blocks;

import gregtech.api.enums.VoltageIndex;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.reference.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.ItemCasings;
import gregtech.common.blocks.MaterialCasings;

import javax.annotation.Nullable;

public class CropsNHBlocksCasing1 extends BlockCasingsAbstract {

    private static final byte START_INDEX = 64;

    public CropsNHBlocksCasing1() {
        super(ItemCasings.class, Reference.MOD_ID.toLowerCase() + ".blockscasing1", MaterialCasings.INSTANCE, 16);
        register(0, CropsNHItemList.BrickedAgriculturalCasing, "Bricked Agricultural Casing");
        register(1, CropsNHItemList.SeedBed_MV, "Basic Seed Bed");
        register(2, CropsNHItemList.SeedBed_HV, "Advanced Seed Bed");
        register(3, CropsNHItemList.SeedBed_EV, "Advanced Seed Bed II");
        register(4, CropsNHItemList.SeedBed_IV, "Advanced Seed Bed III");
        register(5, CropsNHItemList.SeedBed_LuV, "Elite Seed Bed");
        register(6, CropsNHItemList.SeedBed_ZpM, "Elite Seed Bed II");
        register(7, CropsNHItemList.SeedBed_UV, "Ultimate Seed Bed");
        register(8, CropsNHItemList.SeedBed_UHV, "Epic Seed Bed");
        register(9, CropsNHItemList.SeedBed_UEV, "Epic Seed Bed II");
        register(10, CropsNHItemList.SeedBed_UIV, "Epic Seed Bed III");
        register(11, CropsNHItemList.SeedBed_UMV, "Epic Seed Bed IV");
        register(12, CropsNHItemList.SeedBed_UXV, "Epic Seed Bed V");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        // default handler + basic bricks
        if (aSide == 0 || aMeta < 1 || 12 < aMeta) {
            return Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS.getIcon();
        }
        // top of Seed Beds is always farmland
        if (aSide == 1) {
            return Blocks.farmland.getIcon(aSide, 7);
        }
        // else use the tired casing for now
        if (aMeta <= 8) {
            return GregTechAPI.sBlockCasings1.getIcon(aSide, aMeta + 1);
        }
        return GregTechAPI.sBlockCasingsNH.getIcon(aSide, aMeta + 1);
    }

    @Nullable
    public Integer getSeedBedTier(int aMeta) {
        if (aMeta < 1 || 12 < aMeta) return null;
        return VoltageIndex.LV + aMeta;
    }
}
