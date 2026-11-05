package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class hi {
   public static final double Y = 1420.0;
   private static final double v = 150.0;
   private static final double d = 610.0;
   private static final double y = 720.0;
   private static final double N = 600.0;
   private static final double M = 0.28;
   public static final int o = 12;
   private static final double t = 1.4;
   private final hi.o f;
   private hi.c A;
   private long G = -1L;
   private double D;
   private double B;
   private boolean R;
   private final Set<Integer> I = new HashSet<>();

   private hi(hi.o var1, boolean var2) {
      this.f = var1;
      this.A = var2 ? hi.c.w : hi.c.o;
   }

   public hi.c B() {
      return this.A;
   }

   public boolean q() {
      return this.A == hi.c.w;
   }

   public boolean K() {
      return this.A == hi.c.o;
   }

   public double X() {
      return this.B;
   }

   public void F(long var1, boolean var3, boolean var4) {
      double var5 = this.G >= 0L && var1 >= this.G && var1 - this.G <= 500000000L ? (var1 - this.G) / 1000000.0 : 0.0;
      this.G = var1;
      if (var3 && this.R) {
         if (this.q()) {
            this.D += var5;
         } else if (this.A == hi.c.O) {
            this.B += var5;
         } else if (this.A == hi.c.q) {
            this.B -= var5 * 1.4;
         }
      }

      this.R = var3;
      if (this.A == hi.c.O && (!var4 || this.B >= 1420.0)) {
         this.L();
      }

      if (this.A == hi.c.q && (!var4 || this.B <= 0.0)) {
         this.L();
      }
   }

   public boolean m(boolean var1) {
      if (this.R && this.q()) {
         this.f.x = (boolean)1;
         this.A = var1 ? hi.c.O : hi.c.o;
         this.B = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean B(boolean var1) {
      if (this.R && this.K()) {
         this.A = var1 ? hi.c.q : hi.c.w;
         this.B = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void L() {
      if (this.A == hi.c.q) {
         this.A = hi.c.w;
         this.B = 0.0;
      } else if (!this.q()) {
         this.A = hi.c.o;
         this.B = 1420.0;
      }
   }

   public void y() {
      this.f.x = (boolean)1;
      this.A = hi.c.o;
      this.B = 1420.0;
   }

   public boolean a(int var1) {
      return this.I.add(var1);
   }

   public boolean e(int var1) {
      return this.I.contains(var1);
   }

   public boolean C(int var1) {
      return this.I.remove(var1);
   }

   public void F(IntPredicate var1) {
      this.I.removeIf(var1x -> !var1.test(var1x));
   }

   public static o6.b Z(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new o6.b((var0 - var2) / 2, (int)(var1 * 0.48) + P(var1) / 2 + 42, var2, 28);
   }

   private static int P(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public hi.k v(_q var1, boolean var2) {
      o6.b var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.x() + var3.width() / 2.0;
      double var10 = var3.y() + var3.height() / 2.0;
      double var12 = P(var1.height());
      if (this.K()) {
         return new hi.k(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.D / 1800.0) * 0.025 : 0.0;
      if (this.q()) {
         return new hi.k(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * W(this.B / 150.0)), 2.0);
      if (this.B < 150.0) {
         return new hi.k(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = W((this.B - 150.0) / 610.0);
      double var20 = g(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new hi.k(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public hi.s K(_q var1, boolean var2) {
      o6.b var3 = var1.logo();
      o6.b var4 = var1.title();
      hi.k var5 = this.v(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.K()) {
         return new hi.s(var4.x(), 0.0, var6, 1.0);
      }

      double var8 = this.q() ? 0.0 : W((this.B - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.x() - (var3.x() + var3.width() / 2.0);
      double var16 = var5.y() - (var3.y() + var3.height() / 2.0);
      return new hi.s(var5.x() + var12 + (var14 - var12) * var10, var16, var6, E(var8 / 0.25));
   }

   public hi.t w(boolean var1) {
      double var2 = this.q() ? 1.0 : 1.0 - E(this.B / 150.0);
      return new hi.t(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public hi.t U(boolean var1) {
      double var2 = this.q() ? (var1 ? 0.78 + 0.12 * Math.sin(this.D / 900.0) : 1.0) : 1.0 - E(this.B / 110.0);
      return new hi.t(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public hi.t L(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.q(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private hi.t q(double var1, double var3, double var5, double var7, double var9) {
      if (this.K()) {
         return hi.t.REST;
      }

      double var11 = this.q() ? 0.0 : W((this.B - var1) / var3);
      double var13 = E(var11 / 0.42);
      double var15 = s(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new hi.t(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
   }

   public double I() {
      return this.K() ? 1.0 : (this.q() ? 0.12 : 0.12 + 0.88 * g((this.B - 110.0) / 820.0));
   }

   public hi.l S(int var1, _q var2) {
      double var3 = !this.q() && !this.K() ? W((this.B - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new hi.l(var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * E(var3 / 0.15) * (1.0 - E((var3 - 0.35) / 0.65)));
   }

   public static double W(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double E(double var0) {
      var0 = W(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double g(double var0) {
      var0 = W(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double s(double var0) {
      var0 = W(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public enum c {
      w,
      O,
      q,
      o;
   }

   public record k(double x, double y, double size, double stretch, double rotation) {
   }

   public record l(double x, double y, double size, double rotation, double alpha) {
   }

   public static final class o {
      private boolean x;

      public hi p() {
         return new hi(this, !this.x);
      }
   }

   public record s(double x, double lift, double clip, double alpha) {
   }

   public record t(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final hi.t REST = new hi.t(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }
}
