package com.gtnewhorizon.cropsnh.loaders.RecipeMapFrontends;

import com.gtnewhorizons.modularui.api.math.Alignment;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.GTNEIDefaultHandler;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CropBreederFrontend extends RecipeMapFrontend {

    public CropBreederFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
                               NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip, GTNEIDefaultHandler.FixedPositionedStack pStack) {
        currentTip = super.handleNEIItemOutputTooltip(currentTip, pStack);
        currentTip.add(
            EnumChatFormatting.RESET
                + StatCollector.translateToLocal("cropsnh_nei.cropsNHCropBreeder.tooltip.outputStats"));
        return currentTip;
    }

    @Override
    protected void drawNEIOverlayForOutput(GTNEIDefaultHandler.FixedPositionedStack stack) {
        super.drawNEIOverlayForOutput(stack);
        drawNEIOverlayText(
            "+",
            stack,
            colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
            0.5f,
            true,
            Alignment.TopRight);
    }
}
