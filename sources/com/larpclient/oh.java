package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class oh {
   public static final double u = 1420.0;
   private static final double U = 150.0;
   private static final double g = 610.0;
   private static final double Y = 720.0;
   private static final double c = 600.0;
   private static final double K = 0.28;
   public static final int x = 12;
   private static final double v = 1.4;
   private final oh.z e;
   private oh.m s;
   private long z = -1L;
   private double D;
   private double d;
   private boolean r;
   private final Set<Integer> y = new HashSet<>();

   private oh(oh.z var1, boolean var2) {
      this.e = var1;
      this.s = var2 ? oh.m.p : oh.m.n;
   }

   public oh.m k() {
      return this.s;
   }

   public boolean i() {
      return this.s == oh.m.p;
   }

   public boolean g() {
      return this.s == oh.m.n;
   }

   public double F() {
      return this.d;
   }

   public void A(long var1, boolean var3, boolean var4) {
      double var5 = this.z >= 0L && var1 >= this.z && var1 - this.z <= 500000000L ? (var1 - this.z) / 1000000.0 : 0.0;
      this.z = var1;
      if (var3 && this.r) {
         if (this.i()) {
            this.D += var5;
         } else if (this.s == oh.m.w) {
            this.d += var5;
         } else if (this.s == oh.m.t) {
            this.d -= var5 * 1.4;
         }
      }

      this.r = var3;
      if (this.s == oh.m.w && (!var4 || this.d >= 1420.0)) {
         this.V();
      }

      if (this.s == oh.m.t && (!var4 || this.d <= 0.0)) {
         this.V();
      }
   }

   public boolean l(boolean var1) {
      if (this.r && this.i()) {
         this.e.e = (boolean)1;
         this.s = var1 ? oh.m.w : oh.m.n;
         this.d = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean j(boolean var1) {
      if (this.r && this.g()) {
         this.s = var1 ? oh.m.t : oh.m.p;
         this.d = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void V() {
      if (this.s == oh.m.t) {
         this.s = oh.m.p;
         this.d = 0.0;
      } else if (!this.i()) {
         this.s = oh.m.n;
         this.d = 1420.0;
      }
   }

   public void N() {
      this.e.e = (boolean)1;
      this.s = oh.m.n;
      this.d = 1420.0;
   }

   public boolean R(int var1) {
      return this.y.add(var1);
   }

   public boolean p(int var1) {
      return this.y.contains(var1);
   }

   public boolean K(int var1) {
      return this.y.remove(var1);
   }

   public void Y(IntPredicate var1) {
      this.y.removeIf(var1x -> !var1.test(var1x));
   }

   public static p5.w q(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new p5.w((var0 - var2) / 2, (int)(var1 * 0.48) + v(var1) / 2 + 42, var2, 28);
   }

   private static int v(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public oh.w a(o4 var1, boolean var2) {
      p5.w var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.x() + var3.width() / 2.0;
      double var10 = var3.y() + var3.height() / 2.0;
      double var12 = v(var1.height());
      if (this.g()) {
         return new oh.w(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.D / 1800.0) * 0.025 : 0.0;
      if (this.i()) {
         return new oh.w(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * Y(this.d / 150.0)), 2.0);
      if (this.d < 150.0) {
         return new oh.w(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = Y((this.d - 150.0) / 610.0);
      double var20 = x(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new oh.w(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public oh.l P(o4 var1, boolean var2) {
      p5.w var3 = var1.logo();
      p5.w var4 = var1.title();
      oh.w var5 = this.a(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.g()) {
         return new oh.l(var4.x(), 0.0, var6, 1.0);
      }

      double var8 = this.i() ? 0.0 : Y((this.d - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.x() - (var3.x() + var3.width() / 2.0);
      double var16 = var5.y() - (var3.y() + var3.height() / 2.0);
      return new oh.l(var5.x() + var12 + (var14 - var12) * var10, var16, var6, I(var8 / 0.25));
   }

   public oh.x G(boolean var1) {
      double var2 = this.i() ? 1.0 : 1.0 - I(this.d / 150.0);
      return new oh.x(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public oh.x k(boolean var1) {
      double var2 = this.i() ? (var1 ? 0.78 + 0.12 * Math.sin(this.D / 900.0) : 1.0) : 1.0 - I(this.d / 110.0);
      return new oh.x(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public oh.x c(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.b(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private oh.x b(double var1, double var3, double var5, double var7, double var9) {
      if (this.g()) {
         return oh.x.REST;
      }

      double var11 = this.i() ? 0.0 : Y((this.d - var1) / var3);
      double var13 = I(var11 / 0.42);
      double var15 = G(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new oh.x(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
   }

   public double B() {
      return this.g() ? 1.0 : (this.i() ? 0.12 : 0.12 + 0.88 * x((this.d - 110.0) / 820.0));
   }

   public oh.k p(int var1, o4 var2) {
      double var3 = !this.i() && !this.g() ? Y((this.d - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new oh.k(var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * I(var3 / 0.15) * (1.0 - I((var3 - 0.35) / 0.65)));
   }

   public static double Y(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double I(double var0) {
      var0 = Y(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double x(double var0) {
      var0 = Y(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double G(double var0) {
      var0 = Y(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public record k(double x, double y, double size, double rotation, double alpha) {
   }

   public record l(double x, double lift, double clip, double alpha) {
   }

   public enum m {
      p,
      w,
      t,
      n;
   }

   public record w(double x, double y, double size, double stretch, double rotation) {
   }

   public record x(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final oh.x REST = new oh.x(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }

   public static final class z {
      private boolean e;

      public oh l() {
         return new oh(this, !this.e);
      }
   }
}
