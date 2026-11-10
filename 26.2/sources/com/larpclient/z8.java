package com.larpclient;

import java.util.Random;

public final class z8 {
   public static final int C = 96;
   private static final double B = 48.0;
   private final wq[] i = new wq[96];
   private final Random h;
   private final p4 p;
   private td Z;
   private boolean t;
   private double b;
   private double w;
   private double R;
   private double f;
   private int V;
   public double X;
   public double H;
   public double F;
   public double z;

   public z8(long var1, td var3) {
      this.h = new Random(var1);
      this.p = new p4(this.h);
      this.Z = var3;

      for (int var4 = 0; var4 < 96; var4++) {
         this.i[var4] = new wq();
      }
   }

   public wq[] C() {
      return this.i;
   }

   public double L() {
      return this.f;
   }

   public void W(td var1) {
      this.Z = var1;
      this.k();
   }

   public void k() {
      this.V++;
   }

   public void v() {
      this.t = (boolean)0;
      this.f = 0.0;
      this.z = 0.0;

      for (wq var4 : this.i) {
         var4.l = (boolean)0;
         var4.f = wq.y.v;
         var4.O = var4.F = 0.0;
         var4.W = "";
      }
   }

   public static double A(double var0, double var2) {
      return -Math.expm1(-var0 * Math.max(0.0, var2));
   }

   public static double O(double var0) {
      return Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public boolean r(mo var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      var16 = Math.clamp(var16, 0.0, 0.1);
      double var18 = var2 - this.b;
      double var20 = var4 - this.w;
      double var22 = var6 - this.R;
      int var24 = this.t && var18 * var18 + var20 * var20 + var22 * var22 > 2304.0 ? 1 : 0;
      if (!this.t || var24 != 0) {
         this.v();
         this.X = var2;
         this.H = var4;
         this.F = var6;
         this.t = (boolean)1;
         this.z = var1.n == mo.r.G ? var8 : 0.0;
      }

      this.b = var2;
      this.w = var4;
      this.R = var6;
      double var25 = A(var1.B, var16);
      this.X = this.X + (var2 - this.X) * var25;
      this.H = this.H + (var4 - this.H) * var25;
      this.F = this.F + (var6 - this.F) * var25;
      double var27 = var1.n == mo.r.b ? 0.0 : var8;
      this.z = O(this.z + O(var27 - this.z) * A(2.5, var16));
      this.f += var16;
      int var29 = var12 * var12 + var14 * var14 > 1.0E-4 ? 1 : 0;
      int var30 = var1.i != mo.t.G && (var1.i != mo.t.Q || var29 == 0) ? 0 : 1;
      double var31 = (var1.i == mo.t.Q ? Math.atan2(var12, var14) : var10) - this.z;
      int var33 = Math.clamp(var1.g, 0, 96);

      for (int var34 = 0; var34 < 96; var34++) {
         wq var35 = this.i[var34];
         if (var34 >= var33 || var35.y != this.V) {
            var35.r();
         }

         if (var35.f != wq.y.v) {
            l.n(var35, var1, var16);
         }

         if (var35.f == wq.y.v && var34 < var33) {
            var35.F -= var16;
            if (!(var35.F > 0.0)) {
               String var36 = this.Z.next(this.f, this.h);
               if (var36 == null || var36.isBlank()) {
                  var35.F = 0.1;
               } else if (!this.p.C(var35, this.i, var1, this.h, var31, (boolean)var30)) {
                  var35.F = 0.75 + this.h.nextDouble() * 0.5;
               } else {
                  var35.W = var36;
                  var35.P = this.h.nextDouble() * 2.0 - 1.0;
                  var35.q = this.h.nextDouble() * Math.PI * 2.0;
                  var35.V = 0.5 + this.h.nextDouble() * 0.7;
                  var35.G = this.h.nextDouble() * Math.PI * 2.0;
                  var35.J = (this.h.nextDouble() - 0.5) * 0.3;
                  var35.h = (this.h.nextDouble() - 0.5) * 0.16;
                  var35.u = var35.I = var35.O = 0.0;
                  var35.R = var1.K * (0.65 + this.h.nextDouble() * 0.7);
                  var35.y = this.V;
                  var35.l = (boolean)1;
                  var35.f = wq.y.d;
                  l.n(var35, var1, 0.0);
               }
            }
         }
      }

      return (boolean)var24;
   }
}
