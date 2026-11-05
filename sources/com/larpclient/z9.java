package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class z9 {
   public static final int W = 48;
   public static final int C = 800;
   private final Vec3[] S = new Vec3[47];
   private final double[] G = new double[47];
   private int a;
   private int e;
   private int D = -1;
   private Vec3 B;
   private Vec3 A;
   private Vec3 J;
   private double K = Double.NaN;

   public int z() {
      return this.a;
   }

   public void L() {
      Arrays.fill(this.S, null);
      this.a = this.e = 0;
      this.B = this.A = this.J = null;
      this.D = -1;
      this.K = Double.NaN;
   }

   public void D(Vec3 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.K) && (var3 < this.K || this.B != null && this.B.distanceToSqr(var1) > 9.0)) {
            this.L();
         }

         this.K = var3;
         this.B = var1;

         for (int var7 = 0; var7 < this.S.length; var7++) {
            if (this.S[var7] != null && var3 - this.G[var7] >= var5) {
               this.S[var7] = null;
               this.a--;
            }
         }

         this.J = var2 ? var1 : null;
         if (!var2) {
            this.A = null;
         } else if (this.A != null && this.A.distanceToSqr(var1) < 0.2025 && this.S[this.D] != null) {
            this.G[this.D] = var3;
         } else {
            if (this.S[this.e] == null) {
               this.a++;
            }

            this.S[this.e] = var1;
            this.G[this.e] = var3;
            this.D = this.e;
            this.e = (this.e + 1) % this.S.length;
            this.A = var1;
         }
      } else {
         this.L();
      }
   }

   public void p(ByteBuffer var1, Vec3 var2, Vec3 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.S.length; var15++) {
         Vec3 var16 = var15 < 0 ? (this.J == null ? null : var3) : this.S[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.G[var15]) / Math.max(0.1, var6), 0.0, 1.0);
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
