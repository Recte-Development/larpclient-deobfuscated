package com.larpclient.module;

import net.minecraft.class_3532;

public final class ld {
   private float k;
   private float m;
   private float K = Float.NaN;

   public l4 E(l4 var1, l4 var2) {
      return this.C(var1, var2, a_.i());
   }

   public l4 C(l4 var1, l4 var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = a_.H(var1.j, var2.j) - var1.j;
         float var5 = var2.l - var1.l;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.K) || Math.abs(this.K - var3) > 1.0E-5F) {
               this.k = 0.0F;
               this.m = 0.0F;
               this.K = var3;
            }

            float var6 = this.F(var4, var3);
            float var7 = this.a(var5, var3);
            float var8 = var1.l + var7;
            float var9 = class_3532.method_15363(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.m = 0.0F;
            }

            return new l4(var1.j + var6, var9);
         } else {
            this.Y();
            return new l4(var1.j + K(var4), class_3532.method_15363(var1.l + K(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float F(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.k = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.k;
         float var4 = Math.round(var3 / var2) * var2;
         this.k = var3 - var4;
         return var4;
      }
   }

   private float a(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.m = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.m;
         float var4 = Math.round(var3 / var2) * var2;
         this.m = var3 - var4;
         return var4;
      }
   }

   private static float K(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void Y() {
      this.k = 0.0F;
      this.m = 0.0F;
      this.K = Float.NaN;
   }
}
