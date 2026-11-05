package com.larpclient.module;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FullbrightModule extends Module {
   private final d6 D = this.addSetting(new d6("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean d = (boolean)0;
   private double y = 1.0;
   private String L = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      d = (boolean)1;
      this.L = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.D.P();
         int var10000 = var1.equals(this.L);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.k(this.L);
            this.e(var1);
            this.L = var1;
         }

         int var9 = "NightVision".equals(this.L);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.L);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.l(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      d = (boolean)0;
      if ("Gamma".equals(this.L)) {
         this.l(this.y);
      } else if (this.isInGame()) {
         this.k(this.L);
      }

      this.L = null;
   }

   private void e(String var1) {
      if ("Gamma".equals(var1)) {
         this.y = (Double)mc.options.gamma().get();
         this.l(16.0);
      }
   }

   private void k(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.l(this.y);
         } else if ("NightVision".equals(var1)) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
         }
      }
   }

   private void l(double var1) {
      mc.options.gamma().value = var1;
   }

   public static boolean l() {
      return d;
   }
}
