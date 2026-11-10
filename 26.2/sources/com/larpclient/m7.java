package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class m7 {
   public static final double L = 1420.0;
   private static final double n = 150.0;
   private static final double h = 610.0;
   private static final double d = 720.0;
   private static final double F = 600.0;
   private static final double I = 0.28;
   public static final int a = 12;
   private static final double H = 1.4;
   private final m7.m w;
   private m7.l e;
   private long f = -1L;
   private double o;
   private double N;
   private boolean l;
   private final Set<Integer> g = new HashSet<>();

   private m7(m7.m var1, boolean var2) {
      this.w = var1;
      this.e = var2 ? m7.l.B : m7.l.a;
   }

   public m7.l M() {
      return this.e;
   }

   public boolean G() {
      return this.e == m7.l.B;
   }

   public boolean l() {
      return this.e == m7.l.a;
   }

   public double U() {
      return this.N;
   }

   public void w(long var1, boolean var3, boolean var4) {
      double var5 = this.f >= 0L && var1 >= this.f && var1 - this.f <= 500000000L ? (var1 - this.f) / 1000000.0 : 0.0;
      this.f = var1;
      if (var3 && this.l) {
         if (this.G()) {
            this.o += var5;
         } else if (this.e == m7.l.l) {
            this.N += var5;
         } else if (this.e == m7.l.R) {
            this.N -= var5 * 1.4;
         }
      }

      this.l = var3;
      if (this.e == m7.l.l && (!var4 || this.N >= 1420.0)) {
         this.S();
      }

      if (this.e == m7.l.R && (!var4 || this.N <= 0.0)) {
         this.S();
      }
   }

   public boolean H(boolean var1) {
      if (this.l && this.G()) {
         this.w.r = (boolean)1;
         this.e = var1 ? m7.l.l : m7.l.a;
         this.N = 0.0;
         return true;
      } else {
         return false;
      }
   }

   public boolean g(boolean var1) {
      if (this.l && this.l()) {
         this.e = var1 ? m7.l.R : m7.l.B;
         this.N = var1 ? 1420.0 : 0.0;
         return true;
      } else {
         return false;
      }
   }

   public void S() {
      if (this.e == m7.l.R) {
         this.e = m7.l.B;
         this.N = 0.0;
      } else if (!this.G()) {
         this.e = m7.l.a;
         this.N = 1420.0;
      }
   }

   public void v() {
      this.w.r = (boolean)1;
      this.e = m7.l.a;
      this.N = 1420.0;
   }

   public boolean f(int var1) {
      return this.g.add(var1);
   }

   public boolean G(int var1) {
      return this.g.contains(var1);
   }

   public boolean W(int var1) {
      return this.g.remove(var1);
   }

   public void k(IntPredicate var1) {
      this.g.removeIf(var1x -> !var1.test(var1x));
   }

   public static p1.e T(int var0, int var1) {
      int var2 = Math.min(188, var0 - 32);
      return new p1.e((var0 - var2) / 2, (int)(var1 * 0.48) + b(var1) / 2 + 42, var2, 28);
   }

   private static int b(int var0) {
      return (int)Math.clamp(var0 * 0.16, 30.0, 86.0);
   }

   public m7.h L(uz var1, boolean var2) {
      p1.e var3 = var1.logo();
      double var4 = var1.width() / 2.0;
      double var6 = var1.height() * 0.48;
      double var8 = var3.x() + var3.width() / 2.0;
      double var10 = var3.y() + var3.height() / 2.0;
      double var12 = b(var1.height());
      if (this.l()) {
         return new m7.h(var8, var10, var3.width(), 1.0, 0.0);
      }

      double var14 = var2 ? Math.sin(this.o / 1800.0) * 0.025 : 0.0;
      if (this.G()) {
         return new m7.h(var4, var6, var12, 1.0, var14);
      }

      double var16 = Math.pow(Math.sin(Math.PI * D(this.N / 150.0)), 2.0);
      if (this.N < 150.0) {
         return new m7.h(var4, var6 + 5.0 * var16, var12 * (1.0 + 0.06 * var16), 1.0 - 0.15 * var16, var14 - 0.055 * var16);
      }

      double var18 = D((this.N - 150.0) / 610.0);
      double var20 = k(var18);
      double var22 = Math.sin(Math.PI * var20);
      double var24 = Math.sin((Math.PI * 2) * var18) * Math.pow(1.0 - var18, 2.0);
      return new m7.h(
         var4 + (var8 - var4) * var20 + Math.min(42.0, var1.width() * 0.06) * var22,
         var6 + (var10 - var6) * var20 - 24.0 * var22,
         var12 + (var3.width() - var12) * var20,
         1.0 + 0.13 * var24,
         var14 * (1.0 - var20) - 0.28 * var22 + 0.1 * var24
      );
   }

   public m7.e Q(uz var1, boolean var2) {
      p1.e var3 = var1.logo();
      p1.e var4 = var1.title();
      m7.h var5 = this.L(var1, var2);
      double var6 = var5.x() + var5.size() * 0.28;
      if (this.l()) {
         return new m7.e(var4.x(), 0.0, var6, 1.0);
      }

      double var8 = this.G() ? 0.0 : D((this.N - 720.0) / 600.0);
      double var10 = 1.0 - Math.pow(1.0 - var8, 3.0);
      double var12 = var3.width() * 0.28 - var4.width() - 2.0;
      double var14 = var4.x() - (var3.x() + var3.width() / 2.0);
      double var16 = var5.y() - (var3.y() + var3.height() / 2.0);
      return new m7.e(var5.x() + var12 + (var14 - var12) * var10, var16, var6, N(var8 / 0.25));
   }

   public m7.q j(boolean var1) {
      double var2 = this.G() ? 1.0 : 1.0 - N(this.N / 150.0);
      return new m7.q(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public m7.q S(boolean var1) {
      double var2 = this.G() ? (var1 ? 0.78 + 0.12 * Math.sin(this.o / 900.0) : 1.0) : 1.0 - N(this.N / 110.0);
      return new m7.q(0.0, 0.0, 1.0, 1.0, 0.0, var2);
   }

   public m7.q Z(int var1) {
      int var2 = var1 == 5 ? 1 : 0;
      int var3 = var1 < 3 ? 1 : 0;
      return this.t(
         var2 != 0 ? 700 + var1 * 70 + 50 : 700 + var1 * 70,
         440.0,
         var3 != 0 ? 27.0 : 16.0,
         var2 != 0 ? -10.0 : (var1 % 2 == 0 ? -7 : 7),
         var3 != 0 ? 0.78 : 0.9
      );
   }

   private m7.q t(double var1, double var3, double var5, double var7, double var9) {
      if (this.l()) {
         return m7.q.REST;
      }

      double var11 = this.G() ? 0.0 : D((this.N - var1) / var3);
      double var13 = N(var11 / 0.42);
      double var15 = j(var11);
      double var17 = Math.sin(Math.PI * var11) * (1.0 - var11);
      return new m7.q(var7 * var17, var5 * (1.0 - var15), var9 + (1.0 - var9) * var15, var9 + (1.0 - var9) * var15, 0.022 * var7 / 10.0 * var17, var13);
   }

   public double o() {
      return this.l() ? 1.0 : (this.G() ? 0.12 : 0.12 + 0.88 * k((this.N - 110.0) / 820.0));
   }

   public m7.b U(int var1, uz var2) {
      double var3 = !this.G() && !this.l() ? D((this.N - 180.0 - var1 % 3 * 20) / 660.0) : 0.0;
      double var5 = var1 % 2 == 0 ? -1.0 : 1.0;
      double var7 = -0.9 + var1 / 2.0 * 0.29;
      double var9 = Math.min(var2.width() * 0.43, 270.0) * (1.0 - Math.pow(1.0 - var3, 3.0));
      double var11 = var2.width() / 2.0 + var5 * (20.0 + var9 * Math.cos(var7));
      double var13 = var2.height() * 0.48 + var9 * 0.35 * Math.sin(var7) - 44.0 * Math.sin(Math.PI * var3) + 35.0 * var3 * var3;
      return new m7.b(var11, var13, (3 + var1 % 3 * 2) * (1.0 - 0.6 * var3), var5 * var3 * 2.3, 0.32 * N(var3 / 0.15) * (1.0 - N((var3 - 0.35) / 0.65)));
   }

   public static double D(double var0) {
      return Math.clamp(var0, 0.0, 1.0);
   }

   private static double N(double var0) {
      var0 = D(var0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double k(double var0) {
      var0 = D(var0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double j(double var0) {
      var0 = D(var0);
      double var2 = var0 - 1.0;
      return 1.0 + 2.4 * var2 * var2 * var2 + 1.4 * var2 * var2;
   }

   public record b(double x, double y, double size, double rotation, double alpha) {
   }

   public record e(double x, double lift, double clip, double alpha) {
   }

   public record h(double x, double y, double size, double stretch, double rotation) {
   }

   public enum l {
      B,
      l,
      R,
      a;
   }

   public static final class m {
      private boolean r;

      public m7 t() {
         return new m7(this, !this.r);
      }
   }

   public record q(double x, double y, double scaleX, double scaleY, double rotation, double alpha) {
      public static final m7.q REST = new m7.q(0.0, 0.0, 1.0, 1.0, 0.0, 1.0);
   }
}
