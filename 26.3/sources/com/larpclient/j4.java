package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class j4 {
   public static final double n = -0.03125;
   public static final int R = 80;
   public static final int H = 40;
   public static final int V = 2;
   public static final double v = 0.04;
   public static final double r = 0.02;
   private Vec3 a = Vec3.ZERO;
   private int E;
   private int D;
   private int X;
   private double u;
   private boolean c;

   public void z(double var1, boolean var3) {
      this.E = !var3 && !(var1 < -0.03125) ? this.E + 1 : 0;
   }

   public Vec3 t(Vec3 var1, double var2, int var4, int var5, double var6, double var8, boolean var10) {
      Vec3 var11 = new Vec3(var1.x * var2, 0.0, var1.z * var2);
      if (var4 <= 0) {
         this.a = var11;
      } else {
         Vec3 var12 = var11.subtract(this.a);
         double var13 = Math.max(var2, this.a.length()) / var4;
         double var15 = var12.length();
         this.a = var15 > var13 ? this.a.add(var12.scale(var13 / var15)) : var11;
      }

      double var17 = var5 != 0 ? var5 * var6 : -var8;
      if (var5 != 0) {
         this.u = 0.0;
      }

      if (!var10) {
         this.D = 0;
         this.u = 0.0;
      } else if (this.D == 0 && this.E >= 40) {
         this.D = 2;
         this.X++;
      }

      this.c = (boolean)(this.D > 0 ? 1 : 0);
      if (this.c) {
         this.D--;
         if (var17 > -0.04) {
            if (var5 == 0) {
               this.u += var17 + 0.04;
            }

            var17 = -0.04;
         }
      } else if (var5 == 0 && this.u > 0.0) {
         double var14 = Math.min(this.u, 0.02);
         this.u -= var14;
         var17 += var14;
      }

      return new Vec3(this.a.x, var17, this.a.z);
   }

   public void c() {
      this.a = Vec3.ZERO;
   }

   public Vec3 b() {
      return this.a;
   }

   public int R() {
      return this.E;
   }

   public boolean U() {
      return this.c;
   }

   public int q() {
      return this.X;
   }
}
