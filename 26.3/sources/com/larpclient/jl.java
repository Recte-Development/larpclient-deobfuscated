package com.larpclient;

import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class jl {
   public static final double s = 0.4;
   public static final double o = 0.35;
   public static final double c = 2.3;
   public static final double t = 2.6;
   public static final double u = 2.82;
   public static final double CW = 3.25;
   public static final double z = 2.7;
   public static final double n = 5.3;
   public static final double P = 5.6;
   public static final double U = 6.4;
   public static final int b = 1000;
   public static final int M = -67000;
   public static final double CE = -2.7;
   public static final double Z = 1.45;
   public static final double v = 0.1;
   public static final double F = 3.95;
   public static final double R = -2.35;
   public static final double CY = -0.32;
   public static final double j = 0.6;
   public static final double D = 3.3;
   public static final double x = 1.12;
   public static final double CS = 0.45;
   public static final double CB = 1.9;
   public static final double Cz = 0.18;
   public static final int S = 48;
   public static final int Q = 12;
   public static final int g = 16;
   public static final int a = 7;
   public static final int L = 18;
   public static final double C1 = 1.35;
   public static final double CL = 2.8;
   public static final double k = 1.05;
   public static final double T = 1.55;
   public static final String J = "YOU JUST GOT RUG PULLED";
   public static final String Cd = "LOSER.";
   public static final double w = 2.75;
   public static final double I = 2.24;
   public static final double m = 0.42;
   public static final double h = 0.32;
   public static final double Y = 0.25;
   public static final double B = 0.93;
   public static final double N = 0.45;
   public static final double V = 0.7;
   public static final double O = 0.7;
   private static final int X = -3549466;
   private static final int G = -10656141;
   private static final int y = -6217428;
   private static final int A = -1920436;
   private static final int d = -13242230;
   private static final int Ce = -50360;
   private final n5 e;
   private final double[] p = new double[49];
   private final double[] H = new double[49];
   private final double[] i = new double[12];
   private final double[] Cr = new double[12];
   private final double[] C = new double[16];
   private final double[] W = new double[16];
   private final double[] K = new double[16];
   private final double[] f = new double[7];
   private final double[] Cc = new double[7];
   private final double[] r = new double[18];
   private final double[] Ci = new double[18];
   private final double[] l = new double[18];
   private double E = Double.NaN;
   private double q = Double.NaN;

   jl(n5 var1) {
      this.e = var1;
      double var2 = Math.exp(2.4) - 1.0;
      double[] var4 = new double[]{var1.w(15000) * Math.PI * 2.0, var1.w(15001) * Math.PI * 2.0, var1.w(15002) * Math.PI * 2.0};

      for (int var5 = 0; var5 <= 48; var5++) {
         double var6 = var5 / 48.0;
         double var8 = (Math.exp(2.4 * var6) - 1.0) / var2;
         double var10 = Math.sin(var6 * Math.PI * 5.0 + var4[0]) * 0.5
            + Math.sin(var6 * Math.PI * 9.0 + var4[1]) * 0.25
            + Math.sin(var6 * Math.PI * 15.0 + var4[2]) * 0.17;
         this.p[var5] = -2.35 + 2.0300000000000002 * var6;
         this.H[var5] = 0.6 + 2.6999999999999997 * var8 + var10 * 0.16 * Math.sin(Math.PI * var6);
      }

      for (int var12 = 0; var12 < 12; var12++) {
         this.i[var12] = 0.03 + var1.w(15100 + var12) * 0.07;
         this.Cr[var12] = 0.03 + var1.w(15200 + var12) * 0.07;
      }

      for (int var13 = 0; var13 < 16; var13++) {
         this.C[var13] = 0.7 + 1.5499999999999998 * var13 / 15.0 + var1.w(15300 + var13) * 0.08;
         this.W[var13] = var1.w(15400 + var13) * 2.0 - 1.0;
         this.K[var13] = var1.w(15500 + var13) * Math.PI * 2.0;
      }

      for (int var14 = 0; var14 < 7; var14++) {
         this.f[var14] = (var14 + var1.w(15600 + var14) * 0.6) * Math.PI * 2.0 / 7.0;
         this.Cc[var14] = 0.9 + var1.w(15700 + var14) * 0.8;
      }

      for (int var15 = 0; var15 < 18; var15++) {
         this.r[var15] = var1.w(15800 + var15) * Math.PI * 2.0;
         this.Ci[var15] = 0.9 + var1.w(15900 + var15) * 1.4;
         this.l[var15] = 2.6 + var1.w(16000 + var15) * 2.4;
      }
   }

   public static double i(double var0) {
      return n5.g((var0 - 0.35) / 1.9499999999999997);
   }

   public static double N(double var0) {
      return n5.g(var0 / 0.4);
   }

   public static double H(double var0) {
      return n5.g((6.4 - var0) / 0.8000000000000007);
   }

   public static double G(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      } else if (var0 < 2.82) {
         double var2 = (var0 - 2.6) / 0.21999999999999975;
         return var2 * var2;
      } else {
         return 1.0 + Math.min(1.0, (var0 - 2.82) / 0.18);
      }
   }

   public int t() {
      return 49;
   }

   public double W(int var1) {
      return this.p[var1];
   }

   public double S(int var1) {
      return this.H[var1];
   }

   public double[] u(double var1) {
      double var3 = i(var1) * 48.0;
      int var5 = Math.min(47, (int)Math.floor(var3));
      double var6 = var3 - var5;
      return new double[]{this.p[var5] + (this.p[var5 + 1] - this.p[var5]) * var6, this.H[var5] + (this.H[var5 + 1] - this.H[var5]) * var6};
   }

   public double z(double var1) {
      if (var1 < 2.6) {
         double var7 = this.u(var1)[1];
         return Math.round(1000.0 * Math.clamp((var7 - 0.6) / 2.6999999999999997, 0.0, 1.0));
      } else {
         double var3 = Math.clamp((var1 - 2.6) / 0.6499999999999999, 0.0, 1.0);
         double var5 = 1.0 - Math.pow(1.0 - var3, 3.0);
         return Math.round(1000.0 + -68000.0 * var5);
      }
   }

   public static String v(double var0) {
      long var2 = Math.round(var0);
      return (var2 < 0L ? "-$" : "$") + String.format(Locale.ROOT, "%,d", Math.abs(var2));
   }

   public hg.k L(Vec3 var1, double var2) {
      double var4 = var1.x - this.e.r.x;
      double var6 = var1.z - this.e.r.z;
      double var8 = var4 * var4 + var6 * var6 < 1.0E-6 ? (Double.isNaN(this.E) ? this.e.y : this.E) : Math.atan2(var4, var6);
      if (!Double.isNaN(this.E) && !Double.isNaN(this.q) && !(var2 < this.q)) {
         double var10 = Math.IEEEremainder(var8 - this.E, Math.PI * 2);
         this.E = this.E + var10 * (1.0 - Math.exp(-Math.min(0.25, var2 - this.q) * 9.0));
      } else {
         this.E = var8;
      }

      this.q = var2;
      return g(this.E);
   }

   public static hg.k g(double var0) {
      double var2 = Math.sin(var0);
      double var4 = Math.cos(var0);
      return new hg.k(new Vec3(var4, 0.0, -var2), new Vec3(0.0, 1.0, 0.0), new Vec3(var2, 0.0, var4));
   }

   public Vec3 h(hg.k var1, double var2, double var4) {
      Vec3 var6 = this.e.r.subtract(var1.normal().scale(0.45));
      return var6.add(var1.right().scale(var2)).add(0.0, var4, 0.0);
   }

   public Vec3 E(hg.k var1, double var2) {
      double var4 = G(var2);
      Vec3 var6 = this.h(var1, -0.32, 3.3);
      Vec3 var7 = this.e.r;
      Vec3 var8 = this.e.r.add(0.0, -1.9, 0.0);
      return var4 <= 1.0 ? var6.lerp(var7, var4) : var7.lerp(var8, var4 - 1.0);
   }

   private Vec3 I() {
      return new Vec3(-Math.sin(this.e.y), 0.0, Math.cos(this.e.y));
   }

   private Vec3 u() {
      return new Vec3(Math.cos(this.e.y), 0.0, Math.sin(this.e.y));
   }

   public static double V(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      }

      double var2 = var0 - 2.6;
      return 11.0 * var2 * var2 + 0.4 * var2;
   }

   public static n5.p V(n5 var0, int var1, double var2) {
      double var4 = n5.H(var1, var0.Q) / 16.0 * 0.9375;
      double var6 = (24.0 - n5.a[var1][1]) / 16.0 * 0.9375;
      double var8 = n5.g((var2 - 0.35) / 0.5) * n5.g((2.6 - var2) / 0.15);
      double var10 = 0.035 * Math.abs(Math.sin((var2 - 0.35) * Math.PI * 2.6)) * var8;
      double var12 = var2 < 2.6 ? 0.0 : Math.pow(Math.min(1.0, (var2 - 2.6) / 0.32), 2.0);
      double var14 = 1.35 * var12;
      double var16 = 0.26 * n5.g((var2 - 0.35) / 0.5) + 0.7 * n5.g((var2 - 2.6) / 0.25);
      double var18 = var1 == 2 ? -var16 : (var1 == 3 ? var16 : 0.0);
      double var20 = 0.35 * n5.g((var2 - 2.6) / 0.3);
      double var22 = var2 < 2.82 ? 0.0 : Math.min(2.8, 6.5 * (var2 - 2.82) * (var2 - 2.82));
      double var24 = Math.cos(var0.y);
      double var26 = Math.sin(var0.y);
      double var28 = var6 * Math.sin(var14);
      Vec3 var30 = var0.r
         .add(var4 * var24 + var28 * var26 - var20 * var26, var6 * Math.cos(var14) + var10 - var22, var4 * var26 - var28 * var24 + var20 * var24);
      double var31 = 1.0 - n5.g((var2 - 2.82 - 0.3) / 0.45);
      return new n5.p(var30, var14, 0.0, var18, 1.0, var31, 0.0, false);
   }

   public void g(hg.k var1, double var2, double var4, ToIntFunction<String> var6, List<__.n> var7) {
      double var8 = N(var2) * H(var2) * var4;
      if (!(var8 < 0.01)) {
         Quaternionf var10 = new Quaternionf().rotateY((float)Math.atan2(var1.normal().x, var1.normal().z));
         double var11 = n5.g((var2 - 2.6) / 0.12);

         for (int var13 = 0; var13 <= 2; var13++) {
            double var14 = 0.6 + 2.6999999999999997 * var13 / 2.0;
            String var16 = var13 == 0 ? "$0" : (var13 == 1 ? "$500" : "$1K");
            c(var7, var6, var16, this.h(var1, 1.2400000000000002, var14), var1, 0.26, vv.O(-6641994, var8 * 0.75), var10, true);
         }

         double[] var29 = this.u(Math.min(var2, 2.6));
         double var30 = var2 < 2.6 ? 0.0 : 0.045 * (1.0 - n5.g((var2 - 3.25) / 0.6));
         double var31 = var30 * Math.sin(var2 * 71.0);
         double var18 = var30 * Math.sin(var2 * 53.0 + 1.3);
         double var20 = 1.0 + 0.35 * n5.r(var2, 2.6, 3.1500000000000004) + 0.06 * var11;
         double var22 = n5.g((var2 - 0.35) / 0.2);
         Vec3 var24 = this.h(var1, var29[0] + var31, var29[1] + 0.48 + var18);
         int var25 = vv.Q(-13242230, -50360, var11);
         c(var7, var6, v(this.z(var2)), var24, var1, 0.62 * var20, vv.O(var25, var8 * var22), var10, false);
         if (var2 >= 2.6) {
            String var26 = "-" + String.format(Locale.ROOT, "%,d", Math.round((1000.0 - this.z(var2)) * 100.0 / 1000.0)) + "%";
            c(var7, var6, var26, this.h(var1, var29[0] + var31, var29[1] + 0.02 + var18), var1, 0.3, vv.O(-50360, var8 * var11 * 0.9), var10, false);
         }

         if (var2 >= 2.7) {
            Vec3 var32 = this.e.r.add(var1.normal().scale(0.25));
            double var27 = Math.clamp((var2 - 5.3) / 0.7, 0.0, 1.0);
            p(var7, "YOU JUST GOT RUG PULLED", var32.add(0.0, 2.75, 0.0), 0.42, -50360, (var2 - 2.7) / 0.93, var27, var2, var4, var10);
            p(var7, "LOSER.", var32.add(0.0, 2.24, 0.0), 0.32, -854792, (var2 - 2.7 - 0.45) / 0.7, var27, var2, var4, var10);
         }
      }
   }

   private static void p(
      List<__.n> var0, String var1, Vec3 var2, double var3, int var5, double var6, double var8, double var10, double var12, Quaternionf var14
   ) {
      if (!(var6 <= 0.0) && !(var8 >= 1.0) && !(var12 <= 0.004)) {
         __.l var15 = new __.l(3, Math.min(1.0, var6), var8, var10, 0);
         var0.add(new __.n(var1, var2.x, var2.y, var2.z, var3 / 0.605, vv.O(var5, var12), var14.x, var14.y, var14.z, var14.w, true, var15));
      }
   }

   private static void c(List<__.n> var0, ToIntFunction<String> var1, String var2, Vec3 var3, hg.k var4, double var5, int var7, Quaternionf var8, boolean var9) {
      if (var7 >>> 24 != 0) {
         double var10 = var5 / 0.605;
         Vec3 var12 = var3.add(var4.normal().scale(0.01));
         if (var9) {
            var12 = var12.add(var4.right().scale(var1.applyAsInt(var2) * var5 / 32.0 * 0.5));
         }

         var0.add(new __.n(var2, var12.x, var12.y, var12.z, var10, var7, var8.x, var8.y, var8.z, var8.w, true));
      }
   }

   public static void p(fz var0, n5 var1, double var2, int var4, int var5, double var6, boolean var8, hg.k var9) {
      jl var10 = var1.R();
      if (var10 != null && var9 != null) {
         double var11 = N(var2);
         double var13 = H(var2);
         double var15 = var6 * var11 * var13;
         if (!(var15 <= 0.001)) {
            double var17 = n5.g((var2 - 2.6) / 0.6);
            int var19 = vv.Q(var4, -10656141, var17 * 0.7);
            if (!var8) {
               var0.blend(_0.v.z);
               var10.H(var0, var9, var15);
               var10.f(var0, var9, var2, var19, var15 * (1.0 - var17 * 0.6));
               var10.N(var0, var9, var2, var4, var5, var17, var15);
               var10.U(var0, var2, var6 * var13);
               var10.m(var0, var2, var5, var6 * var13);
               var10.M(var0, var9, var2, var6 * var13);
            }

            var0.blend(_0.v.c);
            var10.U(var0, var9, var2, var19, var15 * (var8 ? 0.8 : 1.0));
            var10.f(var0, var9, var2, var19, var5, var15);
            var10.z(var0, var9, var2, var5, var15);
            var10.t(var0, var2, var5, var6 * var13);
         }
      }
   }

   private void l(fz var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6, int var7, int var8, int var9) {
      var1.vertex(var2.x, var2.y, var2.z, var6);
      var1.vertex(var3.x, var3.y, var3.z, var7);
      var1.vertex(var4.x, var4.y, var4.z, var8);
      var1.vertex(var5.x, var5.y, var5.z, var9);
   }

   private void I(fz var1, hg.k var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      this.l(var1, this.h(var2, var3, var5), this.h(var2, var7, var5), this.h(var2, var7, var9), this.h(var2, var3, var9), var11, var11, var12, var12);
   }

   private void d(fz var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6) {
      Vec3 var7 = var3.subtract(var2).cross(var4.subtract(var2));
      if (var7.y >= 0.0) {
         this.l(var1, var2, var3, var4, var5, var6, var6, var6, var6);
      } else {
         this.l(var1, var5, var4, var3, var2, var6, var6, var6, var6);
      }
   }

   private void H(fz var1, hg.k var2, double var3) {
      double var5 = n5.g((var3 - 0.3) / 0.7);
      int var7 = vv.O(-3549466, var3 * 0.075 * var5);

      for (int var8 = 0; var8 <= 4; var8++) {
         double var9 = 0.6 + 2.6999999999999997 * var8 / 4.0;
         var1.ribbon(this.h(var2, -2.58, var9), this.h(var2, 1.08, var9), 0.012, var7, var7);
      }

      for (int var11 = 0; var11 <= 5; var11++) {
         double var13 = -2.35 + 3.47 * var11 / 5.0;
         var1.ribbon(this.h(var2, var13, 0.2), this.h(var2, var13, 3.5), 0.012, var7, var7);
      }

      int var12 = vv.O(-3549466, var3 * 0.22 * var5);
      var1.ribbon(this.h(var2, 1.12, 0.2), this.h(var2, 1.12, 3.5), 0.016, var12, var12);
   }

   private void f(fz var1, hg.k var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = i(var3) * 48.0;
         int var10 = vv.O(var5, var6 * 0.3);
         int var11 = vv.O(var5, 0.0);
         double var12 = 0.16;

         for (int var14 = 0; var14 < 48 && var14 < var8; var14++) {
            double var15 = Math.min(1.0, var8 - var14);
            double var17 = this.p[var14] + (this.p[var14 + 1] - this.p[var14]) * var15;
            double var19 = this.H[var14] + (this.H[var14 + 1] - this.H[var14]) * var15;
            this.l(
               var1,
               this.h(var2, this.p[var14], var12),
               this.h(var2, var17, var12),
               this.h(var2, var17, var19),
               this.h(var2, this.p[var14], this.H[var14]),
               var11,
               var11,
               var10,
               var10
            );
         }
      }
   }

   private void N(fz var1, hg.k var2, double var3, int var5, int var6, double var7, double var9) {
      if (!(var3 < 0.35)) {
         double var11 = i(var3) * 48.0;
         byte var13 = 4;
         double var14 = 0.1691666666666667;
         double var16 = var14 * 0.62;

         for (int var18 = 0; var18 < 12; var18++) {
            int var19 = var18 * var13;
            if (var11 <= var19) {
               break;
            }

            int var20 = Math.min((var18 + 1) * var13, 48);
            double var21 = Math.min(var11, var20);
            double var23 = this.H[var19];
            double var25 = this.r(var21);
            double var27 = Math.max(var23, var25);
            double var29 = Math.min(var23, var25);

            for (int var31 = var19 + 1; var31 < var21; var31++) {
               var27 = Math.max(var27, this.H[var31]);
               var29 = Math.min(var29, this.H[var31]);
            }

            int var48 = var11 < var20 ? 1 : 0;
            double var32 = var48 != 0 ? 1.0 : n5.g((var11 - var20) / 2.0 + 0.5);
            var27 += this.i[var18] * var32;
            var29 -= this.Cr[var18] * var32;
            double var34 = -2.35 + var14 * (var18 + 0.5);
            int var36 = var25 >= var23 ? 1 : 0;
            int var37 = vv.Q(var36 != 0 ? var5 : var6, -10656141, var7 * 0.65);
            double var38 = var9 * (0.55 + 0.2 * (1.0 - var7));
            int var40 = vv.O(var37, var38 * 0.8);
            var1.ribbon(this.h(var2, var34, var29), this.h(var2, var34, var27), 0.018, var40, var40);
            double var41 = Math.min(var23, var25);
            double var43 = Math.max(Math.max(var23, var25), var41 + 0.02);
            int var45 = vv.O(var37, var38);
            this.I(var1, var2, var34 - var16 * 0.5, var41, var34 + var16 * 0.5, var43, var45, var45);
         }
      }
   }

   private double r(double var1) {
      int var3 = Math.min(47, (int)Math.floor(var1));
      return this.H[var3] + (this.H[var3 + 1] - this.H[var3]) * (var1 - var3);
   }

   private void U(fz var1, hg.k var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = i(var3) * 48.0;
         Vec3 var10 = this.h(var2, this.p[0], this.H[0]);

         for (int var11 = 0; var11 < 48 && var11 < var8; var11++) {
            double var12 = Math.min(1.0, var8 - var11);
            Vec3 var14 = this.h(var2, this.p[var11] + (this.p[var11 + 1] - this.p[var11]) * var12, this.H[var11] + (this.H[var11 + 1] - this.H[var11]) * var12);
            vq.b(var1, var10, var14, 0.055, var5, var5, var6);
            var10 = var14;
         }

         double[] var17 = this.u(var3);
         Vec3 var18 = this.h(var2, var17[0], var17[1]);
         double var13 = 0.5 + 0.5 * Math.sin(var3 * 9.0);
         double var15 = var3 < 2.6 ? 1.0 : 1.0 - n5.g((var3 - 2.6) / 0.2);
         var1.K(var18.x, var18.y, var18.z, 0.07 + var13 * 0.03, var3, vv.O(-1, var6 * var15));
         var1.K(var18.x, var18.y, var18.z, 0.16 + var13 * 0.05, -var3, vv.O(var5, var6 * 0.55 * var15));
      }
   }

   private void f(fz var1, hg.k var2, double var3, int var5, int var6, double var7) {
      if (!(var3 < 0.35)) {
         double var9;
         double var11;
         if (var3 < 2.6) {
            double[] var13 = this.u(var3);
            var9 = var13[1];
            var11 = var13[0];
         } else {
            var9 = Math.max(0.18, 3.3 * (1.0 - Math.min(1.0, G(var3))));
            var11 = -0.22;
         }

         int var20 = vv.O(var3 < 2.6 ? var5 : var6, var7 * 0.45 * n5.g((var3 - 0.35) / 0.3));
         double var14 = 0.11;
         double var16 = 0.07;

         for (double var18 = var11 + 0.1; var18 < 1.1; var18 += var14 + var16) {
            var1.ribbon(this.h(var2, var18, var9), this.h(var2, Math.min(1.1, var18 + var14), var9), 0.014, var20, var20);
         }
      }
   }

   private void z(fz var1, hg.k var2, double var3, int var5, double var6) {
      if (!(var3 < 2.6)) {
         double var8 = G(var3);
         Vec3 var10 = this.h(var2, -0.32, 3.3);
         Vec3 var11 = this.E(var2, var3);
         if (var8 <= 1.0) {
            vq.b(var1, var10, var11, 0.085, var5, var5, var6);
         } else {
            vq.b(var1, var10, this.e.r, 0.085, var5, var5, var6);
            vq.b(var1, this.e.r, var11, 0.085, var5, var5, var6);
         }

         double var12 = -0.15083333333333332;
         double var14 = 0.0609;
         double var16 = Math.max(0.12000000000000001, 3.3 * (1.0 - Math.min(1.0, var8)));
         this.I(var1, var2, var12 - var14, var16, var12 + var14, 3.3, vv.O(var5, var6 * 0.55), vv.O(var5, var6 * 0.8));
         if (var8 < 1.0) {
            var1.K(var11.x, var11.y, var11.z, 0.14, var3 * 3.0, vv.O(-1, var6));
         }
      }
   }

   private void t(fz var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0)) {
         Vec3 var9 = this.e.r.add(0.0, 0.03, 0.0);
         double var10 = n5.r(var7, 0.0, 0.8) * var5;
         vq.a(var1, var9, 0.15 + var7 * 3.4, 0.07, var4, var10);
         vq.a(var1, var9.add(0.0, 0.005, 0.0), 0.1 + var7 * 2.2, 0.02, -1, var10 * 0.6);
         double var12 = n5.g(var7 / 0.12) * (1.0 - n5.g((var2 - 5.6 + 0.4) / 0.8)) * var5;
         vq.a(var1, var9, 0.42 + 0.03 * Math.sin(var2 * 6.0), 0.05, var4, var12 * 0.5);
         double var14 = n5.g(var7 / 0.35);

         for (int var16 = 0; var16 < 7; var16++) {
            double var17 = this.f[var16];
            double var19 = this.Cc[var16] * var14;
            Vec3 var21 = var9;

            for (int var22 = 1; var22 <= 4; var22++) {
               double var23 = var22 / 4.0;
               double var25 = (this.e.w(16200 + var16 * 8 + var22) - 0.5) * 0.5 * var23;
               Vec3 var27 = var9.add(Math.cos(var17 + var25) * var19 * var23, 0.0, Math.sin(var17 + var25) * var19 * var23);
               vq.b(var1, var21, var27, 0.05 * (1.2 - var23), var4, var4, var12 * 0.85);
               var21 = var27;
            }
         }

         vq.E(var1, this.e, var7, var9.add(0.0, 0.1, 0.0), var4, -11566, 34, 2.1, var5);
      }
   }

   private void m(fz var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0) && !(var7 > 1.6)) {
         for (int var9 = 0; var9 < 18; var9++) {
            double var10 = this.Ci[var9];
            double var12 = this.r[var9];
            double var14 = this.l[var9] * var7 - 4.9 * var7 * var7;
            double var16 = var14 < 0.0 ? 0.0 : 1.0;
            double var18 = var10 * var7;
            double var20 = var5 * n5.g((1.6 - var7) / 0.4) * var16;
            if (!(var20 <= 0.002)) {
               Vec3 var22 = this.e.r.add(Math.cos(var12) * var18, Math.max(0.0, var14) + 0.04, Math.sin(var12) * var18);
               int var23 = vv.O(vv.Q(-14016476, var4, this.e.w(16300 + var9) * 0.35), var20);
               var1.c(var22.x, var22.y, var22.z, 0.06 + this.e.w(16400 + var9) * 0.05, var12 + var7 * 7.0, var7 * 9.0, var7 * 5.0, var23);
            }
         }
      }
   }

   private void M(fz var1, hg.k var2, double var3, double var5) {
      Vec3 var7 = this.e.r.add(0.0, -0.4, 0.0);

      for (int var8 = 0; var8 < 16; var8++) {
         double var9 = var3 - this.C[var8];
         if (!(var9 < 0.0)) {
            double[] var11 = this.u(this.C[var8]);
            Vec3 var12 = this.h(var2, var11[0], var11[1]).add(var2.normal().scale(0.2));
            double var13 = Math.min(var9, Math.max(0.0, 2.6 - this.C[var8]));
            Vec3 var15 = var12.add(var2.right().scale(this.W[var8] * 0.35 * var13)).add(0.0, 0.45 * var13, 0.0).add(var2.normal().scale(0.25 * var13));
            double var16 = var3 < 2.6 ? 0.0 : n5.g((var3 - 2.6 - var8 * 0.012) / 0.5);
            Vec3 var18 = var15.lerp(var7, var16 * var16);
            double var19 = var5 * n5.g(var9 / 0.2) * (1.0 - n5.g((var16 - 0.75) / 0.25));
            if (!(var19 <= 0.002)) {
               double var21 = this.K[var8] + var9 * 2.4 + var16 * 8.0;
               var1.x(
                  var18.x,
                  var18.y,
                  var18.z,
                  0.17 * (1.0 - var16 * 0.5),
                  var21,
                  0.4 * Math.sin(var9 * 3.0 + var8),
                  0.3 * Math.cos(var9 * 2.3 + var8),
                  Math.sin(var9 * 6.0 + var8) * 0.6,
                  vv.O(-1, var19)
               );
            }
         }
      }
   }

   private void U(fz var1, double var2, double var4) {
      double var6 = n5.g(var2 / 0.5);
      double var8 = 1.0 - n5.g((var2 - 2.6 - 0.25) / 0.3);
      double var10 = var4 * var6 * var8;
      if (!(var10 <= 0.002)) {
         Vec3 var12 = this.I();
         Vec3 var13 = this.u();
         double var14 = V(var2);
         double var16 = var2 < 2.6 ? 0.0 : 0.5 * Math.sin(Math.min(1.0, (var2 - 2.6) / 0.45) * Math.PI);
         double var18 = -0.775 + var14;
         double var20 = var18 + 1.55 * var6;
         Vec3 var22 = this.e.r.add(0.0, 0.012, 0.0);
         double[] var23 = new double[]{var18, var20};
         double[] var24 = new double[]{var16, var16 * 0.35};
         int var25 = vv.O(vv.Q(-6217428, -16777216, 0.2), var10);
         this.C(var1, var22, var12, var13, var23, var24, 0.525, vv.O(-1920436, var10));
         this.C(var1, var22.add(0.0, 0.002, 0.0), var12, var13, new double[]{var23[0] + 0.09, var23[1] - 0.09}, var24, 0.43500000000000005, var25);
         this.C(var1, var22.add(0.0, 0.004, 0.0), var12, var13, new double[]{var23[0] + 0.15, var23[1] - 0.15}, var24, 0.375, vv.O(-6217428, var10));
         double var26 = (var23[0] + var23[1]) * 0.5;
         double var28 = 0.22 * var6;
         double var30 = (var24[0] + var24[1]) * 0.5;
         Vec3 var32 = var22.add(0.0, 0.006 + var30, 0.0).add(var12.scale(var26));
         this.d(
            var1,
            var32.add(var12.scale(var28)),
            var32.add(var13.scale(var28 * 0.7)),
            var32.add(var12.scale(-var28)),
            var32.add(var13.scale(-var28 * 0.7)),
            vv.O(-1920436, var10)
         );
         int var33 = vv.O(-792892, var10 * 0.9);

         for (int var34 = 0; var34 < 2; var34++) {
            double var35 = var23[var34];
            double var37 = var34 == 0 ? -1.0 : 1.0;

            for (int var39 = 0; var39 < 9; var39++) {
               double var40 = (var39 / 8.0 - 0.5) * 0.93;
               Vec3 var42 = var22.add(0.0, var24[var34] + 0.008, 0.0).add(var12.scale(var35)).add(var13.scale(var40));
               var1.ribbon(var42, var42.add(var12.scale(var37 * 0.1)), 0.018, var33, vv.O(var33, 0.0));
            }
         }
      }
   }

   private void C(fz var1, Vec3 var2, Vec3 var3, Vec3 var4, double[] var5, double[] var6, double var7, int var9) {
      if (!(var5[1] - var5[0] <= 0.001)) {
         Vec3 var10 = var2.add(var3.scale(var5[0])).add(0.0, var6[0], 0.0);
         Vec3 var11 = var2.add(var3.scale(var5[1])).add(0.0, var6[1], 0.0);
         this.d(var1, var10.add(var4.scale(-var7)), var10.add(var4.scale(var7)), var11.add(var4.scale(var7)), var11.add(var4.scale(-var7)), var9);
      }
   }
}
