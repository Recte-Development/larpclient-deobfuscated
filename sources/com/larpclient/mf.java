package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class mf {
   public static final double A = -0.03125;
   public static final int X = 80;
   public static final int K = 40;
   public static final int i = 2;
   public static final double I = 0.04;
   public static final double G = 0.02;
   private Vec3 u = Vec3.ZERO;
   private int W;
   private int n;
   private int c;
   private double x;
   private boolean g;

   public void H(double var1, boolean var3) {
      this.W = !var3 && !(var1 < -0.03125) ? this.W + 1 : 0;
   }

   public Vec3 G(Vec3 var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      Vec3 var11 = new Vec3(var1.x * var2, 0.0, var1.z * var2);
      if (var4 <= 0) {
         this.u = var11;
      } else {
         Vec3 var12 = var11.subtract(this.u);
         double var13 = Math.max(var2, this.u.length()) / var4;
         double var15 = var12.length();
         this.u = var15 > var13 ? this.u.add(var12.scale(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? var5 * var6 : -var8;
      if (var5 != 0) {
         this.x = 0.0;
      }

      if (!var10) {
         this.n = 0;
         this.x = 0.0;
      } else if (this.n == 0 && this.W >= 40) {
         this.n = 2;
         this.c++;
      }

      this.g = (boolean)(this.n > 0 ? 1 : 0);
      if (this.g) {
         this.n--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.x += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.x > 0.0) {
         double var14 = Math.min(this.x, 0.02);
         this.x -= var14;
         var17 += var14;
      }

      return new Vec3(this.u.x, var17, this.u.z);
   }

   public void o() {
      this.u = Vec3.ZERO;
   }

   public Vec3 P() {
      return this.u;
   }

   public int C() {
      return this.W;
   }

   public boolean V() {
      return this.g;
   }

   public int H() {
      return this.c;
   }
}
