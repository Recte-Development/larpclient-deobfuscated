package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class ba {
   public static final double B = -0.03125;
   public static final int Z = 80;
   public static final int E = 40;
   public static final int s = 2;
   public static final double r = 0.04;
   public static final double J = 0.02;
   private Vec3 h = Vec3.ZERO;
   private int X;
   private int M;
   private int x;
   private double W;
   private boolean R;

   public void p(double var1, boolean var3) {
      this.X = !var3 && !(var1 < -0.03125) ? this.X + 1 : 0;
   }

   public Vec3 G(Vec3 var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      Vec3 var11 = new Vec3(var1.x * var2, 0.0, var1.z * var2);
      if (var4 <= 0) {
         this.h = var11;
      } else {
         Vec3 var12 = var11.subtract(this.h);
         double var13 = Math.max(var2, this.h.length()) / var4;
         double var15 = var12.length();
         this.h = var15 > var13 ? this.h.add(var12.scale(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? var5 * var6 : -var8;
      if (var5 != 0) {
         this.W = 0.0;
      }

      if (!var10) {
         this.M = 0;
         this.W = 0.0;
      } else if (this.M == 0 && this.X >= 40) {
         this.M = 2;
         this.x++;
      }

      this.R = (boolean)(this.M > 0 ? 1 : 0);
      if (this.R) {
         this.M--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.W += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.W > 0.0) {
         double var14 = Math.min(this.W, 0.02);
         this.W -= var14;
         var17 += var14;
      }

      return new Vec3(this.h.x, var17, this.h.z);
   }

   public void J() {
      this.h = Vec3.ZERO;
   }

   public Vec3 J() {
      return this.h;
   }

   public int O() {
      return this.X;
   }

   public boolean q() {
      return this.R;
   }

   public int h() {
      return this.x;
   }
}
