package com.larpclient;

import java.util.Random;

public final class o3 {
   public static final int z = 96;
   private static final double k = 48.0;
   private final lg[] J = new lg[96];
   private final Random W;
   private final yp R;
   private fh q;
   private boolean I;
   private double y;
   private double N;
   private double K;
   private double u;
   private int X;
   public double D;
   public double w;
   public double s;
   public double p;

   public o3(long var1, fh var3) {
      this.W = new Random(var1);
      this.R = new yp(this.W);
      this.q = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.J[var4] = new lg();
      }
   }

   public lg[] n() {
      return this.J;
   }

   public double e() {
      return this.u;
   }

   public void r(fh var1) {
      this.q = var1;
      this.g();
   }

   public void g() {
      this.X++;
   }

   public void s() {
      this.I = (boolean)0;
      this.u = 0.0;
      this.p = 0.0;

      for (lg var4 : this.J) {
         var4.h = (boolean)0;
         var4.t = lg.s.I;
         var4.v = var4.B = 0.0;
         var4.W = "";
      }
   }

   public static double w(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double X(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean W(e0 var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.y;
      double var20 = var4 - this.N;
      double var22 = var6 - this.K;
      int var24 = this.I && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.I || var24 != 0) {
         this.s();
         this.D = var2;
         this.w = var4;
         this.s = var6;
         this.I = (boolean)1;
         this.p = var1.r == e0.q.Y ? var8 : 0.0;
      }

      this.y = var2;
      this.N = var4;
      this.K = var6;
      double var25 = w(var1.T, var16);
      this.D = this.D + (var2 - this.D) * var25;
      this.w = this.w + (var4 - this.w) * var25;
      this.s = this.s + (var6 - this.s) * var25;
      double var27 = var1.r == e0.q.I ? 0.0 : var8;
      this.p = X(this.p + X(var27 - this.p) * w(2.5, var16));
      this.u += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.x != e0.b.C && (var1.x != e0.b.F || var29 == 0) ? 0 : 1;
      double var31 = (var1.x == e0.b.F ? Math.atan2(var12, var14) : var10) - this.p;
      int var33 = Math.clamp(var1.n, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         lg var35 = this.J[var34];
         if (var34 >= var33 || var35.e != this.X) {
            var35.g();
         }

         if (var35.t != lg.s.I) {
            e6.G(var35, var1, var16);
         }

         if (var35.t == lg.s.I && var34 < var33) {
            var35.B -= var16;
            if (!(var35.B > 0.0)) {
               String var36 = this.q.next(this.u, this.W);
               if (var36 == null || var36.isBlank()) {
                  var35.B = 0.1;
               } else if (!this.R.T(var35, this.J, var1, this.W, var31, (boolean)var30)) {
                  var35.B = 0.75 + this.W.nextDouble() * 0.5;
               } else {
                  var35.W = var36;
                  var35.f = this.W.nextDouble() * 2.0 - 1.0;
                  var35.p = this.W.nextDouble() * Math.PI * 2.0;
                  var35.l = 0.5 + this.W.nextDouble() * 0.7;
                  var35.u = this.W.nextDouble() * Math.PI * 2.0;
                  var35.D = (this.W.nextDouble() - 0.5) * 0.3;
                  var35.i = (this.W.nextDouble() - 0.5) * 0.16;
                  var35.T = var35.o = var35.v = 0.0;
                  var35.L = var1.K * (0.65 + this.W.nextDouble() * 0.7);
                  var35.e = this.X;
                  var35.h = (boolean)1;
                  var35.t = lg.s.A;
                  e6.G(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
