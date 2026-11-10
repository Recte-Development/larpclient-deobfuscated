package com.larpclient;

import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class hq {
   private class_10185 M = class_10185.field_54098;
   private int f;
   private double R = 1.0;
   public static final double P = 1.0;
   public static final double i = 40.0;

   public static class_243 T(float var0, class_10185 var1, double var2, double var4, boolean var6) {
      double var7 = x(var1.comp_3159(), var1.comp_3160());
      double var9 = x(var1.comp_3161(), var1.comp_3162());
      double var11 = x(var1.comp_3163(), var1.comp_3164());
      double var13 = Math.toRadians(var0);
      double var15 = var2 * (var6 ? 0.5 : 1.0);
      return new class_243(
         (-Math.sin(var13) * var7 + Math.cos(var13) * var9) * var15, var11 * var15 * var4, (Math.cos(var13) * var7 + Math.sin(var13) * var9) * var15
      );
   }

   public void g(class_10185 var1, boolean var2, boolean var3) {
      this.W();
      this.M = var3 ? var1 : class_10185.field_54098;
      if (var2) {
         this.M = X(this.M, new class_10185(false, false, false, false, false, true, false));
      }

      this.f = 40;
   }

   public void Q(class_10185 var1, boolean var2, boolean var3, boolean var4, class_241 var5, class_243 var6, float var7, boolean var8) {
      if (!var8) {
         if (this.f <= 0) {
            return;
         }

         this.f--;
      }

      if (var2 && var3) {
         this.M = X(this.M, var1);
      }

      if (var4) {
         this.M = X(this.M, new class_10185(false, false, false, false, false, true, false));
      }

      if (var2) {
         this.M = X(
            this.M, new class_10185(var5.field_1342 > 0.02, var5.field_1342 < -0.02, var5.field_1343 > 0.02, var5.field_1343 < -0.02, false, false, false)
         );
         if (!var8 || !this.M.comp_3159() && !this.M.comp_3160() && !this.M.comp_3161() && !this.M.comp_3162()) {
            if (!(var6.method_37268() < 1.0E-8)) {
               double var9 = Math.toRadians(var7);
               double var11 = -var6.field_1352 * Math.sin(var9) + var6.field_1350 * Math.cos(var9);
               double var13 = -var6.field_1350 * Math.sin(var9) - var6.field_1352 * Math.cos(var9);
               int var15 = Math.abs(var11) >= Math.abs(var13) ? 1 : 0;
               this.M = X(
                  this.M,
                  new class_10185(
                     var15 != 0 && var11 > 0.01, var15 != 0 && var11 < -0.01, var15 == 0 && var13 > 0.01, var15 == 0 && var13 < -0.01, false, false, false
                  )
               );
            }
         }
      }
   }

   public class_10185 i(class_10185 var1, boolean var2) {
      return var2
         ? X(this.M, new class_10185(var1.comp_3159(), var1.comp_3160(), var1.comp_3161(), var1.comp_3162(), false, var1.comp_3164(), var1.comp_3165()))
         : this.M;
   }

   public class_10185 V(class_10185 var1, boolean var2) {
      class_10185 var3 = this.i(var1, var2);
      if (var2) {
         this.M = new class_10185(
            this.M.comp_3159() && !var1.comp_3160(),
            this.M.comp_3160() && !var1.comp_3159(),
            this.M.comp_3161() && !var1.comp_3162(),
            this.M.comp_3162() && !var1.comp_3161(),
            this.M.comp_3163(),
            this.M.comp_3164(),
            this.M.comp_3165()
         );
      }

      return var3;
   }

   public static class_241 T(class_10185 var0) {
      return new class_241(x(var0.comp_3161(), var0.comp_3162()), x(var0.comp_3159(), var0.comp_3160())).method_35581();
   }

   public boolean m(double var1, double var3, boolean var5) {
      if (Double.isFinite(var1) && var1 != 0.0) {
         if (var5) {
            this.R = Math.clamp(this.R * (var1 > 0.0 ? 1.2 : 0.8333333333333334), 0.1 / var3, 50.0 / var3);
         }

         return true;
      } else {
         return false;
      }
   }

   public double U(double var1, boolean var3) {
      return var3 ? Math.clamp(var1 * this.R, 0.1, 50.0) : var1;
   }

   public static double W(double var0, double var2) {
      return Math.clamp(var0 * (var2 > 0.0 ? 1.2 : 0.8333333333333334), 1.0, 40.0);
   }

   public void Q() {
      this.R = 1.0;
   }

   public void V() {
      this.M = class_10185.field_54098;
      this.f = 0;
   }

   public void W() {
      this.V();
      this.Q();
   }

   public static hq.c B(class_243 var0, int var1, int var2, int var3, int var4) {
      int var5 = class_3532.method_15357(var0.field_1352) >> 4;
      int var6 = class_3532.method_15357(var0.field_1350) >> 4;
      int var7 = Math.max(Math.abs(var5 - var1), Math.abs(var6 - var2));
      int var8 = Math.clamp(var3, 2, 32);
      int var9 = Math.max(2, var4);
      int var10 = Math.max(var9, Math.clamp(Math.max(var8, var7 / 2 + 3), 2, 32));
      int var11 = var7 > var8 ? var1 + (var5 - var1) / 2 : var5;
      int var12 = var7 > var8 ? var2 + (var6 - var2) / 2 : var6;
      int var13 = var10 - var9;
      return new hq.c(Math.clamp(var11, var1 - var13, var1 + var13), Math.clamp(var12, var2 - var13, var2 + var13), var10);
   }

   private static float x(boolean var0, boolean var1) {
      return (var0 ? 1 : 0) - (var1 ? 1 : 0);
   }

   public static class_10185 X(class_10185 var0, class_10185 var1) {
      return new class_10185(
         var0.comp_3159() || var1.comp_3159(),
         var0.comp_3160() || var1.comp_3160(),
         var0.comp_3161() || var1.comp_3161(),
         var0.comp_3162() || var1.comp_3162(),
         var0.comp_3163() || var1.comp_3163(),
         var0.comp_3164() || var1.comp_3164(),
         var0.comp_3165() || var1.comp_3165()
      );
   }

   public record c(int x, int z, int radius) {
   }
}
