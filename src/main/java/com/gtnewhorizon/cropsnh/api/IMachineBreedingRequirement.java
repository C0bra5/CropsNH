package com.gtnewhorizon.cropsnh.api;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface IMachineBreedingRequirement extends IBreedingRequirement {

    public boolean canBreed(ArrayList<ICropCard> parents, IGregTechTileEntity te, ItemStack[] catalysts, int[] consumptionTracker);

    public @Nullable List<ItemStack> getMachineOnlyCatalystsForNEI();
}
