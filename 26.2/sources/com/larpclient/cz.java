package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class cz {
   public static final int G = 48;
   public static final int U = 800;
   private final Vec3[] b = new Vec3[47];
   private final double[] i = new double[47];
   private int J;
   private int P;
   private int x = -1;
   private Vec3 D;
   private Vec3 n;
   private Vec3 l;
   private double h = Double.NaN;

   public int A() {
      return this.J;
   }

   public void t() {
      Arrays.fill(this.b, null);
      this.J = this.P = 0;
      this.D = this.n = this.l = null;
      this.x = -1;
      this.h = Double.NaN;
   }

   public void b(Vec3 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.x + var1.y + var1.z)) {
         if (Double.isFinite(this.h) && (var3 < this.h || this.D != null && this.D.distanceToSqr(var1) > 9.0)) {
            this.t();
         }

         this.h = var3;
         this.D = var1;

         for (int var7 = 0; var7 < this.b.length; var7++) {
            if (this.b[var7] != null && var3 - this.i[var7] >= var5) {
               this.b[var7] = null;
               this.J--;
            }
         }

         this.l = var2 ? var1 : null;
         if (!var2) {
            this.n = null;
         } else if (this.n != null && this.n.distanceToSqr(var1) < 0.2025 && this.b[this.x] != null) {
            this.i[this.x] = var3;
         } else {
            if (this.b[this.P] == null) {
               this.J++;
            }

            this.b[this.P] = var1;
            this.i[this.P] = var3;
            this.x = this.P;
            this.P = (this.P + 1) % this.b.length;
            this.n = var1;
         }
      } else {
         this.t();
      }
   }

   public void L(ByteBuffer var1, Vec3 var2, Vec3 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.b.length; var15++) {
         Vec3 var16 = var15 < 0 ? (this.l == null ? null : var3) : this.b[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.i[var15]) / Math.max(0.1, var6), 0.0, 1.0);
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
