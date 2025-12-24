package com.gtnewhorizon.cropsnh.tileentity.multi;

import com.cleanroommc.modularui.screen.ModularContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MTEIndustrialFarmModularContainer extends ModularContainer {
    @Override
    public @Nullable ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        return super.transferStackInSlot(playerIn, index);
    }
}
