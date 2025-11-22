package com.gtnewhorizon.cropsnh.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemNaNCertificate extends ItemCropsNH {

    @Override
    protected String getInternalName() {
        return "nanCertificate";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean addAdditionalInformation) {
        list.add(StatCollector.translateToLocal("cropsnh_tooltip.NaNCertificate"));
    }
}
