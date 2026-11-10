package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class so {
   public static final int E = 48;
   public static final int W = 800;
   private final Vec3[] c = new Vec3[47];
   private final double[] C = new double[47];
   private int g;
   private int O;
   private int Q = -1;
   private Vec3 Z;
   private Vec3 e;
   private Vec3 M;
   private double s = Double.NaN;

   public int t() {
      return this.g;
   }

   public void f() {
      Arrays.fill(this.c, null);
      this.g = this.O = 0;
      this.Z = this.e = this.M = null;
      this.Q = -1;
      this.s = Double.NaN;
   }

   public void d(Vec3 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.s) && (var3 < this.s || this.Z != null && this.Z.distanceToSqr(var1) > 9.0)) {
            this.f();
         }

         this.s = var3;
         this.Z = var1;

         for (int var7 = 0; var7 < this.c.length; var7++) {
            if (this.c[var7] != null && var3 - this.C[var7] >= var5) {
               this.c[var7] = null;
               this.g--;
            }
         }

         this.M = var2 ? var1 : null;
         if (!var2) {
            this.e = null;
         } else if (this.e != null && this.e.distanceToSqr(var1) < 0.2025 && this.c[this.Q] != null) {
            this.C[this.Q] = var3;
         } else {
            if (this.c[this.O] == null) {
               this.g++;
            }

            this.c[this.O] = var1;
            this.C[this.O] = var3;
            this.Q = this.O;
            this.O = (this.O + 1) % this.c.length;
            this.e = var1;
         }
      } else {
         this.f();
      }
   }

   public void t(ByteBuffer var1, Vec3 var2, Vec3 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.c.length; var15++) {
         Vec3 var16 = var15 < 0 ? (this.M == null ? null : var3) : this.c[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.C[var15]) / Math.max(0.1, var6), 0.0, 1.0);
         var17 = var17 >= 0.35F ? 1.0F : var17 / 0.35F;
         if (var8 && var16 != null && !(var17 <= 0.0F) && !(var16.distanceToSqr(var2) > 9216.0)) {
            float var18 = (float)(var16.x - var2.x);
            float var19 = (float)(var16.y - var2.y);
            float var20 = (float)(var16.z - var2.z);
            var1.putFloat(var18).putFloat(var19).putFloat(var20).putFloat(var17);
            var10 = Math.min(var10, var18);
            var12 = Math.max(var12, var18);
            var11 = Math.min(var11, var20);
            var13 = Math.max(var13, var20);
            var9++;
         }
      }

      for (int var21 = var9; var21 < 48; var21++) {
         var1.putLong(0L).putLong(0L);
      }

      int var22 = var1.position();
      var1.position(var14);
      var1.putFloat(var9).putFloat(0.0F).putFloat(0.0F).putFloat(0.0F);
      var1.putFloat(var9 == 0 ? 0.0F : var10 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var11 - 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var12 + 1.8F)
         .putFloat(var9 == 0 ? 0.0F : var13 + 1.8F);
      var1.position(var22);
   }
}
