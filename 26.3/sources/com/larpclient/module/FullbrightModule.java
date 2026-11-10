package com.larpclient.module;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
   private final o E = this.addSetting(new o("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean G = (boolean)0;
   private double K = 1.0;
   private String M = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      G = (boolean)1;
      this.M = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.E.getValue();
         int var10000 = var1.equals(this.M);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.H(this.M);
            this.X(var1);
            this.M = var1;
         }

         int var9 = "NightVision".equals(this.M);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.M);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.g(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      G = (boolean)0;
      if ("Gamma".equals(this.M)) {
         this.g(this.K);
      } else if (this.isInGame()) {
         this.H(this.M);
      }

      this.M = null;
   }

   private void X(String var1) {
      if ("Gamma".equals(var1)) {
         this.K = (Double)mc.options.gamma().get();
         this.g(16.0);
      }
   }

   private void H(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.g(this.K);
         } else if ("NightVision".equals(var1)) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
         }
      }
   }

   private void g(double var1) {
      mc.options.gamma().value = var1;
   }

   public static boolean v() {
      return G;
   }
}
