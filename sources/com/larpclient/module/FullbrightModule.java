package com.larpclient.module;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
   private final lp v = this.addSetting(new lp("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean B = (boolean)0;
   private double x = 1.0;
   private String I = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      B = (boolean)1;
      this.I = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.v.getValue();
         int var10000 = var1.equals(this.I);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.I(this.I);
            this.i(var1);
            this.I = var1;
         }

         int var9 = "NightVision".equals(this.I);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.I);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.a(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      B = (boolean)0;
      if ("Gamma".equals(this.I)) {
         this.a(this.x);
      } else if (this.isInGame()) {
         this.I(this.I);
      }

      this.I = null;
   }

   private void i(String var1) {
      if ("Gamma".equals(var1)) {
         this.x = (Double)mc.options.gamma().get();
         this.a(16.0);
      }
   }

   private void I(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.a(this.x);
         } else if ("NightVision".equals(var1)) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
         }
      }
   }

   private void a(double var1) {
      mc.options.gamma().value = var1;
   }

   public static boolean W() {
      return B;
   }
}
