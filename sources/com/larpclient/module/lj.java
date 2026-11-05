package com.larpclient.module;

import net.minecraft.util.Mth;

public final class lj {
   private float A;
   private float T;
   private float V = Float.NaN;

   public lx Q(lx var1, lx var2) {
      return this.M(var1, var2, le.e());
   }

   public lx M(lx var1, lx var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = le.l(var1.z, var2.z) - var1.z;
         float var5 = var2.I - var1.I;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.V) || Math.abs(this.V - var3) > 1.0E-5F) {
               this.A = 0.0F;
               this.T = 0.0F;
               this.V = var3;
            }

            float var6 = this.W(var4, var3);
            float var7 = this.y(var5, var3);
            float var8 = var1.I + var7;
            float var9 = Mth.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.T = 0.0F;
            }

            return new lx(var1.z + var6, var9);
         } else {
            this.y();
            return new lx(var1.z + K(var4), Mth.clamp(var1.I + K(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float W(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.A = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.A;
         float var4 = Math.round(var3 / var2) * var2;
         this.A = var3 - var4;
         return var4;
      }
   }

   private float y(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.T = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.T;
         float var4 = Math.round(var3 / var2) * var2;
         this.T = var3 - var4;
         return var4;
      }
   }

   private static float K(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void y() {
      this.A = 0.0F;
      this.T = 0.0F;
      this.V = Float.NaN;
   }
}
