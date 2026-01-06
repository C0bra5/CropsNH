package com.gtnewhorizon.cropsnh.compatibility.NEI.dumpers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.gtnewhorizon.cropsnh.farming.registries.SoilRegistry;
import com.gtnewhorizon.cropsnh.reference.Reference;
import com.gtnewhorizon.cropsnh.utility.DebugHelper;

import codechicken.nei.config.DataDumper;
import ic2.api.crops.CropCard;
import ic2.core.crop.IC2Crops;

public class SoilRegistryDumper extends DataDumper {

    public SoilRegistryDumper() {
        super("tools.dump." + Reference.MOD_ID_LOWER + ".soilRegistry");
    }

    @Override
    public String renderName() {
        return translateN(this.name);
    }

    @Override
    public String modeButtonText() {
        return "";
    }

    @Override
    public String[] header() {
        return new String[0];
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return ".txt";
    }

    @Override
    public void dumpTo(File file) throws IOException {
        PrintWriter w = new PrintWriter(file);
        w.print(SoilRegistry.instance.dump());
        w.println("");
        w.println(DebugHelper.makeCSVLine("owner", "name"));
        for (CropCard cc : IC2Crops.instance.getCrops()) {
            w.println("case \"" + cc.owner() + ":" + cc.name() + "\" -> null;");
        }
        w.flush();
        w.close();
    }

    @Override
    public int modeCount() {
        return 0;
    }
}
