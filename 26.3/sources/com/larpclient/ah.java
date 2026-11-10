package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class ah {
   public static final double o = 1420.0;
   private static final double e = 150.0;
   private static final double c = 610.0;
   private static final double C = 720.0;
   private static final double i = 600.0;
   private static final double u = 0.28;
   public static final int L = 12;
   private static final double d = 1.4;
   private final ah.z y;
   private ah.c X;
   private long Y = -1L;
   private double j;
   private double A;
   private boolean x;
   private final Set<Integer> w = new HashSet<>();

   private ah(ah.z var1, boolean var2) {
      this.y = var1;
      this.X = var2 ? ah.c.c : ah.c.I;
   }

   public ah.c c() {
      return this.X;
   }

   public boolean y() {
      return this.X == ah.c.c;
   }

   public boolean F() {
      return this.X == ah.c.I;
   }

   public double I() {
      return this.A;
   }

   public void D(long var1, boolean var3, boolean var4) {
      double var5 = this.Y >= 0L && var1 >= this.Y && var1 - this.Y <= 500000000L ? (var1 - this.Y) / 1000000.0 : 0.0;
      this.Y = var1;
      if (var3 && this.x) {
         if (this.y()) {
            this.j += var5;
         } else if (this.X == ah.c.X) {
            this.A += var5;
         } else if (this.X == ah.c.K) {
            this.A -= var5 * 1.4;
         }
      }

      this.x = var3;
      if (this.X == ah.c.X && (!var4 || this.A >= 1420.0)) {
         this.N();
      }

      if (this.X == ah.c.K && (!var4 || this.A <= 0.0)) {
         this.N();
      }
   }

   public boolean i(boolean var1) {
      if (this.x && this.y()) {
         this.y.H = (boolean)1;
         this.X = var1 ? ah.c.X : ah.c.I;
         this.A = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean e(boolean var1) {
      if (this.x && this.F()) {
         this.X = var1 ? ah.c.K : ah.c.c;
         this.A = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void N() {
      if (this.X == ah.c.K) {
         this.X = ah.c.c;
         this.A = 0.0;
      } else if (!this.y()) {
         this.X = ah.c.I;
         this.A = 1420.0;
      }
   }

   public void R() {
      this.y.H = (boolean)1;
      this.X = ah.c.I;
      this.A = 1420.0;
   }

   public boolean l(int var1) {
      return this.w.add(var1);
   }

   public boolean y(int var1) {
      return this.w.contains(var1);
   }

   public boolean u(int var1) {
      return this.w.remove(var1);
   }

   public void E(IntPredicate var1) {
      this.w.removeIf(var1x -> !var1.test(var1x));
   }

   public static _c.f W(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new _c.f((var0 - var2) / 2, (int)(var1 * 0.48) + L(var1) / 2 + 42, var2, 28);
   }

   private static int L(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public ah.u v(qa var1, boolean var2) {
      _c.f var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.x() + var3.width() / 2.0;
      double var10 = var3.y() + var3.height() / 2.0;
      double var12 = L(var1.height());
      if (this.F()) {
         return new ah.u(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.j / 1800.0) * 0.025 : 0.0;
      if (this.y()) {
         return new ah.u(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * Y(this.A / 150.0)), 2.0);
      if (this.A < 150.0) {
         return new ah.u(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = Y((this.A - 150.0) / 610.0);
      double var20 = K(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new ah.u(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public ah.m G(qa var1, boolean var2) {
      _c.f var3 = var1.logo();
      _c.f var4 = var1.title();
      ah.u var5 = this.v(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.F()) {
         return new ah.m(var4.x(), 0.0, var6, 1.0);
      }

      double var8 = this.y() ? 0.0 : Y((this.A - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.x() - (var3.x() + var3.width() / 2.0);
      double var16 = var5.y() - (var3.y() + var3.height() / 2.0);
      return new ah.m(var5.x() + var12 + (var14 - var12) * var10, var16, var6, E(var8 / 0.25));
   }

   public ah.j t(boolean var1) {
      double var2 = this.y() ? 1.0 : 1.0 - E(this.A / 150.0);
      return new ah.j(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public ah.j l(boolean var1) {
      double var2 = this.y() ? (var1 ? 0.78 + 0.12 * Math.sin(this.j / 900.0) : 1.0) : 1.0 - E(this.A / 110.0);
      return new ah.j(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public ah.j E(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.u(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private ah.j u(double var1, double var3, double var5, double var7, double var9) {
      if (this.F()) {
         return ah.j.REST;
      }

      double var11 = this.y() ? 0.0 : Y((this.A - var1) / var3);
      double var13 = E(var11 / 0.42);
      double var15 = s(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new ah.j(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
   }

   public double e() {
      return this.F() ? 1.0 : (this.y() ? 0.12 : 0.12 + 0.88 * K((this.A - 110.0) / 820.0));
   }

   public ah.r m(int var1, qa var2) {
      double var3 = !this.y() && !this.F() ? Y((this.A - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new ah.r(var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * E(var3 / 0.15) * (1.0 - E((var3 - 0.35) / 0.65)));
   }

   public static double Y(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double E(double var0) {
      var0 = Y(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double K(double var0) {
      var0 = Y(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double s(double var0) {
      var0 = Y(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public enum c {
      c,
      X,
      K,
      I;
   }

   public record j(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final ah.j REST = new ah.j(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }

   public record m(double x, double lift, double clip, double alpha) {
   }

   public record r(double x, double y, double size, double rotation, double alpha) {
   }

   public record u(double x, double y, double size, double stretch, double rotation) {
   }

   public static final class z {
      private boolean H;

      public ah Q() {
         return new ah(this, !this.H);
      }
   }
}
