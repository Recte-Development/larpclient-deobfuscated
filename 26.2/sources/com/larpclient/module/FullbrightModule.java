package com.larpclient.module;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
   private final l O = this.addSetting(new l("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean a = (boolean)0;
   private double q = 1.0;
   private String s = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      a = (boolean)1;
      this.s = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.O.getValue();
         int var10000 = var1.equals(this.s);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f(this.s);
            this.t(var1);
            this.s = var1;
         }

         int var9 = "NightVision".equals(this.s);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.s);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.M(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      a = (boolean)0;
      if ("Gamma".equals(this.s)) {
         this.M(this.q);
      } else if (this.isInGame()) {
         this.f(this.s);
      }

      this.s = null;
   }

   private void t(String var1) {
      if ("Gamma".equals(var1)) {
         this.q = (Double)mc.options.gamma().get();
         this.M(16.0);
      }
   }

   private void f(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.M(this.q);
         } else if ("NightVision".equals(var1)) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
         }
      }
   }

   private void M(double var1) {
      mc.options.gamma().value = var1;
   }

   public static boolean W() {
      return a;
   }
}
