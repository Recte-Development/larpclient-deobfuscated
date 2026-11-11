package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean active = (boolean)0;
   private double originalGamma = 1.0;
   private String appliedMode = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      active = (boolean)1;
      this.appliedMode = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.mode.getValue();
         int var10000 = var1.equals(this.appliedMode);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.revertMode(this.appliedMode);
            this.applyMode(var1);
            this.appliedMode = var1;
         }

         int var9 = "NightVision".equals(this.appliedMode);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            minecraft.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.appliedMode);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.setGamma(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      active = (boolean)0;
      if ("Gamma".equals(this.appliedMode)) {
         this.setGamma(this.originalGamma);
      } else if (this.isInGame()) {
         this.revertMode(this.appliedMode);
      }

      this.appliedMode = null;
   }

   private void applyMode(String var1) {
      if ("Gamma".equals(var1)) {
         this.originalGamma = (Double)minecraft.options.gamma().get();
         this.setGamma(16.0);
      }
   }

   private void revertMode(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.setGamma(this.originalGamma);
         } else if ("NightVision".equals(var1)) {
            minecraft.player.removeEffect(MobEffects.NIGHT_VISION);
         }
      }
   }

   private void setGamma(double var1) {
      minecraft.options.gamma().value = var1;
   }

   public static boolean isActive() {
      return active;
   }
}
