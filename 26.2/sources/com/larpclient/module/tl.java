package com.larpclient.module;

import net.minecraft.util.Mth;

public final class tl {
   private float j;
   private float Y;
   private float e = Float.NaN;

   public u M(u var1, u var2) {
      return this.x(var1, var2, tv.g());
   }

   public u x(u var1, u var2, float var3) {
      if (var1 != null && var2 != null) {
         float var4 = tv.X(var1.R, var2.R) - var1.R;
         float var5 = var2.x - var1.x;
         if (Float.isFinite(var3) && !(var3 <= 1.0E-5F)) {
            if (!Float.isFinite(this.e) || Math.abs(this.e - var3) > 1.0E-5F) {
               this.j = 0.0F;
               this.Y = 0.0F;
               this.e = var3;
            }

            float var6 = this.I(var4, var3);
            float var7 = this.B(var5, var3);
            float var8 = var1.x + var7;
            float var9 = Mth.clamp(var8, -90.0F, 90.0F);
            if (var9 != var8) {
               this.Y = 0.0F;
            }

            return new u(var1.R + var6, var9);
         } else {
            this.g();
            return new u(var1.R + G(var4), Mth.clamp(var1.x + G(var5), -90.0F, 90.0F));
         }
      } else {
         return var2 != null ? var2 : var1;
      }
   }

   private float I(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.j = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.j;
         float var4 = Math.round(var3 / var2) * var2;
         this.j = var3 - var4;
         return var4;
      }
   }

   private float B(float var1, float var2) {
      if (!Float.isFinite(var1)) {
         this.Y = 0.0F;
         return 0.0F;
      } else {
         float var3 = var1 + this.Y;
         float var4 = Math.round(var3 / var2) * var2;
         this.Y = var3 - var4;
         return var4;
      }
   }

   private static float G(float var0) {
      return Float.isFinite(var0) ? var0 : 0.0F;
   }

   public void g() {
      this.j = 0.0F;
      this.Y = 0.0F;
      this.e = Float.NaN;
   }
}
