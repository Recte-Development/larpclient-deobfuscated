package com.larpclient;

import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class b3 {
   public static final double M = 0.4;
   public static final double a = 0.35;
   public static final double R = 2.3;
   public static final double C = 2.6;
   public static final double x = 2.82;
   public static final double H = 3.25;
   public static final double z = 2.7;
   public static final double m = 5.3;
   public static final double g = 5.6;
   public static final double q = 6.4;
   public static final int n = 1000;
   public static final int N9 = -67000;
   public static final double NZ = -2.7;
   public static final double T = 1.45;
   public static final double V = 0.1;
   public static final double O = 3.95;
   public static final double s = -2.35;
   public static final double F = -0.32;
   public static final double P = 0.6;
   public static final double S = 3.3;
   public static final double N = 1.12;
   public static final double j = 0.45;
   public static final double NB = 1.9;
   public static final double w = 0.18;
   public static final int NF = 48;
   public static final int Nw = 12;
   public static final int B = 16;
   public static final int c = 7;
   public static final int I = 18;
   public static final double E = 1.35;
   public static final double Y = 2.8;
   public static final double U = 1.05;
   public static final double v = 1.55;
   public static final String N0 = "YOU JUST GOT RUG PULLED";
   public static final String Nl = "LOSER.";
   public static final double A = 2.75;
   public static final double i = 2.24;
   public static final double d = 0.42;
   public static final double u = 0.32;
   public static final double b = 0.25;
   public static final double r = 0.93;
   public static final double Nr = 0.45;
   public static final double h = 0.7;
   public static final double k = 0.7;
   private static final int G = -3549466;
   private static final int o = -10656141;
   private static final int NT = -6217428;
   private static final int e = -1920436;
   private static final int W = -13242230;
   private static final int t = -50360;
   private final pj K;
   private final double[] J = new double[49];
   private final double[] Q = new double[49];
   private final double[] D = new double[12];
   private final double[] Z = new double[12];
   private final double[] NG = new double[16];
   private final double[] Nk = new double[16];
   private final double[] NX = new double[16];
   private final double[] X = new double[7];
   private final double[] f = new double[7];
   private final double[] y = new double[18];
   private final double[] l = new double[18];
   private final double[] NO = new double[18];
   private double p = Double.NaN;
   private double L = Double.NaN;

   b3(pj var1) {
      this.K = var1;
      double var2 = Math.exp(2.4) - 1.0;
      double[] var4 = new double[]{var1.F(15000) * Math.PI * 2.0, var1.F(15001) * Math.PI * 2.0, var1.F(15002) * Math.PI * 2.0};

      for (int var5 = 0; var5 <= 48; var5++) {
         double var6 = var5 / 48.0;
         double var8 = (Math.exp(2.4 * var6) - 1.0) / var2;
         double var10 = Math.sin(var6 * Math.PI * 5.0 + var4[0]) * 0.5
            + Math.sin(var6 * Math.PI * 9.0 + var4[1]) * 0.25
            + Math.sin(var6 * Math.PI * 15.0 + var4[2]) * 0.17;
         this.J[var5] = -2.35 + 2.0300000000000002 * var6;
         this.Q[var5] = 0.6 + 2.6999999999999997 * var8 + var10 * 0.16 * Math.sin(Math.PI * var6);
      }

      for (int var12 = 0; var12 < 12; var12++) {
         this.D[var12] = 0.03 + var1.F(15100 + var12) * 0.07;
         this.Z[var12] = 0.03 + var1.F(15200 + var12) * 0.07;
      }

      for (int var13 = 0; var13 < 16; var13++) {
         this.NG[var13] = 0.7 + 1.5499999999999998 * var13 / 15.0 + var1.F(15300 + var13) * 0.08;
         this.Nk[var13] = var1.F(15400 + var13) * 2.0 - 1.0;
         this.NX[var13] = var1.F(15500 + var13) * Math.PI * 2.0;
      }

      for (int var14 = 0; var14 < 7; var14++) {
         this.X[var14] = (var14 + var1.F(15600 + var14) * 0.6) * Math.PI * 2.0 / 7.0;
         this.f[var14] = 0.9 + var1.F(15700 + var14) * 0.8;
      }

      for (int var15 = 0; var15 < 18; var15++) {
         this.y[var15] = var1.F(15800 + var15) * Math.PI * 2.0;
         this.l[var15] = 0.9 + var1.F(15900 + var15) * 1.4;
         this.NO[var15] = 2.6 + var1.F(16000 + var15) * 2.4;
      }
   }

   public static double T(double var0) {
      return pj.Z((var0 - 0.35) / 1.9499999999999997);
   }

   public static double q(double var0) {
      return pj.Z(var0 / 0.4);
   }

   public static double D(double var0) {
      return pj.Z((6.4 - var0) / 0.8000000000000007);
   }

   public static double g(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      } else if (var0 < 2.82) {
         double var2 = (var0 - 2.6) / 0.21999999999999975;
         return var2 * var2;
      } else {
         return 1.0 + Math.min(1.0, (var0 - 2.82) / 0.18);
      }
   }

   public int I() {
      return 49;
   }

   public double E(int var1) {
      return this.J[var1];
   }

   public double V(int var1) {
      return this.Q[var1];
   }

   public double[] W(double var1) {
      double var3 = T(var1) * 48.0;
      int var5 = Math.min(47, (int)Math.floor(var3));
      double var6 = var3 - var5;
      return new double[]{this.J[var5] + (this.J[var5 + 1] - this.J[var5]) * var6, this.Q[var5] + (this.Q[var5 + 1] - this.Q[var5]) * var6};
   }

   public double P(double var1) {
      if (var1 < 2.6) {
         double var7 = this.W(var1)[1];
         return Math.round(1000.0 * Math.clamp((var7 - 0.6) / 2.6999999999999997, 0.0, 1.0));
      } else {
         double var3 = Math.clamp((var1 - 2.6) / 0.6499999999999999, 0.0, 1.0);
         double var5 = 1.0 - Math.pow(1.0 - var3, 3.0);
         return Math.round(1000.0 + -68000.0 * var5);
      }
   }

   public static String z(double var0) {
      long var2 = Math.round(var0);
      return (var2 < 0L ? "-$" : "$") + String.format(Locale.ROOT, "%,d", Math.abs(var2));
   }

   public uy.b i(Vec3 var1, double var2) {
      double var4 = var1.x - this.K.v.x;
      double var6 = var1.z - this.K.v.z;
      double var8 = var4 * var4 + var6 * var6 < 1.0E-6 ? (Double.isNaN(this.p) ? this.K.z : this.p) : Math.atan2(var4, var6);
      if (!Double.isNaN(this.p) && !Double.isNaN(this.L) && !(var2 < this.L)) {
         double var10 = Math.IEEEremainder(var8 - this.p, Math.PI * 2);
         this.p = this.p + var10 * (1.0 - Math.exp(-Math.min(0.25, var2 - this.L) * 9.0));
      } else {
         this.p = var8;
      }

      this.L = var2;
      return v(this.p);
   }

   public static uy.b v(double var0) {
      double var2 = Math.sin(var0);
      double var4 = Math.cos(var0);
      return new uy.b(new Vec3(var4, 0.0, -var2), new Vec3(0.0, 1.0, 0.0), new Vec3(var2, 0.0, var4));
   }

   public Vec3 Y(uy.b var1, double var2, double var4) {
      Vec3 var6 = this.K.v.subtract(var1.normal().scale(0.45));
      return var6.add(var1.right().scale(var2)).add(0.0, var4, 0.0);
   }

   public Vec3 O(uy.b var1, double var2) {
      double var4 = g(var2);
      Vec3 var6 = this.Y(var1, -0.32, 3.3);
      Vec3 var7 = this.K.v;
      Vec3 var8 = this.K.v.add(0.0, -1.9, 0.0);
      return var4 <= 1.0 ? var6.lerp(var7, var4) : var7.lerp(var8, var4 - 1.0);
   }

   private Vec3 u() {
      return new Vec3(-Math.sin(this.K.z), 0.0, Math.cos(this.K.z));
   }

   private Vec3 U() {
      return new Vec3(Math.cos(this.K.z), 0.0, Math.sin(this.K.z));
   }

   public static double e(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      }

      double var2 = var0 - 2.6;
      return 11.0 * var2 * var2 + 0.4 * var2;
   }

   public static pj.n k(pj var0, int var1, double var2) {
      double var4 = pj.R(var1, var0.l) / 16.0 * 0.9375;
      double var6 = (24.0 - pj.S[var1][1]) / 16.0 * 0.9375;
      double var8 = pj.Z((var2 - 0.35) / 0.5) * pj.Z((2.6 - var2) / 0.15);
      double var10 = 0.035 * Math.abs(Math.sin((var2 - 0.35) * Math.PI * 2.6)) * var8;
      double var12 = var2 < 2.6 ? 0.0 : Math.pow(Math.min(1.0, (var2 - 2.6) / 0.32), 2.0);
      double var14 = 1.35 * var12;
      double var16 = 0.26 * pj.Z((var2 - 0.35) / 0.5) + 0.7 * pj.Z((var2 - 2.6) / 0.25);
      double var18 = var1 == 2 ? -var16 : (var1 == 3 ? var16 : 0.0);
      double var20 = 0.35 * pj.Z((var2 - 2.6) / 0.3);
      double var22 = var2 < 2.82 ? 0.0 : Math.min(2.8, 6.5 * (var2 - 2.82) * (var2 - 2.82));
      double var24 = Math.cos(var0.z);
      double var26 = Math.sin(var0.z);
      double var28 = var6 * Math.sin(var14);
      Vec3 var30 = var0.v
         .add(var4 * var24 + var28 * var26 - var20 * var26, var6 * Math.cos(var14) + var10 - var22, var4 * var26 - var28 * var24 + var20 * var24);
      double var31 = 1.0 - pj.Z((var2 - 2.82 - 0.3) / 0.45);
      return new pj.n(var30, var14, 0.0, var18, 1.0, var31, 0.0, false);
   }

   public void Y(uy.b var1, double var2, double var4, ToIntFunction<String> var6, List<b0.k> var7) {
      double var8 = q(var2) * D(var2) * var4;
      if (!(var8 < 0.01)) {
         Quaternionf var10 = new Quaternionf().rotateY((float)Math.atan2(var1.normal().x, var1.normal().z));
         double var11 = pj.Z((var2 - 2.6) / 0.12);

         for (int var13 = 0; var13 <= 2; var13++) {
            double var14 = 0.6 + 2.6999999999999997 * var13 / 2.0;
            String var16 = var13 == 0 ? "$0" : (var13 == 1 ? "$500" : "$1K");
            r(var7, var6, var16, this.Y(var1, 1.2400000000000002, var14), var1, 0.26, uh.F(-6641994, var8 * 0.75), var10, true);
         }

         double[] var29 = this.W(Math.min(var2, 2.6));
         double var30 = var2 < 2.6 ? 0.0 : 0.045 * (1.0 - pj.Z((var2 - 3.25) / 0.6));
         double var31 = var30 * Math.sin(var2 * 71.0);
         double var18 = var30 * Math.sin(var2 * 53.0 + 1.3);
         double var20 = 1.0 + 0.35 * pj.T(var2, 2.6, 3.1500000000000004) + 0.06 * var11;
         double var22 = pj.Z((var2 - 0.35) / 0.2);
         Vec3 var24 = this.Y(var1, var29[0] + var31, var29[1] + 0.48 + var18);
         int var25 = uh.q(-13242230, -50360, var11);
         r(var7, var6, z(this.P(var2)), var24, var1, 0.62 * var20, uh.F(var25, var8 * var22), var10, false);
         if (var2 >= 2.6) {
            String var26 = "-" + String.format(Locale.ROOT, "%,d", Math.round((1000.0 - this.P(var2)) * 100.0 / 1000.0)) + "%";
            r(var7, var6, var26, this.Y(var1, var29[0] + var31, var29[1] + 0.02 + var18), var1, 0.3, uh.F(-50360, var8 * var11 * 0.9), var10, false);
         }

         if (var2 >= 2.7) {
            Vec3 var32 = this.K.v.add(var1.normal().scale(0.25));
            double var27 = Math.clamp((var2 - 5.3) / 0.7, 0.0, 1.0);
            a(var7, "YOU JUST GOT RUG PULLED", var32.add(0.0, 2.75, 0.0), 0.42, -50360, (var2 - 2.7) / 0.93, var27, var2, var4, var10);
            a(var7, "LOSER.", var32.add(0.0, 2.24, 0.0), 0.32, -854792, (var2 - 2.7 - 0.45) / 0.7, var27, var2, var4, var10);
         }
      }
   }

   private static void a(
      List<b0.k> var0, String var1, Vec3 var2, double var3, int var5, double var6, double var8, double var10, double var12, Quaternionf var14
   ) {
      if (!(var6 <= 0.0) && !(var8 >= 1.0) && !(var12 <= 0.004)) {
         b0.b var15 = new b0.b(3, Math.min(1.0, var6), var8, var10, 0);
         var0.add(new b0.k(var1, var2.x, var2.y, var2.z, var3 / 0.605, uh.F(var5, var12), var14.x, var14.y, var14.z, var14.w, true, var15));
      }
   }

   private static void r(List<b0.k> var0, ToIntFunction<String> var1, String var2, Vec3 var3, uy.b var4, double var5, int var7, Quaternionf var8, boolean var9) {
      if (var7 >>> 24 != 0) {
         double var10 = var5 / 0.605;
         Vec3 var12 = var3.add(var4.normal().scale(0.01));
         if (var9) {
            var12 = var12.add(var4.right().scale(var1.applyAsInt(var2) * var5 / 32.0 * 0.5));
         }

         var0.add(new b0.k(var2, var12.x, var12.y, var12.z, var10, var7, var8.x, var8.y, var8.z, var8.w, true));
      }
   }

   public static void d(kj var0, pj var1, double var2, int var4, int var5, double var6, boolean var8, uy.b var9) {
      b3 var10 = var1.s();
      if (var10 != null && var9 != null) {
         double var11 = q(var2);
         double var13 = D(var2);
         double var15 = var6 * var11 * var13;
         if (!(var15 <= 0.001)) {
            double var17 = pj.Z((var2 - 2.6) / 0.6);
            int var19 = uh.q(var4, -10656141, var17 * 0.7);
            if (!var8) {
               var0.blend(gl.c.K);
               var10.F(var0, var9, var15);
               var10.z(var0, var9, var2, var19, var15 * (1.0 - var17 * 0.6));
               var10.W(var0, var9, var2, var4, var5, var17, var15);
               var10.h(var0, var2, var6 * var13);
               var10.V(var0, var2, var5, var6 * var13);
               var10.w(var0, var9, var2, var6 * var13);
            }

            var0.blend(gl.c.E);
            var10.f(var0, var9, var2, var19, var15 * (var8 ? 0.8 : 1.0));
            var10.V(var0, var9, var2, var19, var5, var15);
            var10.w(var0, var9, var2, var5, var15);
            var10.s(var0, var2, var5, var6 * var13);
         }
      }
   }

   private void u(kj var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6, int var7, int var8, int var9) {
      var1.vertex(var2.x, var2.y, var2.z, var6);
      var1.vertex(var3.x, var3.y, var3.z, var7);
      var1.vertex(var4.x, var4.y, var4.z, var8);
      var1.vertex(var5.x, var5.y, var5.z, var9);
   }

   private void J(kj var1, uy.b var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      this.u(var1, this.Y(var2, var3, var5), this.Y(var2, var7, var5), this.Y(var2, var7, var9), this.Y(var2, var3, var9), var11, var11, var12, var12);
   }

   private void M(kj var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6) {
      Vec3 var7 = var3.subtract(var2).cross(var4.subtract(var2));
      if (var7.y >= 0.0) {
         this.u(var1, var2, var3, var4, var5, var6, var6, var6, var6);
      } else {
         this.u(var1, var5, var4, var3, var2, var6, var6, var6, var6);
      }
   }

   private void F(kj var1, uy.b var2, double var3) {
      double var5 = pj.Z((var3 - 0.3) / 0.7);
      int var7 = uh.F(-3549466, var3 * 0.075 * var5);

      for (int var8 = 0; var8 <= 4; var8++) {
         double var9 = 0.6 + 2.6999999999999997 * var8 / 4.0;
         var1.ribbon(this.Y(var2, -2.58, var9), this.Y(var2, 1.08, var9), 0.012, var7, var7);
      }

      for (int var11 = 0; var11 <= 5; var11++) {
         double var13 = -2.35 + 3.47 * var11 / 5.0;
         var1.ribbon(this.Y(var2, var13, 0.2), this.Y(var2, var13, 3.5), 0.012, var7, var7);
      }

      int var12 = uh.F(-3549466, var3 * 0.22 * var5);
      var1.ribbon(this.Y(var2, 1.12, 0.2), this.Y(var2, 1.12, 3.5), 0.016, var12, var12);
   }

   private void z(kj var1, uy.b var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = T(var3) * 48.0;
         int var10 = uh.F(var5, var6 * 0.3);
         int var11 = uh.F(var5, 0.0);
         double var12 = 0.16;

         for (int var14 = 0; var14 < 48 && var14 < var8; var14++) {
            double var15 = Math.min(1.0, var8 - var14);
            double var17 = this.J[var14] + (this.J[var14 + 1] - this.J[var14]) * var15;
            double var19 = this.Q[var14] + (this.Q[var14 + 1] - this.Q[var14]) * var15;
            this.u(
               var1,
               this.Y(var2, this.J[var14], var12),
               this.Y(var2, var17, var12),
               this.Y(var2, var17, var19),
               this.Y(var2, this.J[var14], this.Q[var14]),
               var11,
               var11,
               var10,
               var10
            );
         }
      }
   }

   private void W(kj var1, uy.b var2, double var3, int var5, int var6, double var7, double var9) {
      if (!(var3 < 0.35)) {
         double var11 = T(var3) * 48.0;
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
            double var23 = this.Q[var19];
            double var25 = this.W(var21);
            double var27 = Math.max(var23, var25);
            double var29 = Math.min(var23, var25);

            for (int var31 = var19 + 1; var31 < var21; var31++) {
               var27 = Math.max(var27, this.Q[var31]);
               var29 = Math.min(var29, this.Q[var31]);
            }

            int var48 = var11 < var20 ? 1 : 0;
            double var32 = var48 != 0 ? 1.0 : pj.Z((var11 - var20) / 2.0 + 0.5);
            var27 += this.D[var18] * var32;
            var29 -= this.Z[var18] * var32;
            double var34 = -2.35 + var14 * (var18 + 0.5);
            int var36 = var25 >= var23 ? 1 : 0;
            int var37 = uh.q(var36 != 0 ? var5 : var6, -10656141, var7 * 0.65);
            double var38 = var9 * (0.55 + 0.2 * (1.0 - var7));
            int var40 = uh.F(var37, var38 * 0.8);
            var1.ribbon(this.Y(var2, var34, var29), this.Y(var2, var34, var27), 0.018, var40, var40);
            double var41 = Math.min(var23, var25);
            double var43 = Math.max(Math.max(var23, var25), var41 + 0.02);
            int var45 = uh.F(var37, var38);
            this.J(var1, var2, var34 - var16 * 0.5, var41, var34 + var16 * 0.5, var43, var45, var45);
         }
      }
   }

   private double W(double var1) {
      int var3 = Math.min(47, (int)Math.floor(var1));
      return this.Q[var3] + (this.Q[var3 + 1] - this.Q[var3]) * (var1 - var3);
   }

   private void f(kj var1, uy.b var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = T(var3) * 48.0;
         Vec3 var10 = this.Y(var2, this.J[0], this.Q[0]);

         for (int var11 = 0; var11 < 48 && var11 < var8; var11++) {
            double var12 = Math.min(1.0, var8 - var11);
            Vec3 var14 = this.Y(var2, this.J[var11] + (this.J[var11 + 1] - this.J[var11]) * var12, this.Q[var11] + (this.Q[var11 + 1] - this.Q[var11]) * var12);
            sh.r(var1, var10, var14, 0.055, var5, var5, var6);
            var10 = var14;
         }

         double[] var17 = this.W(var3);
         Vec3 var18 = this.Y(var2, var17[0], var17[1]);
         double var13 = 0.5 + 0.5 * Math.sin(var3 * 9.0);
         double var15 = var3 < 2.6 ? 1.0 : 1.0 - pj.Z((var3 - 2.6) / 0.2);
         var1.P(var18.x, var18.y, var18.z, 0.07 + var13 * 0.03, var3, uh.F(-1, var6 * var15));
         var1.P(var18.x, var18.y, var18.z, 0.16 + var13 * 0.05, -var3, uh.F(var5, var6 * 0.55 * var15));
      }
   }

   private void V(kj var1, uy.b var2, double var3, int var5, int var6, double var7) {
      if (!(var3 < 0.35)) {
         double var9;
         double var11;
         if (var3 < 2.6) {
            double[] var13 = this.W(var3);
            var9 = var13[1];
            var11 = var13[0];
         } else {
            var9 = Math.max(0.18, 3.3 * (1.0 - Math.min(1.0, g(var3))));
            var11 = -0.22;
         }

         int var20 = uh.F(var3 < 2.6 ? var5 : var6, var7 * 0.45 * pj.Z((var3 - 0.35) / 0.3));
         double var14 = 0.11;
         double var16 = 0.07;

         for (double var18 = var11 + 0.1; var18 < 1.1; var18 += var14 + var16) {
            var1.ribbon(this.Y(var2, var18, var9), this.Y(var2, Math.min(1.1, var18 + var14), var9), 0.014, var20, var20);
         }
      }
   }

   private void w(kj var1, uy.b var2, double var3, int var5, double var6) {
      if (!(var3 < 2.6)) {
         double var8 = g(var3);
         Vec3 var10 = this.Y(var2, -0.32, 3.3);
         Vec3 var11 = this.O(var2, var3);
         if (var8 <= 1.0) {
            sh.r(var1, var10, var11, 0.085, var5, var5, var6);
         } else {
            sh.r(var1, var10, this.K.v, 0.085, var5, var5, var6);
            sh.r(var1, this.K.v, var11, 0.085, var5, var5, var6);
         }

         double var12 = -0.15083333333333332;
         double var14 = 0.0609;
         double var16 = Math.max(0.12000000000000001, 3.3 * (1.0 - Math.min(1.0, var8)));
         this.J(var1, var2, var12 - var14, var16, var12 + var14, 3.3, uh.F(var5, var6 * 0.55), uh.F(var5, var6 * 0.8));
         if (var8 < 1.0) {
            var1.P(var11.x, var11.y, var11.z, 0.14, var3 * 3.0, uh.F(-1, var6));
         }
      }
   }

   private void s(kj var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0)) {
         Vec3 var9 = this.K.v.add(0.0, 0.03, 0.0);
         double var10 = pj.T(var7, 0.0, 0.8) * var5;
         sh.q(var1, var9, 0.15 + var7 * 3.4, 0.07, var4, var10);
         sh.q(var1, var9.add(0.0, 0.005, 0.0), 0.1 + var7 * 2.2, 0.02, -1, var10 * 0.6);
         double var12 = pj.Z(var7 / 0.12) * (1.0 - pj.Z((var2 - 5.6 + 0.4) / 0.8)) * var5;
         sh.q(var1, var9, 0.42 + 0.03 * Math.sin(var2 * 6.0), 0.05, var4, var12 * 0.5);
         double var14 = pj.Z(var7 / 0.35);

         for (int var16 = 0; var16 < 7; var16++) {
            double var17 = this.X[var16];
            double var19 = this.f[var16] * var14;
            Vec3 var21 = var9;

            for (int var22 = 1; var22 <= 4; var22++) {
               double var23 = var22 / 4.0;
               double var25 = (this.K.F(16200 + var16 * 8 + var22) - 0.5) * 0.5 * var23;
               Vec3 var27 = var9.add(Math.cos(var17 + var25) * var19 * var23, 0.0, Math.sin(var17 + var25) * var19 * var23);
               sh.r(var1, var21, var27, 0.05 * (1.2 - var23), var4, var4, var12 * 0.85);
               var21 = var27;
            }
         }

         sh.C(var1, this.K, var7, var9.add(0.0, 0.1, 0.0), var4, -11566, 34, 2.1, var5);
      }
   }

   private void V(kj var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0) && !(var7 > 1.6)) {
         for (int var9 = 0; var9 < 18; var9++) {
            double var10 = this.l[var9];
            double var12 = this.y[var9];
            double var14 = this.NO[var9] * var7 - 4.9 * var7 * var7;
            double var16 = var14 < 0.0 ? 0.0 : 1.0;
            double var18 = var10 * var7;
            double var20 = var5 * pj.Z((1.6 - var7) / 0.4) * var16;
            if (!(var20 <= 0.002)) {
               Vec3 var22 = this.K.v.add(Math.cos(var12) * var18, Math.max(0.0, var14) + 0.04, Math.sin(var12) * var18);
               int var23 = uh.F(uh.q(-14016476, var4, this.K.F(16300 + var9) * 0.35), var20);
               var1.b(var22.x, var22.y, var22.z, 0.06 + this.K.F(16400 + var9) * 0.05, var12 + var7 * 7.0, var7 * 9.0, var7 * 5.0, var23);
            }
         }
      }
   }

   private void w(kj var1, uy.b var2, double var3, double var5) {
      Vec3 var7 = this.K.v.add(0.0, -0.4, 0.0);

      for (int var8 = 0; var8 < 16; var8++) {
         double var9 = var3 - this.NG[var8];
         if (!(var9 < 0.0)) {
            double[] var11 = this.W(this.NG[var8]);
            Vec3 var12 = this.Y(var2, var11[0], var11[1]).add(var2.normal().scale(0.2));
            double var13 = Math.min(var9, Math.max(0.0, 2.6 - this.NG[var8]));
            Vec3 var15 = var12.add(var2.right().scale(this.Nk[var8] * 0.35 * var13)).add(0.0, 0.45 * var13, 0.0).add(var2.normal().scale(0.25 * var13));
            double var16 = var3 < 2.6 ? 0.0 : pj.Z((var3 - 2.6 - var8 * 0.012) / 0.5);
            Vec3 var18 = var15.lerp(var7, var16 * var16);
            double var19 = var5 * pj.Z(var9 / 0.2) * (1.0 - pj.Z((var16 - 0.75) / 0.25));
            if (!(var19 <= 0.002)) {
               double var21 = this.NX[var8] + var9 * 2.4 + var16 * 8.0;
               var1.r(
                  var18.x,
                  var18.y,
                  var18.z,
                  0.17 * (1.0 - var16 * 0.5),
                  var21,
                  0.4 * Math.sin(var9 * 3.0 + var8),
                  0.3 * Math.cos(var9 * 2.3 + var8),
                  Math.sin(var9 * 6.0 + var8) * 0.6,
                  uh.F(-1, var19)
               );
            }
         }
      }
   }

   private void h(kj var1, double var2, double var4) {
      double var6 = pj.Z(var2 / 0.5);
      double var8 = 1.0 - pj.Z((var2 - 2.6 - 0.25) / 0.3);
      double var10 = var4 * var6 * var8;
      if (!(var10 <= 0.002)) {
         Vec3 var12 = this.u();
         Vec3 var13 = this.U();
         double var14 = e(var2);
         double var16 = var2 < 2.6 ? 0.0 : 0.5 * Math.sin(Math.min(1.0, (var2 - 2.6) / 0.45) * Math.PI);
         double var18 = -0.775 + var14;
         double var20 = var18 + 1.55 * var6;
         Vec3 var22 = this.K.v.add(0.0, 0.012, 0.0);
         double[] var23 = new double[]{var18, var20};
         double[] var24 = new double[]{var16, var16 * 0.35};
         int var25 = uh.F(uh.q(-6217428, -16777216, 0.2), var10);
         this.I(var1, var22, var12, var13, var23, var24, 0.525, uh.F(-1920436, var10));
         this.I(var1, var22.add(0.0, 0.002, 0.0), var12, var13, new double[]{var23[0] + 0.09, var23[1] - 0.09}, var24, 0.43500000000000005, var25);
         this.I(var1, var22.add(0.0, 0.004, 0.0), var12, var13, new double[]{var23[0] + 0.15, var23[1] - 0.15}, var24, 0.375, uh.F(-6217428, var10));
         double var26 = (var23[0] + var23[1]) * 0.5;
         double var28 = 0.22 * var6;
         double var30 = (var24[0] + var24[1]) * 0.5;
         Vec3 var32 = var22.add(0.0, 0.006 + var30, 0.0).add(var12.scale(var26));
         this.M(
            var1,
            var32.add(var12.scale(var28)),
            var32.add(var13.scale(var28 * 0.7)),
            var32.add(var12.scale(-var28)),
            var32.add(var13.scale(-var28 * 0.7)),
            uh.F(-1920436, var10)
         );
         int var33 = uh.F(-792892, var10 * 0.9);

         for (int var34 = 0; var34 < 2; var34++) {
            double var35 = var23[var34];
            double var37 = var34 == 0 ? -1.0 : 1.0;

            for (int var39 = 0; var39 < 9; var39++) {
               double var40 = (var39 / 8.0 - 0.5) * 0.93;
               Vec3 var42 = var22.add(0.0, var24[var34] + 0.008, 0.0).add(var12.scale(var35)).add(var13.scale(var40));
               var1.ribbon(var42, var42.add(var12.scale(var37 * 0.1)), 0.018, var33, uh.F(var33, 0.0));
            }
         }
      }
   }

   private void I(kj var1, Vec3 var2, Vec3 var3, Vec3 var4, double[] var5, double[] var6, double var7, int var9) {
      if (!(var5[1] - var5[0] <= 0.001)) {
         Vec3 var10 = var2.add(var3.scale(var5[0])).add(0.0, var6[0], 0.0);
         Vec3 var11 = var2.add(var3.scale(var5[1])).add(0.0, var6[1], 0.0);
         this.M(var1, var10.add(var4.scale(-var7)), var10.add(var4.scale(var7)), var11.add(var4.scale(var7)), var11.add(var4.scale(-var7)), var9);
      }
   }
}
