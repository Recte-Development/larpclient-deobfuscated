package com.larpclient;

import net.minecraft.class_243;

public final class hn {
   public static final double O = -0.03125;
   public static final int x = 80;
   public static final int h = 40;
   public static final int b = 2;
   public static final double d = 0.04;
   public static final double A = 0.02;
   private class_243 N = class_243.field_1353;
   private int J;
   private int z;
   private int s;
   private double y;
   private boolean o;

   public void D(double var1, boolean var3) {
      this.J = !var3 && !(var1 < -0.03125) ? this.J + 1 : 0;
   }

   public class_243 J(class_243 var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      class_243 var11 = new class_243(var1.field_1352 * var2, 0.0, var1.field_1350 * var2);
      if (var4 <= 0) {
         this.N = var11;
      } else {
         class_243 var12 = var11.method_1020(this.N);
         double var13 = Math.max(var2, this.N.method_1033()) / var4;
         double var15 = var12.method_1033();
         this.N = var15 > var13 ? this.N.method_1019(var12.method_1021(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? var5 * var6 : -var8;
      if (var5 != 0) {
         this.y = 0.0;
      }

      if (!var10) {
         this.z = 0;
         this.y = 0.0;
      } else if (this.z == 0 && this.J >= 40) {
         this.z = 2;
         this.s++;
      }

      this.o = (boolean)(this.z > 0 ? 1 : 0);
      if (this.o) {
         this.z--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.y += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.y > 0.0) {
         double var14 = Math.min(this.y, 0.02);
         this.y -= var14;
         var17 += var14;
      }

      return new class_243(this.N.field_1352, var17, this.N.field_1350);
   }

   public void W() {
      this.N = class_243.field_1353;
   }

   public class_243 N() {
      return this.N;
   }

   public int o() {
      return this.J;
   }

   public boolean e() {
      return this.o;
   }

   public int X() {
      return this.s;
   }
}
