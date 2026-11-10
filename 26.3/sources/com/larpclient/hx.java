package com.larpclient;

import java.util.Random;

public final class hx {
   public static final int g = 96;
   private static final double I = 48.0;
   private final fe[] n = new fe[96];
   private final Random a;
   private final n0 e;
   private ok P;
   private boolean N;
   private double c;
   private double L;
   private double C;
   private double q;
   private int r;
   public double S;
   public double Z;
   public double X;
   public double k;

   public hx(long var1, ok var3) {
      this.a = new Random(var1);
      this.e = new n0(this.a);
      this.P = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.n[var4] = new fe();
      }
   }

   public fe[] v() {
      return this.n;
   }

   public double b() {
      return this.q;
   }

   public void D(ok var1) {
      this.P = var1;
      this.c();
   }

   public void c() {
      this.r++;
   }

   public void k() {
      this.N = (boolean)0;
      this.q = 0.0;
      this.k = 0.0;

      for (fe var4 : this.n) {
         var4.Q = (boolean)0;
         var4.z = fe.q.h;
         var4.g = var4.C = 0.0;
         var4.p = "";
      }
   }

   public static double j(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double U(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean t(jd var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.c;
      double var20 = var4 - this.L;
      double var22 = var6 - this.C;
      int var24 = this.N && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.N || var24 != 0) {
         this.k();
         this.S = var2;
         this.Z = var4;
         this.X = var6;
         this.N = (boolean)1;
         this.k = var1.Y == jd.t.b ? var8 : 0.0;
      }

      this.c = var2;
      this.L = var4;
      this.C = var6;
      double var25 = j(var1.r, var16);
      this.S = this.S + (var2 - this.S) * var25;
      this.Z = this.Z + (var4 - this.Z) * var25;
      this.X = this.X + (var6 - this.X) * var25;
      double var27 = var1.Y == jd.t.q ? 0.0 : var8;
      this.k = U(this.k + U(var27 - this.k) * j(2.5, var16));
      this.q += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.x != jd.l.y && (var1.x != jd.l.O || var29 == 0) ? 0 : 1;
      double var31 = (var1.x == jd.l.O ? Math.atan2(var12, var14) : var10) - this.k;
      int var33 = Math.clamp(var1.R, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         fe var35 = this.n[var34];
         if (var34 >= var33 || var35.w != this.r) {
            var35.l();
         }

         if (var35.z != fe.q.h) {
            bs.m(var35, var1, var16);
         }

         if (var35.z == fe.q.h && var34 < var33) {
            var35.C -= var16;
            if (!(var35.C > 0.0)) {
               String var36 = this.P.next(this.q, this.a);
               if (var36 == null || var36.isBlank()) {
                  var35.C = 0.1;
               } else if (!this.e.E(var35, this.n, var1, this.a, var31, (boolean)var30)) {
                  var35.C = 0.75 + this.a.nextDouble() * 0.5;
               } else {
                  var35.p = var36;
                  var35.A = this.a.nextDouble() * 2.0 - 1.0;
                  var35.t = this.a.nextDouble() * Math.PI * 2.0;
                  var35.s = 0.5 + this.a.nextDouble() * 0.7;
                  var35.k = this.a.nextDouble() * Math.PI * 2.0;
                  var35.d = (this.a.nextDouble() - 0.5) * 0.3;
                  var35.v = (this.a.nextDouble() - 0.5) * 0.16;
                  var35.K = var35.S = var35.g = 0.0;
                  var35.x = var1.m * (0.65 + this.a.nextDouble() * 0.7);
                  var35.w = this.r;
                  var35.Q = (boolean)1;
                  var35.z = fe.q.k;
                  bs.m(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
