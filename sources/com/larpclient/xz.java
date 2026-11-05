package com.larpclient;

public final class xz {
   public static final double o = 16.0;
   private boolean q;
   private boolean D;
   private boolean w;
   private boolean a;
   private double e = Double.NaN;
   private double v = 4.0;
   private double b = 1.0;
   private long F;

   public void I(boolean var1) {
      this.q = var1;
      this.D = var1;
      this.w = this.a = (boolean)0;
      this.e = Double.NaN;
      this.b = 1.0;
      this.F = 0L;
   }

   public void E(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.I(true);
      } else {
         double var7 = K(var4);
         if (this.e != var7) {
            this.e = var7;
            this.v = var7;
         }

         if (!var2) {
            this.q = (boolean)0;
         }

         if (this.q) {
            this.D = var2;
            this.a = (boolean)0;
         } else {
            int var9 = var2 && !this.D ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.w = (boolean)(!this.w ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.w;
            if (!var10 && this.a && !var6) {
               this.v = this.e;
            }

            this.a = var10;
            this.D = var2;
         }
      }
   }

   public boolean z(double var1) {
      if (this.a && Double.isFinite(var1) && var1 != 0.0) {
         this.v = K(this.v * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double t(long var1, double var3, boolean var5) {
      double var6 = this.a ? 1.0 / this.v : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.b = var6;
      } else if (this.F != 0L && var1 > this.F) {
         double var8 = Math.min((var1 - this.F) * 1.0E-9, 0.05);
         this.b = this.b + (var6 - this.b) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.b) < 1.0E-4) {
            this.b = var6;
         }
      }

      this.F = var1;
      return this.b;
   }

   public boolean k() {
      return this.a;
   }

   public double H() {
      return this.b;
   }

   public double L() {
      return this.v;
   }

   public static double K(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float K(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
