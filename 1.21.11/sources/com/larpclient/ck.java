package com.larpclient;

import java.util.Random;

public final class ck {
   public static final int c = 96;
   private static final double O = 48.0;
   private final bz[] z = new bz[96];
   private final Random r;
   private final jd T;
   private d Z;
   private boolean d;
   private double B;
   private double W;
   private double D;
   private double X;
   private int R;
   public double K;
   public double p;
   public double u;
   public double e;

   public ck(long var1, d var3) {
      this.r = new Random(var1);
      this.T = new jd(this.r);
      this.Z = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.z[var4] = new bz();
      }
   }

   public bz[] d() {
      return this.z;
   }

   public double k() {
      return this.X;
   }

   public void V(d var1) {
      this.Z = var1;
      this.v();
   }

   public void v() {
      this.R++;
   }

   public void J() {
      this.d = (boolean)0;
      this.X = 0.0;
      this.e = 0.0;

      for (bz var4 : this.z) {
         var4.S = (boolean)0;
         var4.X = bz.o.H;
         var4.M = var4.x = 0.0;
         var4.k = "";
      }
   }

   public static double h(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double J(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean C(ic var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.B;
      double var20 = var4 - this.W;
      double var22 = var6 - this.D;
      int var24 = this.d && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.d || var24 != 0) {
         this.J();
         this.K = var2;
         this.p = var4;
         this.u = var6;
         this.d = (boolean)1;
         this.e = var1.r == ic.u.q ? var8 : 0.0;
      }

      this.B = var2;
      this.W = var4;
      this.D = var6;
      double var25 = h(var1.L, var16);
      this.K = this.K + (var2 - this.K) * var25;
      this.p = this.p + (var4 - this.p) * var25;
      this.u = this.u + (var6 - this.u) * var25;
      double var27 = var1.r == ic.u.T ? 0.0 : var8;
      this.e = J(this.e + J(var27 - this.e) * h(2.5, var16));
      this.X += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.a != ic.p.v && (var1.a != ic.p.M || var29 == 0) ? 0 : 1;
      double var31 = (var1.a == ic.p.M ? Math.atan2(var12, var14) : var10) - this.e;
      int var33 = Math.clamp(var1.l, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         bz var35 = this.z[var34];
         if (var34 >= var33 || var35.P != this.R) {
            var35.g();
         }

         if (var35.X != bz.o.H) {
            u7.m(var35, var1, var16);
         }

         if (var35.X == bz.o.H && var34 < var33) {
            var35.x -= var16;
            if (!(var35.x > 0.0)) {
               String var36 = this.Z.next(this.X, this.r);
               if (var36 == null || var36.isBlank()) {
                  var35.x = 0.1;
               } else if (!this.T.a(var35, this.z, var1, this.r, var31, (boolean)var30)) {
                  var35.x = 0.75 + this.r.nextDouble() * 0.5;
               } else {
                  var35.k = var36;
                  var35.w = this.r.nextDouble() * 2.0 - 1.0;
                  var35.o = this.r.nextDouble() * Math.PI * 2.0;
                  var35.a = 0.5 + this.r.nextDouble() * 0.7;
                  var35.W = this.r.nextDouble() * Math.PI * 2.0;
                  var35.Y = (this.r.nextDouble() - 0.5) * 0.3;
                  var35.d = (this.r.nextDouble() - 0.5) * 0.16;
                  var35.J = var35.e = var35.M = 0.0;
                  var35.u = var1.Y * (0.65 + this.r.nextDouble() * 0.7);
                  var35.P = this.R;
                  var35.S = (boolean)1;
                  var35.X = bz.o.B;
                  u7.m(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
