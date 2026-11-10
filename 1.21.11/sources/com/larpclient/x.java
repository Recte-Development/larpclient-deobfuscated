package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class x {
   public static final double p = 1420.0;
   private static final double s = 150.0;
   private static final double c = 610.0;
   private static final double v = 720.0;
   private static final double y = 600.0;
   private static final double J = 0.28;
   public static final int Z = 12;
   private static final double Q = 1.4;
   private final x.i g;
   private x.a f;
   private long t = -1L;
   private double T;
   private double A;
   private boolean R;
   private final Set<Integer> X = new HashSet<>();

   private x(x.i var1, boolean var2) {
      this.g = var1;
      this.f = var2 ? x.a.m : x.a.f;
   }

   public x.a l() {
      return this.f;
   }

   public boolean F() {
      return this.f == x.a.m;
   }

   public boolean W() {
      return this.f == x.a.f;
   }

   public double D() {
      return this.A;
   }

   public void h(long var1, boolean var3, boolean var4) {
      double var5 = this.t >= 0L && var1 >= this.t && var1 - this.t <= 500000000L ? (var1 - this.t) / 1000000.0 : 0.0;
      this.t = var1;
      if (var3 && this.R) {
         if (this.F()) {
            this.T += var5;
         } else if (this.f == x.a.h) {
            this.A += var5;
         } else if (this.f == x.a.C) {
            this.A -= var5 * 1.4;
         }
      }

      this.R = var3;
      if (this.f == x.a.h && (!var4 || this.A >= 1420.0)) {
         this.j();
      }

      if (this.f == x.a.C && (!var4 || this.A <= 0.0)) {
         this.j();
      }
   }

   public boolean z(boolean var1) {
      if (this.R && this.F()) {
         this.g.v = (boolean)1;
         this.f = var1 ? x.a.h : x.a.f;
         this.A = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean e(boolean var1) {
      if (this.R && this.W()) {
         this.f = var1 ? x.a.C : x.a.m;
         this.A = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void j() {
      if (this.f == x.a.C) {
         this.f = x.a.m;
         this.A = 0.0;
      } else if (!this.F()) {
         this.f = x.a.f;
         this.A = 1420.0;
      }
   }

   public void a() {
      this.g.v = (boolean)1;
      this.f = x.a.f;
      this.A = 1420.0;
   }

   public boolean c(int var1) {
      return this.X.add(var1);
   }

   public boolean E(int var1) {
      return this.X.contains(var1);
   }

   public boolean p(int var1) {
      return this.X.remove(var1);
   }

   public void K(IntPredicate var1) {
      this.X.removeIf(var1x -> !var1.test(var1x));
   }

   public static ue.c g(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new ue.c((var0 - var2) / 2, (int)(var1 * 0.48) + P(var1) / 2 + 42, var2, 28);
   }

   private static int P(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public x.v p(hc var1, boolean var2) {
      ue.c var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.x() + var3.width() / 2.0;
      double var10 = var3.y() + var3.height() / 2.0;
      double var12 = P(var1.height());
      if (this.W()) {
         return new x.v(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.T / 1800.0) * 0.025 : 0.0;
      if (this.F()) {
         return new x.v(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * e(this.A / 150.0)), 2.0);
      if (this.A < 150.0) {
         return new x.v(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = e((this.A - 150.0) / 610.0);
      double var20 = P(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new x.v(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public x.q I(hc var1, boolean var2) {
      ue.c var3 = var1.logo();
      ue.c var4 = var1.title();
      x.v var5 = this.p(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.W()) {
         return new x.q(var4.x(), 0.0, var6, 1.0);
      }

      double var8 = this.F() ? 0.0 : e((this.A - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.x() - (var3.x() + var3.width() / 2.0);
      double var16 = var5.y() - (var3.y() + var3.height() / 2.0);
      return new x.q(var5.x() + var12 + (var14 - var12) * var10, var16, var6, o(var8 / 0.25));
   }

   public x.h U(boolean var1) {
      double var2 = this.F() ? 1.0 : 1.0 - o(this.A / 150.0);
      return new x.h(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public x.h A(boolean var1) {
      double var2 = this.F() ? (var1 ? 0.78 + 0.12 * Math.sin(this.T / 900.0) : 1.0) : 1.0 - o(this.A / 110.0);
      return new x.h(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public x.h N(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.f(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private x.h f(double var1, double var3, double var5, double var7, double var9) {
      if (this.W()) {
         return x.h.REST;
      }

      double var11 = this.F() ? 0.0 : e((this.A - var1) / var3);
      double var13 = o(var11 / 0.42);
      double var15 = t(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new x.h(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
   }

   public double Y() {
      return this.W() ? 1.0 : (this.F() ? 0.12 : 0.12 + 0.88 * P((this.A - 110.0) / 820.0));
   }

   public x.d l(int var1, hc var2) {
      double var3 = !this.F() && !this.W() ? e((this.A - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new x.d(var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * o(var3 / 0.15) * (1.0 - o((var3 - 0.35) / 0.65)));
   }

   public static double e(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double o(double var0) {
      var0 = e(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double P(double var0) {
      var0 = e(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double t(double var0) {
      var0 = e(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public enum a {
      m,
      h,
      C,
      f;
   }

   public record d(double x, double y, double size, double rotation, double alpha) {
   }

   public record h(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final x.h REST = new x.h(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }

   public static final class i {
      private boolean v;

      public x J() {
         return new x(this, !this.v);
      }
   }

   public record q(double x, double lift, double clip, double alpha) {
   }

   public record v(double x, double y, double size, double stretch, double rotation) {
   }
}
