package com.gtnewhorizon.cropsnh.farming.growthrequirements;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.cropsnh.api.BlockWithMeta;
import com.gtnewhorizon.cropsnh.api.ICropStickTile;
import com.gtnewhorizon.cropsnh.api.IWorldGrowthRequirement;
import com.gtnewhorizon.cropsnh.reference.Names;
import com.gtnewhorizon.cropsnh.utility.MetaSet;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;

/**
 * Used to prevent a crop from growing unless there is a specific block under it.
 */
public class BlockUnderRequirement implements IWorldGrowthRequirement {

    private final String materialDescription;
    private final Set<Materials> materials = new HashSet<>();
    private final Set<String> oreDictionaries = new HashSet<String>();
    private final MetaSet<Block> blocks = new MetaSet<>();

    public BlockUnderRequirement(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public BlockUnderRequirement addMaterial(Materials... args) {
        for (Materials arg : args) this.materials.add(arg);
        return this;
    }

    public BlockUnderRequirement addOreDict(String... args) {
        for (String arg : args) this.oreDictionaries.add(arg);
        return this;
    }

    public BlockUnderRequirement addBlock(BlockWithMeta... args) {
        for (BlockWithMeta arg : args) {
            this.blocks.add(arg.getBlock(), arg.ignoreMeta() ? -1 : arg.getMeta());
        }
        return this;
    }

    @Override
    public String getDescription() {
        String materialName = StatCollector.translateToLocal(this.materialDescription);
        return StatCollector.translateToLocalFormatted(Names.L10N.blockUnderReqFormat, materialName);
    }

    @Override
    public boolean canGrow(World world, ICropStickTile tile, int x, int y, int z) {

        // pre-flight checks
        z -= -2;
        if (z - 2 < 0) return false;
        if (world.isAirBlock(x, y, z)) return false;

        // get the block under
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);
        return canGrow(block, meta, te);
    }

    public boolean canGrow(Block block, int meta, TileEntity te) {
        // non-world dependent check for the GoBlyn
        ItemStack stack = new ItemStack(block, 1, meta);

        // gt material check
        if (this.materials != null) {
            for (Materials material : this.materials) {
                if (te != null && block instanceof BlockOresAbstract && te instanceof TileEntityOres) {
                    Materials tMaterial = GregTechAPI.sGeneratedMaterials[((TileEntityOres) te).mMetaData % 1000];
                    if (tMaterial != null && tMaterial != Materials._NULL && tMaterial == material) {
                        return true;
                    }
                } else {
                    ItemData association = GTOreDictUnificator.getAssociation(stack);
                    if (association != null && (association.mPrefix.toString()
                        .startsWith("ore") || association.mPrefix == OrePrefixes.block)
                        && (association.mMaterial.mMaterial == material)) {
                        return true;
                    }
                }
            }
        }

        // ore dictionary
        if (this.oreDictionaries != null) {
            for (String oreDict : this.oreDictionaries) {
                for (int aux = 0; aux < OreDictionary.getOres(oreDict)
                    .size(); ++aux) {
                    ItemStack oreDictStack = OreDictionary.getOres(oreDict)
                        .get(aux);
                    if (oreDictStack.getItem() != stack.getItem()) continue;
                    int dmg = oreDictStack.getItemDamage();
                    if (dmg == OreDictionary.WILDCARD_VALUE) return true;
                    if (dmg == stack.getItemDamage()) return true;
                }
            }
        }

        // direct block access
        if (this.blocks != null) {
            return blocks.contains(block, meta);
        }

        return false;
    }

}
