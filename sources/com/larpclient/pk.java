package com.larpclient;

public final class pk {
   public static final double A = 16.0;
   private boolean s;
   private boolean v;
   private boolean F;
   private boolean B;
   private double V = Double.NaN;
   private double U = 4.0;
   private double y = 1.0;
   private long b;

   public void x(boolean var1) {
      this.s = var1;
      this.v = var1;
      this.F = this.B = (boolean)0;
      this.V = Double.NaN;
      this.y = 1.0;
      this.b = 0L;
   }

   public void W(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.x(true);
      } else {
         double var7 = C(var4);
         if (this.V != var7) {
            this.V = var7;
            this.U = var7;
         }

         if (!var2) {
            this.s = (boolean)0;
         }

         if (this.s) {
            this.v = var2;
            this.B = (boolean)0;
         } else {
            int var9 = var2 && !this.v ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.F = (boolean)(!this.F ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.F;
            if (!var10 && this.B && !var6) {
               this.U = this.V;
            }

            this.B = var10;
            this.v = var2;
         }
      }
   }

   public boolean c(double var1) {
      if (this.B && Double.isFinite(var1) && var1 != 0.0) {
         this.U = C(this.U * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double i(long var1, double var3, boolean var5) {
      double var6 = this.B ? 1.0 / this.U : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.y = var6;
      } else if (this.b != 0L && var1 > this.b) {
         double var8 = Math.min((var1 - this.b) * 1.0E-9, 0.05);
         this.y = this.y + (var6 - this.y) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.y) < 1.0E-4) {
            this.y = var6;
         }
      }

      this.b = var1;
      return this.y;
   }

   public boolean F() {
      return this.B;
   }

   public double D() {
      return this.y;
   }

   public double y() {
      return this.U;
   }

   public static double C(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float e(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
