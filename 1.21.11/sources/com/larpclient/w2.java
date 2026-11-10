package com.larpclient;

public final class w2 {
   public static final double r = 16.0;
   private boolean l;
   private boolean Q;
   private boolean N;
   private boolean L;
   private double E = Double.NaN;
   private double w = 4.0;
   private double q = 1.0;
   private long J;

   public void V(boolean var1) {
      this.l = var1;
      this.Q = var1;
      this.N = this.L = (boolean)0;
      this.E = Double.NaN;
      this.q = 1.0;
      this.J = 0L;
   }

   public void R(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.V(true);
      } else {
         double var7 = C(var4);
         if (this.E != var7) {
            this.E = var7;
            this.w = var7;
         }

         if (!var2) {
            this.l = (boolean)0;
         }

         if (this.l) {
            this.Q = var2;
            this.L = (boolean)0;
         } else {
            int var9 = var2 && !this.Q ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.N = (boolean)(!this.N ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.N;
            if (!var10 && this.L && !var6) {
               this.w = this.E;
            }

            this.L = var10;
            this.Q = var2;
         }
      }
   }

   public boolean H(double var1) {
      if (this.L && Double.isFinite(var1) && var1 != 0.0) {
         this.w = C(this.w * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double Q(long var1, double var3, boolean var5) {
      double var6 = this.L ? 1.0 / this.w : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.q = var6;
      } else if (this.J != 0L && var1 > this.J) {
         double var8 = Math.min((var1 - this.J) * 1.0E-9, 0.05);
         this.q = this.q + (var6 - this.q) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.q) < 1.0E-4) {
            this.q = var6;
         }
      }

      this.J = var1;
      return this.q;
   }

   public boolean X() {
      return this.L;
   }

   public double F() {
      return this.q;
   }

   public double O() {
      return this.w;
   }

   public static double C(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float q(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
