package com.larpclient;

public final class _j {
   public static final double F = 0.05;
   public static final double w = 0.35;
   private static final double E = 720.0;

   public static double V(double var0) {
      return var0 - Math.floor((var0 + 180.0) / 360.0) * 360.0;
   }

   public static double f(double var0, double var2, boolean var4) {
      return Math.hypot(V(var0), var4 ? var2 : 0.0);
   }

   public static boolean Q(double var0, double var2) {
      return Double.isFinite(var0) && var0 <= Math.clamp(var2, 0.0, 180.0) / 2.0;
   }

   public static boolean o(double var0, int var2, double var3, int var5, boolean var6) {
      double var7 = var6 ? Math.max(2.0, var3 * 0.15) : 0.0;
      return var0 < var3 - var7 - 1.0E-7 || !var6 && Math.abs(var0 - var3) <= 1.0E-7 && var2 < var5;
   }

   public static boolean A(double var0, double var2, double var4, double var6, boolean var8) {
      return var0 * var4 < -1.0E-5 || var8 && var2 * var6 < -1.0E-5;
   }

   private _j() {
   }

   public static final class c {
      private long C;
      private boolean l;

      public double r(long var1) {
         long var3 = this.C == 0L ? 0L : var1 - this.C;
         this.l = (boolean)(this.C == 0L || var3 > 0L && var3 <= 250000000L ? 0 : 1);
         this.C = var1;
         return var3 <= 0L ? 0.0 : Math.min(var3 / 1.0E9, 0.05);
      }

      public boolean h() {
         return this.l;
      }

      public void L() {
         this.C = 0L;
         this.l = (boolean)0;
      }
   }

   public static final class o {
      private double I;
      private double o;

      public double v(double var1, double var3, double var5) {
         if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5) && !(var3 <= 0.0) && !(var5 <= 0.0)) {
            double var7 = Math.min(var3, 0.05);
            if (Math.abs(var1) <= 0.35) {
               this.h();
               return 0.0;
            }

            double var9 = Math.clamp(var1 * 8.0, -var5, var5);
            if (this.I * var1 < 0.0) {
               this.I = 0.0;
            }

            this.I = Math.clamp(this.I, -var5, var5);
            double var11 = this.I;
            this.I = this.I + Math.clamp(var9 - this.I, -720.0 * var7, 720.0 * var7);
            double var13 = (var11 + this.I) * 0.5 * var7;
            return Math.copySign(Math.min(Math.min(Math.abs(var13), var5 * var7), Math.max(0.0, Math.abs(var1) - 0.35)), var1);
         } else {
            this.h();
            return 0.0;
         }
      }

      public float O(float var1, double var2, boolean var4) {
         double var5 = var1 + var2 + this.o;
         if (var4) {
            var5 = Math.clamp(var5, -90.0, 90.0);
         }

         float var7 = (float)var5;
         this.o = var5 - var7;
         return var7;
      }

      public void h() {
         this.I = this.o = 0.0;
      }

      public double e() {
         return this.I;
      }
   }
}
