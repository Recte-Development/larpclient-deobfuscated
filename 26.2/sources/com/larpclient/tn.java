package com.larpclient;

public final class tn {
   public static final double a = 16.0;
   private boolean q;
   private boolean v;
   private boolean t;
   private boolean I;
   private double j = Double.NaN;
   private double H = 4.0;
   private double o = 1.0;
   private long c;

   public void s(boolean var1) {
      this.q = var1;
      this.v = var1;
      this.t = this.I = (boolean)0;
      this.j = Double.NaN;
      this.o = 1.0;
      this.c = 0L;
   }

   public void M(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.s(true);
      } else {
         double var7 = u(var4);
         if (this.j != var7) {
            this.j = var7;
            this.H = var7;
         }

         if (!var2) {
            this.q = (boolean)0;
         }

         if (this.q) {
            this.v = var2;
            this.I = (boolean)0;
         } else {
            int var9 = var2 && !this.v ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.t = (boolean)(!this.t ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.t;
            if (!var10 && this.I && !var6) {
               this.H = this.j;
            }

            this.I = var10;
            this.v = var2;
         }
      }
   }

   public boolean E(double var1) {
      if (this.I && Double.isFinite(var1) && var1 != 0.0) {
         this.H = u(this.H * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double w(long var1, double var3, boolean var5) {
      double var6 = this.I ? 1.0 / this.H : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.o = var6;
      } else if (this.c != 0L && var1 > this.c) {
         double var8 = Math.min((var1 - this.c) * 1.0E-9, 0.05);
         this.o = this.o + (var6 - this.o) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.o) < 1.0E-4) {
            this.o = var6;
         }
      }

      this.c = var1;
      return this.o;
   }

   public boolean H() {
      return this.I;
   }

   public double I() {
      return this.o;
   }

   public double n() {
      return this.H;
   }

   public static double u(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float A(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
