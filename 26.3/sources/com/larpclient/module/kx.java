package com.larpclient.module;

import net.minecraft.util.Mth;

public final class kx {
   private float N;
   private float a;
   private float v = Float.NaN;

   public c X(c var1, c var2) {
      return this.j(var1, var2, w.d());
   }

   public c j(c var1, c var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = w.P(var1.V, var2.V) - var1.V;
         float var5 = var2.T - var1.T;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.v) || Math.abs(this.v - var3) > 1.0E-5F) {
               this.N = 0.0F;
               this.a = 0.0F;
               this.v = var3;
            }

            float var6 = this.a(var4, var3);
            float var7 = this.t(var5, var3);
            float var8 = var1.T + var7;
            float var9 = Mth.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.a = 0.0F;
            }

            return new c(var1.V + var6, var9);
         } else {
            this.L();
            return new c(var1.V + z(var4), Mth.clamp(var1.T + z(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float a(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.N = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.N;
         float var4 = Math.round(var3 / var2) * var2;
         this.N = var3 - var4;
         return var4;
      }
   }

   private float t(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.a = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.a;
         float var4 = Math.round(var3 / var2) * var2;
         this.a = var3 - var4;
         return var4;
      }
   }

   private static float z(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void L() {
      this.N = 0.0F;
      this.a = 0.0F;
      this.v = Float.NaN;
   }
}
