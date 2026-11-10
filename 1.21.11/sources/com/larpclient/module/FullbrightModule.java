package com.larpclient.module;

import net.minecraft.class_1293;
import net.minecraft.class_1294;

public class FullbrightModule extends Module {
   private final li d = this.addSetting(new li("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean J = (boolean)0;
   private double i = 1.0;
   private String s = null;

   public FullbrightModule() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      J = (boolean)1;
      this.s = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.d.getValue();
         int var10000 = var1.equals(this.s);
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.Q(this.s);
            this.E(var1);
            this.s = var1;
         }

         int var9 = "NightVision".equals(this.s);
         boolean var7 = false;
         byte var6 = (byte)var9;
         var9 = Integer.signum(var6);
         if (((var9 | -var9) >>> 31 ^ 1) == 0) {
            mc.field_1724.method_6092(new class_1293(class_1294.field_5925, 400, 0, false, false, false));
         } else {
            int var11 = "Gamma".equals(this.s);
            boolean var3 = false;
            byte var2 = (byte)var11;
            var11 = Integer.signum(var2);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               this.p(16.0);
            }
         }
      }
   }

   @Override
   public void onDisable() {
      J = (boolean)0;
      if ("Gamma".equals(this.s)) {
         this.p(this.i);
      } else if (this.isInGame()) {
         this.Q(this.s);
      }

      this.s = null;
   }

   private void E(String var1) {
      if ("Gamma".equals(var1)) {
         this.i = (Double)mc.field_1690.method_42473().method_41753();
         this.p(16.0);
      }
   }

   private void Q(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.p(this.i);
         } else if ("NightVision".equals(var1)) {
            mc.field_1724.method_6016(class_1294.field_5925);
         }
      }
   }

   private void p(double var1) {
      mc.field_1690.method_42473().field_37868 = var1;
   }

   public static boolean K() {
      return J;
   }
}
