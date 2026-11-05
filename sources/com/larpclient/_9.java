package com.larpclient;

import java.util.Random;

public final class _9 {
   public static final int Z = 96;
   private static final double c = 48.0;
   private final t6[] B = new t6[96];
   private final Random j;
   private final tn Y;
   private oy y;
   private boolean S;
   private double k;
   private double V;
   private double b;
   private double m;
   private int r;
   public double t;
   public double z;
   public double P;
   public double n;

   public _9(long var1, oy var3) {
      this.j = new Random(var1);
      this.Y = new tn(this.j);
      this.y = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.B[var4] = new t6();
      }
   }

   public t6[] b() {
      return this.B;
   }

   public double t() {
      return this.m;
   }

   public void I(oy var1) {
      this.y = var1;
      this.R();
   }

   public void R() {
      this.r++;
   }

   public void G() {
      this.S = (boolean)0;
      this.m = 0.0;
      this.n = 0.0;

      for (t6 var4 : this.B) {
         var4.g = (boolean)0;
         var4.P = t6.l.X;
         var4.G = var4.k = 0.0;
         var4.W = "";
      }
   }

   public static double I(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double E(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean u(mf var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.k;
      double var20 = var4 - this.V;
      double var22 = var6 - this.b;
      int var24 = this.S && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.S || var24 != 0) {
         this.G();
         this.t = var2;
         this.z = var4;
         this.P = var6;
         this.S = (boolean)1;
         this.n = var1.R == mf.b.v ? var8 : 0.0;
      }

      this.k = var2;
      this.V = var4;
      this.b = var6;
      double var25 = I(var1.P, var16);
      this.t = this.t + (var2 - this.t) * var25;
      this.z = this.z + (var4 - this.z) * var25;
      this.P = this.P + (var6 - this.P) * var25;
      double var27 = var1.R == mf.b.E ? 0.0 : var8;
      this.n = E(this.n + E(var27 - this.n) * I(2.5, var16));
      this.m += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.a != mf.t.z && (var1.a != mf.t.B || var29 == 0) ? 0 : 1;
      double var31 = (var1.a == mf.t.B ? Math.atan2(var12, var14) : var10) - this.n;
      int var33 = Math.clamp(var1.X, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         t6 var35 = this.B[var34];
         if (var34 >= var33 || var35.V != this.r) {
            var35.y();
         }

         if (var35.P != t6.l.X) {
            o5.k(var35, var1, var16);
         }

         if (var35.P == t6.l.X && var34 < var33) {
            var35.k -= var16;
            if (!(var35.k > 0.0)) {
               String var36 = this.y.next(this.m, this.j);
               if (var36 == null || var36.isBlank()) {
                  var35.k = 0.1;
               } else if (!this.Y.G(var35, this.B, var1, this.j, var31, (boolean)var30)) {
                  var35.k = 0.75 + this.j.nextDouble() * 0.5;
               } else {
                  var35.W = var36;
                  var35.u = this.j.nextDouble() * 2.0 - 1.0;
                  var35.R = this.j.nextDouble() * Math.PI * 2.0;
                  var35.T = 0.5 + this.j.nextDouble() * 0.7;
                  var35.E = this.j.nextDouble() * Math.PI * 2.0;
                  var35.p = (this.j.nextDouble() - 0.5) * 0.3;
                  var35.J = (this.j.nextDouble() - 0.5) * 0.16;
                  var35.b = var35.j = var35.G = 0.0;
                  var35.q = var1.r * (0.65 + this.j.nextDouble() * 0.7);
                  var35.V = this.r;
                  var35.g = (boolean)1;
                  var35.P = t6.l.p;
                  o5.k(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
