package com.larpclient.module;

import net.minecraft.util.Mth;

public final class a {
   private float t;
   private float q;
   private float m = Float.NaN;

   public d5 T(d5 var1, d5 var2) {
      return this.H(var1, var2, dj.t());
   }

   public d5 H(d5 var1, d5 var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = dj.A(var1.M, var2.M) - var1.M;
         float var5 = var2.G - var1.G;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.m) || Math.abs(this.m - var3) > 1.0E-5F) {
               this.t = 0.0F;
               this.q = 0.0F;
               this.m = var3;
            }

            float var6 = this.Q(var4, var3);
            float var7 = this.k(var5, var3);
            float var8 = var1.G + var7;
            float var9 = Mth.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.q = 0.0F;
            }

            return new d5(var1.M + var6, var9);
         } else {
            this.P();
            return new d5(var1.M + p(var4), Mth.clamp(var1.G + p(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float Q(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.t = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.t;
         float var4 = Math.round(var3 / var2) * var2;
         this.t = var3 - var4;
         return var4;
      }
   }

   private float k(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.q = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.q;
         float var4 = Math.round(var3 / var2) * var2;
         this.q = var3 - var4;
         return var4;
      }
   }

   private static float p(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void P() {
      this.t = 0.0F;
      this.q = 0.0F;
      this.m = Float.NaN;
   }
}
