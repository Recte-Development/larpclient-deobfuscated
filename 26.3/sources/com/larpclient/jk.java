package com.larpclient;

public final class jk {
   public static final double v = 16.0;
   private boolean F;
   private boolean K;
   private boolean D;
   private boolean g;
   private double a = Double.NaN;
   private double h = 4.0;
   private double f = 1.0;
   private long Y;

   public void h(boolean var1) {
      this.F = var1;
      this.K = var1;
      this.D = this.g = (boolean)0;
      this.a = Double.NaN;
      this.f = 1.0;
      this.Y = 0L;
   }

   public void z(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.h(true);
      } else {
         double var7 = E(var4);
         if (this.a != var7) {
            this.a = var7;
            this.h = var7;
         }

         if (!var2) {
            this.F = (boolean)0;
         }

         if (this.F) {
            this.K = var2;
            this.g = (boolean)0;
         } else {
            int var9 = var2 && !this.K ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.D = (boolean)(!this.D ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.D;
            if (!var10 && this.g && !var6) {
               this.h = this.a;
            }

            this.g = var10;
            this.K = var2;
         }
      }
   }

   public boolean T(double var1) {
      if (this.g && Double.isFinite(var1) && var1 != 0.0) {
         this.h = E(this.h * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double X(long var1, double var3, boolean var5) {
      double var6 = this.g ? 1.0 / this.h : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.f = var6;
      } else if (this.Y != 0L && var1 > this.Y) {
         double var8 = Math.min((var1 - this.Y) * 1.0E-9, 0.05);
         this.f = this.f + (var6 - this.f) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.f) < 1.0E-4) {
            this.f = var6;
         }
      }

      this.Y = var1;
      return this.f;
   }

   public boolean B() {
      return this.g;
   }

   public double U() {
      return this.f;
   }

   public double v() {
      return this.h;
   }

   public static double E(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float p(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
