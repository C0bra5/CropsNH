package com.gtnewhorizon.cropsnh.loaders;

import com.gtnewhorizon.cropsnh.api.CropsNHItemList;
import com.gtnewhorizon.cropsnh.tileentity.MTECropBreeder;
import com.gtnewhorizon.cropsnh.tileentity.MTECropManager;
import com.gtnewhorizon.cropsnh.tileentity.MTESeedGenerator;

import gregtech.api.enums.VoltageIndex;

public class MTELoader {

    public static void init() {
        CropsNHItemList.CropManager_LV
            .set(new MTECropManager(28001, VoltageIndex.LV, "Basic Crop Manager").getStackForm(1L));
        CropsNHItemList.CropManager_MV
            .set(new MTECropManager(28002, VoltageIndex.MV, "Advanced Crop Manager").getStackForm(1L));
        CropsNHItemList.CropManager_HV
            .set(new MTECropManager(28003, VoltageIndex.HV, "Advanced Crop Manager II").getStackForm(1L));
        CropsNHItemList.CropManager_EV
            .set(new MTECropManager(28004, VoltageIndex.EV, "Advanced Crop Manager III").getStackForm(1L));
        CropsNHItemList.CropManager_IV
            .set(new MTECropManager(28005, VoltageIndex.IV, "Advanced Crop Manager IV").getStackForm(1L));
        CropsNHItemList.CropManager_LuV
            .set(new MTECropManager(28006, VoltageIndex.LuV, "Elite Crop Manager").getStackForm(1L));
        CropsNHItemList.CropManager_ZPM
            .set(new MTECropManager(28007, VoltageIndex.ZPM, "Elite Crop Manager II").getStackForm(1L));
        CropsNHItemList.CropManager_UV
            .set(new MTECropManager(28008, VoltageIndex.UV, "Ultimate Crop Manager").getStackForm(1L));
        CropsNHItemList.CropManager_UHV
            .set(new MTECropManager(28009, VoltageIndex.UHV, "Epic Crop Manager").getStackForm(1L));
        CropsNHItemList.CropManager_UEV
            .set(new MTECropManager(28010, VoltageIndex.UEV, "Epic Crop Manager II").getStackForm(1L));
        CropsNHItemList.CropManager_UIV
            .set(new MTECropManager(28011, VoltageIndex.UIV, "Epic Crop Manager III").getStackForm(1L));
        CropsNHItemList.CropManager_UMV
            .set(new MTECropManager(28012, VoltageIndex.UMV, "Epic Crop Manager IV").getStackForm(1L));

        CropsNHItemList.SeedGenerator_LV
            .set(new MTESeedGenerator(28013, VoltageIndex.LV, "Basic Seed Generator").getStackForm(1L));
        CropsNHItemList.SeedGenerator_MV
            .set(new MTESeedGenerator(28014, VoltageIndex.MV, "Advanced Seed Generator").getStackForm(1L));
        CropsNHItemList.SeedGenerator_HV
            .set(new MTESeedGenerator(28015, VoltageIndex.HV, "Advanced Seed Generator II").getStackForm(1L));
        CropsNHItemList.SeedGenerator_EV
            .set(new MTESeedGenerator(28016, VoltageIndex.EV, "Advanced Seed Generator III").getStackForm(1L));
        CropsNHItemList.SeedGenerator_IV
            .set(new MTESeedGenerator(28017, VoltageIndex.IV, "Advanced Seed Generator IV").getStackForm(1L));
        CropsNHItemList.SeedGenerator_LuV
            .set(new MTESeedGenerator(28018, VoltageIndex.LuV, "Elite Seed Generator").getStackForm(1L));
        CropsNHItemList.SeedGenerator_ZPM
            .set(new MTESeedGenerator(28019, VoltageIndex.ZPM, "Elite Seed Generator II").getStackForm(1L));
        CropsNHItemList.SeedGenerator_UV
            .set(new MTESeedGenerator(28020, VoltageIndex.UV, "Ultimate Seed Replicator").getStackForm(1L));
        CropsNHItemList.SeedGenerator_UHV
            .set(new MTESeedGenerator(28021, VoltageIndex.UHV, "Epic Seed Replicator").getStackForm(1L));
        CropsNHItemList.SeedGenerator_UEV
            .set(new MTESeedGenerator(28022, VoltageIndex.UEV, "Epic Seed Replicator II").getStackForm(1L));
        CropsNHItemList.SeedGenerator_UIV
            .set(new MTESeedGenerator(28023, VoltageIndex.UIV, "Epic Seed Replicator III").getStackForm(1L));
        CropsNHItemList.SeedGenerator_UMV
            .set(new MTESeedGenerator(28024, VoltageIndex.UMV, "Epic Seed Replicator IV").getStackForm(1L));

        CropsNHItemList.CropBreeder_LV
            .set(new MTECropBreeder(28025, VoltageIndex.LV, "Basic Crop Breeder").getStackForm(1L));
        CropsNHItemList.CropBreeder_MV
            .set(new MTECropBreeder(28026, VoltageIndex.MV, "Advanced Crop Breeder").getStackForm(1L));
        CropsNHItemList.CropBreeder_HV
            .set(new MTECropBreeder(28027, VoltageIndex.HV, "Advanced Crop Breeder II").getStackForm(1L));
        CropsNHItemList.CropBreeder_EV
            .set(new MTECropBreeder(28028, VoltageIndex.EV, "Advanced Crop Breeder III").getStackForm(1L));
        CropsNHItemList.CropBreeder_IV
            .set(new MTECropBreeder(28029, VoltageIndex.IV, "Advanced Crop Breeder IV").getStackForm(1L));
        CropsNHItemList.CropBreeder_LuV
            .set(new MTECropBreeder(28030, VoltageIndex.LuV, "Elite Crop Breeder").getStackForm(1L));
        CropsNHItemList.CropBreeder_ZPM
            .set(new MTECropBreeder(28031, VoltageIndex.ZPM, "Elite Crop Breeder II").getStackForm(1L));
        CropsNHItemList.CropBreeder_UV
            .set(new MTECropBreeder(28032, VoltageIndex.UV, "Ultimate Crop Breeder").getStackForm(1L));
        CropsNHItemList.CropBreeder_UHV
            .set(new MTECropBreeder(28033, VoltageIndex.UHV, "Epic Crop Breeder").getStackForm(1L));
        CropsNHItemList.CropBreeder_UEV
            .set(new MTECropBreeder(28034, VoltageIndex.UEV, "Epic Crop Breeder II").getStackForm(1L));
        CropsNHItemList.CropBreeder_UIV
            .set(new MTECropBreeder(28035, VoltageIndex.UIV, "Epic Crop Breeder III").getStackForm(1L));
        CropsNHItemList.CropBreeder_UMV
            .set(new MTECropBreeder(28036, VoltageIndex.UMV, "Epic Crop Breeder IV").getStackForm(1L));
    }

}
