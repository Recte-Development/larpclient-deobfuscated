package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class k4 {
   public static final double j = -0.03125;
   public static final int d = 80;
   public static final int E = 40;
   public static final int p = 2;
   public static final double K = 0.04;
   public static final double B = 0.02;
   private Vec3 S = Vec3.ZERO;
   private int C;
   private int s;
   private int R;
   private double k;
   private boolean a;

   public void j(double var1, boolean var3) {
      this.C = !var3 && !(var1 < -0.03125) ? this.C + 1 : 0;
   }

   public Vec3 O(Vec3 var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      Vec3 var11 = new Vec3(var1.x * var2, 0.0, var1.z * var2);
      if (var4 <= 0) {
         this.S = var11;
      } else {
         Vec3 var12 = var11.subtract(this.S);
         double var13 = Math.max(var2, this.S.length()) / var4;
         double var15 = var12.length();
         this.S = var15 > var13 ? this.S.add(var12.scale(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? var5 * var6 : -var8;
      if (var5 != 0) {
         this.k = 0.0;
      }

      if (!var10) {
         this.s = 0;
         this.k = 0.0;
      } else if (this.s == 0 && this.C >= 40) {
         this.s = 2;
         this.R++;
      }

      this.a = (boolean)(this.s > 0 ? 1 : 0);
      if (this.a) {
         this.s--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.k += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.k > 0.0) {
         double var14 = Math.min(this.k, 0.02);
         this.k -= var14;
         var17 += var14;
      }

      return new Vec3(this.S.x, var17, this.S.z);
   }

   public void i() {
      this.S = Vec3.ZERO;
   }

   public Vec3 m() {
      return this.S;
   }

   public int G() {
      return this.C;
   }

   public boolean O() {
      return this.a;
   }

   public int s() {
      return this.R;
   }
}
