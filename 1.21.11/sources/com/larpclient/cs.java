package com.larpclient;

import java.nio.ByteBuffer;
import java.util.Arrays;
import net.minecraft.class_243;

public final class cs {
   public static final int a = 48;
   public static final int C = 800;
   private final class_243[] l = new class_243[47];
   private final double[] A = new double[47];
   private int J;
   private int V;
   private int M = -1;
   private class_243 b;
   private class_243 U;
   private class_243 E;
   private double t = Double.NaN;

   public int y() {
      return this.J;
   }

   public void V() {
      Arrays.fill(this.l, null);
      this.J = this.V = 0;
      this.b = this.U = this.E = null;
      this.M = -1;
      this.t = Double.NaN;
   }

   public void F(class_243 var1, boolean var2, double var3, double var5) {
      if (Double.isFinite(var3) && Double.isFinite(var1.field_1352 + var1.field_1351 + var1.field_1350)) {
         if (Double.isFinite(this.t) && (var3 < this.t || this.b != null && this.b.method_1025(var1) > 9.0)) {
            this.V();
         }

         this.t = var3;
         this.b = var1;

         for (int var7 = 0; var7 < this.l.length; var7++) {
            if (this.l[var7] != null && var3 - this.A[var7] >= var5) {
               this.l[var7] = null;
               this.J--;
            }
         }

         this.E = var2 ? var1 : null;
         if (!var2) {
            this.U = null;
         } else if (this.U != null && this.U.method_1025(var1) < 0.2025 && this.l[this.M] != null) {
            this.A[this.M] = var3;
         } else {
            if (this.l[this.V] == null) {
               this.J++;
            }

            this.l[this.V] = var1;
            this.A[this.V] = var3;
            this.M = this.V;
            this.V = (this.V + 1) % this.l.length;
            this.U = var1;
         }
      } else {
         this.V();
      }
   }

   public void G(ByteBuffer var1, class_243 var2, class_243 var3, double var4, double var6, boolean var8) {
      int var9 = 0;
      float var10 = Float.POSITIVE_INFINITY;
      float var11 = var10;
      float var12 = Float.NEGATIVE_INFINITY;
      float var13 = var12;
      int var14 = var1.position();
      var1.position(var14 + 32);

      for (int var15 = -1; var15 < this.l.length; var15++) {
         class_243 var16 = var15 < 0 ? (this.E == null ? null : var3) : this.l[var15];
         float var17 = var15 < 0 ? 1.0F : (float)Math.clamp(1.0 - (var4 - this.A[var15]) / Math.max(0.1, var6), 0.0, 1.0);
         var17 = var17 >= 0.35F ? 1.0F : var17 / 0.35F;
         if (var8 && var16 != null && !(var17 <= 0.0F) && !(var16.method_1025(var2) > 9216.0)) {
            float var18 = (float)(var16.field_1352 - var2.field_1352);
            float var19 = (float)(var16.field_1351 - var2.field_1351);
            float var20 = (float)(var16.field_1350 - var2.field_1350);
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
