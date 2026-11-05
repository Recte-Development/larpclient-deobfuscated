package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class lu {
   public static final int s = 48;
   public static final int M = 800;
   private final Vec3[] S = new Vec3[47];
   private final double[] t = new double[47];
   private int Q;
   private int L;
   private int G = -1;
   private Vec3 r;
   private Vec3 n;
   private Vec3 w;
   private double k = Double.NaN;

   public int x() {
      return this.Q;
   }

   public void C() {
      Arrays.fill(this.S, null);
      this.Q = this.L = 0;
      this.r = this.n = this.w = null;
      this.G = -1;
      this.k = Double.NaN;
   }

   public void C(Vec3 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.k) && (var3 < this.k || this.r != null && this.r.distanceToSqr(var1) > 9.0)) {
            this.C();
         }

         this.k = var3;
         this.r = var1;

         for (int var7 = 0; var7 < this.S.length; var7++) {
            if (this.S[var7] != null && var3 - this.t[var7] >= var5) {
               this.S[var7] = null;
               this.Q--;
            }
         }

         this.w = var2 ? var1 : null;
         if (!var2) {
            this.n = null;
         } else if (this.n != null && this.n.distanceToSqr(var1) < 0.2025 && this.S[this.G] != null) {
            this.t[this.G] = var3;
         } else {
            if (this.S[this.L] == null) {
               this.Q++;
            }

            this.S[this.L] = var1;
            this.t[this.L] = var3;
            this.G = this.L;
            this.L = (this.L + 1) % this.S.length;
            this.n = var1;
         }
      } else {
         this.C();
      }
   }

   public void P(ByteBuffer var1, Vec3 var2, Vec3 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.S.length; var15++) {
         Vec3 var16 = var15 < 0 ? (this.w == null ? null : var3) : this.S[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.t[var15]) / Math.max(0.1, var6), 0.0, 1.0);
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
