package com.gtnewhorizon.cropsnh.renderers.player.renderhooks;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerEffectRendererEntityDragon extends PlayerEffectRendererEntity {

    protected PlayerEffectRendererEntityDragon() {
        super();
    }

    @Override
    ArrayList<String> getDisplayNames() {
        ArrayList<String> names = new ArrayList<>();
        names.add("GreatOrator");
        return names;
    }

    @Override
    protected IWrappedEntity getEntityWrapper() {
        return new EntityDragon();
    }

    @Override
    protected ModelBase getModel() {
        return new ModelDragon(0.0F);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation("cropsnh", "textures/entities/player/dragon/dragon.png");
    }

    @Override
    protected float getScale() {
        return 0.005F;
    }

    protected static class EntityDragon extends net.minecraft.entity.boss.EntityDragon
        implements IWrappedEntity<net.minecraft.entity.boss.EntityDragon> {

        public EntityDragon() {
            super(Minecraft.getMinecraft().theWorld);
            this.setHealth(this.getMaxHealth());
            this.setSize(2.0F, 1.0F);
            this.noClip = true;
            this.isImmuneToFire = true;
            this.ignoreFrustumCheck = false;
        }

        @Override
        public net.minecraft.entity.boss.EntityDragon getEntity() {
            return this;
        }

        @Override
        public void performAnimationUpdates() {
            double test = 10 * (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
            this.prevAnimTime = this.animTime;
            this.animTime = (float) (test / 360);
        }

        @Override
        public float[] getModelParameters() {
            return new float[] { 0, 0, 0, 0, 0, 1 };
        }

        @Override
        public int getFloatingVelocity() {
            return 10;
        }
    }
}
