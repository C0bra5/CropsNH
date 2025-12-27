package com.gtnewhorizon.cropsnh.items;

import java.util.List;

import com.gtnewhorizon.cropsnh.reference.Reference;
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
        list.add(StatCollector.translateToLocal(Reference.MOD_ID_LOWER + "_tooltip.NaNCertificate"));
    }
}
