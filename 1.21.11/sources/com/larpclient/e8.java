package com.larpclient;

import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;
import net.minecraft.class_243;
import org.joml.Quaternionf;

public final class e8 {
   public static final double n = 0.4;
   public static final double o = 0.35;
   public static final double b = 2.3;
   public static final double w = 2.6;
   public static final double y = 2.82;
   public static final double M = 3.25;
   public static final double r = 2.7;
   public static final double fR = 5.3;
   public static final double c = 5.6;
   public static final double fZ = 6.4;
   public static final int R = 1000;
   public static final int W = -67000;
   public static final double fk = -2.7;
   public static final double t = 1.45;
   public static final double u = 0.1;
   public static final double E = 3.95;
   public static final double X = -2.35;
   public static final double e = -0.32;
   public static final double P = 0.6;
   public static final double D = 3.3;
   public static final double O = 1.12;
   public static final double S = 0.45;
   public static final double B = 1.9;
   public static final double F = 0.18;
   public static final int Q = 48;
   public static final int h = 12;
   public static final int fJ = 16;
   public static final int fm = 7;
   public static final int x = 18;
   public static final double fA = 1.35;
   public static final double f1 = 2.8;
   public static final double d = 1.05;
   public static final double v = 1.55;
   public static final String s = "YOU JUST GOT RUG PULLED";
   public static final String fF = "LOSER.";
   public static final double j = 2.75;
   public static final double fH = 2.24;
   public static final double I = 0.42;
   public static final double p = 0.32;
   public static final double a = 0.25;
   public static final double K = 0.93;
   public static final double L = 0.45;
   public static final double G = 0.7;
   public static final double g = 0.7;
   private static final int fT = -3549466;
   private static final int q = -10656141;
   private static final int l = -6217428;
   private static final int Z = -1920436;
   private static final int fd = -13242230;
   private static final int J = -50360;
   private final u8 f;
   private final double[] i = new double[49];
   private final double[] fu = new double[49];
   private final double[] C = new double[12];
   private final double[] k = new double[12];
   private final double[] V = new double[16];
   private final double[] m = new double[16];
   private final double[] A = new double[16];
   private final double[] z = new double[7];
   private final double[] N = new double[7];
   private final double[] Y = new double[18];
   private final double[] fs = new double[18];
   private final double[] T = new double[18];
   private double U = Double.NaN;
   private double H = Double.NaN;

   e8(u8 var1) {
      this.f = var1;
      double var2 = Math.exp(2.4) - 1.0;
      double[] var4 = new double[]{var1.P(15000) * Math.PI * 2.0, var1.P(15001) * Math.PI * 2.0, var1.P(15002) * Math.PI * 2.0};

      for (int var5 = 0; var5 <= 48; var5++) {
         double var6 = var5 / 48.0;
         double var8 = (Math.exp(2.4 * var6) - 1.0) / var2;
         double var10 = Math.sin(var6 * Math.PI * 5.0 + var4[0]) * 0.5
            + Math.sin(var6 * Math.PI * 9.0 + var4[1]) * 0.25
            + Math.sin(var6 * Math.PI * 15.0 + var4[2]) * 0.17;
         this.i[var5] = -2.35 + 2.0300000000000002 * var6;
         this.fu[var5] = 0.6 + 2.6999999999999997 * var8 + var10 * 0.16 * Math.sin(Math.PI * var6);
      }

      for (int var12 = 0; var12 < 12; var12++) {
         this.C[var12] = 0.03 + var1.P(15100 + var12) * 0.07;
         this.k[var12] = 0.03 + var1.P(15200 + var12) * 0.07;
      }

      for (int var13 = 0; var13 < 16; var13++) {
         this.V[var13] = 0.7 + 1.5499999999999998 * var13 / 15.0 + var1.P(15300 + var13) * 0.08;
         this.m[var13] = var1.P(15400 + var13) * 2.0 - 1.0;
         this.A[var13] = var1.P(15500 + var13) * Math.PI * 2.0;
      }

      for (int var14 = 0; var14 < 7; var14++) {
         this.z[var14] = (var14 + var1.P(15600 + var14) * 0.6) * Math.PI * 2.0 / 7.0;
         this.N[var14] = 0.9 + var1.P(15700 + var14) * 0.8;
      }

      for (int var15 = 0; var15 < 18; var15++) {
         this.Y[var15] = var1.P(15800 + var15) * Math.PI * 2.0;
         this.fs[var15] = 0.9 + var1.P(15900 + var15) * 1.4;
         this.T[var15] = 2.6 + var1.P(16000 + var15) * 2.4;
      }
   }

   public static double Y(double var0) {
      return u8.S((var0 - 0.35) / 1.9499999999999997);
   }

   public static double A(double var0) {
      return u8.S(var0 / 0.4);
   }

   public static double m(double var0) {
      return u8.S((6.4 - var0) / 0.8000000000000007);
   }

   public static double d(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      } else if (var0 < 2.82) {
         double var2 = (var0 - 2.6) / 0.21999999999999975;
         return var2 * var2;
      } else {
         return 1.0 + Math.min(1.0, (var0 - 2.82) / 0.18);
      }
   }

   public int v() {
      return 49;
   }

   public double E(int var1) {
      return this.i[var1];
   }

   public double K(int var1) {
      return this.fu[var1];
   }

   public double[] q(double var1) {
      double var3 = Y(var1) * 48.0;
      int var5 = Math.min(47, (int)Math.floor(var3));
      double var6 = var3 - var5;
      return new double[]{this.i[var5] + (this.i[var5 + 1] - this.i[var5]) * var6, this.fu[var5] + (this.fu[var5 + 1] - this.fu[var5]) * var6};
   }

   public double o(double var1) {
      if (var1 < 2.6) {
         double var7 = this.q(var1)[1];
         return Math.round(1000.0 * Math.clamp((var7 - 0.6) / 2.6999999999999997, 0.0, 1.0));
      } else {
         double var3 = Math.clamp((var1 - 2.6) / 0.6499999999999999, 0.0, 1.0);
         double var5 = 1.0 - Math.pow(1.0 - var3, 3.0);
         return Math.round(1000.0 + -68000.0 * var5);
      }
   }

   public static String S(double var0) {
      long var2 = Math.round(var0);
      return (var2 < 0L ? "-$" : "$") + String.format(Locale.ROOT, "%,d", Math.abs(var2));
   }

   public yt.l a(class_243 var1, double var2) {
      double var4 = var1.field_1352 - this.f.z.field_1352;
      double var6 = var1.field_1350 - this.f.z.field_1350;
      double var8 = var4 * var4 + var6 * var6 < 1.0E-6 ? (Double.isNaN(this.U) ? this.f.y : this.U) : Math.atan2(var4, var6);
      if (!Double.isNaN(this.U) && !Double.isNaN(this.H) && !(var2 < this.H)) {
         double var10 = Math.IEEEremainder(var8 - this.U, Math.PI * 2);
         this.U = this.U + var10 * (1.0 - Math.exp(-Math.min(0.25, var2 - this.H) * 9.0));
      } else {
         this.U = var8;
      }

      this.H = var2;
      return k(this.U);
   }

   public static yt.l k(double var0) {
      double var2 = Math.sin(var0);
      double var4 = Math.cos(var0);
      return new yt.l(new class_243(var4, 0.0, -var2), new class_243(0.0, 1.0, 0.0), new class_243(var2, 0.0, var4));
   }

   public class_243 i(yt.l var1, double var2, double var4) {
      class_243 var6 = this.f.z.method_1020(var1.normal().method_1021(0.45));
      return var6.method_1019(var1.right().method_1021(var2)).method_1031(0.0, var4, 0.0);
   }

   public class_243 P(yt.l var1, double var2) {
      double var4 = d(var2);
      class_243 var6 = this.i(var1, -0.32, 3.3);
      class_243 var7 = this.f.z;
      class_243 var8 = this.f.z.method_1031(0.0, -1.9, 0.0);
      return var4 <= 1.0 ? var6.method_35590(var7, var4) : var7.method_35590(var8, var4 - 1.0);
   }

   private class_243 w() {
      return new class_243(-Math.sin(this.f.y), 0.0, Math.cos(this.f.y));
   }

   private class_243 W() {
      return new class_243(Math.cos(this.f.y), 0.0, Math.sin(this.f.y));
   }

   public static double x(double var0) {
      if (var0 < 2.6) {
         return 0.0;
      }

      double var2 = var0 - 2.6;
      return 11.0 * var2 * var2 + 0.4 * var2;
   }

   public static u8.s f(u8 var0, int var1, double var2) {
      double var4 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var6 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      double var8 = u8.S((var2 - 0.35) / 0.5) * u8.S((2.6 - var2) / 0.15);
      double var10 = 0.035 * Math.abs(Math.sin((var2 - 0.35) * Math.PI * 2.6)) * var8;
      double var12 = var2 < 2.6 ? 0.0 : Math.pow(Math.min(1.0, (var2 - 2.6) / 0.32), 2.0);
      double var14 = 1.35 * var12;
      double var16 = 0.26 * u8.S((var2 - 0.35) / 0.5) + 0.7 * u8.S((var2 - 2.6) / 0.25);
      double var18 = var1 == 2 ? -var16 : (var1 == 3 ? var16 : 0.0);
      double var20 = 0.35 * u8.S((var2 - 2.6) / 0.3);
      double var22 = var2 < 2.82 ? 0.0 : Math.min(2.8, 6.5 * (var2 - 2.82) * (var2 - 2.82));
      double var24 = Math.cos(var0.y);
      double var26 = Math.sin(var0.y);
      double var28 = var6 * Math.sin(var14);
      class_243 var30 = var0.z
         .method_1031(var4 * var24 + var28 * var26 - var20 * var26, var6 * Math.cos(var14) + var10 - var22, var4 * var26 - var28 * var24 + var20 * var24);
      double var31 = 1.0 - u8.S((var2 - 2.82 - 0.3) / 0.45);
      return new u8.s(var30, var14, 0.0, var18, 1.0, var31, 0.0, false);
   }

   public void h(yt.l var1, double var2, double var4, ToIntFunction<String> var6, List<im.u> var7) {
      double var8 = A(var2) * m(var2) * var4;
      if (!(var8 < 0.01)) {
         Quaternionf var10 = new Quaternionf().rotateY((float)Math.atan2(var1.normal().field_1352, var1.normal().field_1350));
         double var11 = u8.S((var2 - 2.6) / 0.12);

         for (int var13 = 0; var13 <= 2; var13++) {
            double var14 = 0.6 + 2.6999999999999997 * var13 / 2.0;
            String var16 = var13 == 0 ? "$0" : (var13 == 1 ? "$500" : "$1K");
            k(var7, var6, var16, this.i(var1, 1.2400000000000002, var14), var1, 0.26, ei.R(-6641994, var8 * 0.75), var10, true);
         }

         double[] var29 = this.q(Math.min(var2, 2.6));
         double var30 = var2 < 2.6 ? 0.0 : 0.045 * (1.0 - u8.S((var2 - 3.25) / 0.6));
         double var31 = var30 * Math.sin(var2 * 71.0);
         double var18 = var30 * Math.sin(var2 * 53.0 + 1.3);
         double var20 = 1.0 + 0.35 * u8.o(var2, 2.6, 3.1500000000000004) + 0.06 * var11;
         double var22 = u8.S((var2 - 0.35) / 0.2);
         class_243 var24 = this.i(var1, var29[0] + var31, var29[1] + 0.48 + var18);
         int var25 = ei.W(-13242230, -50360, var11);
         k(var7, var6, S(this.o(var2)), var24, var1, 0.62 * var20, ei.R(var25, var8 * var22), var10, false);
         if (var2 >= 2.6) {
            String var26 = "-" + String.format(Locale.ROOT, "%,d", Math.round((1000.0 - this.o(var2)) * 100.0 / 1000.0)) + "%";
            k(var7, var6, var26, this.i(var1, var29[0] + var31, var29[1] + 0.02 + var18), var1, 0.3, ei.R(-50360, var8 * var11 * 0.9), var10, false);
         }

         if (var2 >= 2.7) {
            class_243 var32 = this.f.z.method_1019(var1.normal().method_1021(0.25));
            double var27 = Math.clamp((var2 - 5.3) / 0.7, 0.0, 1.0);
            B(var7, "YOU JUST GOT RUG PULLED", var32.method_1031(0.0, 2.75, 0.0), 0.42, -50360, (var2 - 2.7) / 0.93, var27, var2, var4, var10);
            B(var7, "LOSER.", var32.method_1031(0.0, 2.24, 0.0), 0.32, -854792, (var2 - 2.7 - 0.45) / 0.7, var27, var2, var4, var10);
         }
      }
   }

   private static void B(
      List<im.u> var0, String var1, class_243 var2, double var3, int var5, double var6, double var8, double var10, double var12, Quaternionf var14
   ) {
      if (!(var6 <= 0.0) && !(var8 >= 1.0) && !(var12 <= 0.004)) {
         im.p var15 = new im.p(3, Math.min(1.0, var6), var8, var10, 0);
         var0.add(
            new im.u(var1, var2.field_1352, var2.field_1351, var2.field_1350, var3 / 0.605, ei.R(var5, var12), var14.x, var14.y, var14.z, var14.w, true, var15)
         );
      }
   }

   private static void k(
      List<im.u> var0, ToIntFunction<String> var1, String var2, class_243 var3, yt.l var4, double var5, int var7, Quaternionf var8, boolean var9
   ) {
      if (var7 >>> 24 != 0) {
         double var10 = var5 / 0.605;
         class_243 var12 = var3.method_1019(var4.normal().method_1021(0.01));
         if (var9) {
            var12 = var12.method_1019(var4.right().method_1021(var1.applyAsInt(var2) * var5 / 32.0 * 0.5));
         }

         var0.add(new im.u(var2, var12.field_1352, var12.field_1351, var12.field_1350, var10, var7, var8.x, var8.y, var8.z, var8.w, true));
      }
   }

   public static void b(f1 var0, u8 var1, double var2, int var4, int var5, double var6, boolean var8, yt.l var9) {
      e8 var10 = var1.E();
      if (var10 != null && var9 != null) {
         double var11 = A(var2);
         double var13 = m(var2);
         double var15 = var6 * var11 * var13;
         if (!(var15 <= 0.001)) {
            double var17 = u8.S((var2 - 2.6) / 0.6);
            int var19 = ei.W(var4, -10656141, var17 * 0.7);
            if (!var8) {
               var0.blend(ti.d.G);
               var10.N(var0, var9, var15);
               var10.B(var0, var9, var2, var19, var15 * (1.0 - var17 * 0.6));
               var10.j(var0, var9, var2, var4, var5, var17, var15);
               var10.Y(var0, var2, var6 * var13);
               var10.c(var0, var2, var5, var6 * var13);
               var10.n(var0, var9, var2, var6 * var13);
            }

            var0.blend(ti.d.l);
            var10.V(var0, var9, var2, var19, var15 * (var8 ? 0.8 : 1.0));
            var10.k(var0, var9, var2, var19, var5, var15);
            var10.T(var0, var9, var2, var5, var15);
            var10.R(var0, var2, var5, var6 * var13);
         }
      }
   }

   private void J(f1 var1, class_243 var2, class_243 var3, class_243 var4, class_243 var5, int var6, int var7, int var8, int var9) {
      var1.vertex(var2.field_1352, var2.field_1351, var2.field_1350, var6);
      var1.vertex(var3.field_1352, var3.field_1351, var3.field_1350, var7);
      var1.vertex(var4.field_1352, var4.field_1351, var4.field_1350, var8);
      var1.vertex(var5.field_1352, var5.field_1351, var5.field_1350, var9);
   }

   private void g(f1 var1, yt.l var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      this.J(var1, this.i(var2, var3, var5), this.i(var2, var7, var5), this.i(var2, var7, var9), this.i(var2, var3, var9), var11, var11, var12, var12);
   }

   private void o(f1 var1, class_243 var2, class_243 var3, class_243 var4, class_243 var5, int var6) {
      class_243 var7 = var3.method_1020(var2).method_1036(var4.method_1020(var2));
      if (var7.field_1351 >= 0.0) {
         this.J(var1, var2, var3, var4, var5, var6, var6, var6, var6);
      } else {
         this.J(var1, var5, var4, var3, var2, var6, var6, var6, var6);
      }
   }

   private void N(f1 var1, yt.l var2, double var3) {
      double var5 = u8.S((var3 - 0.3) / 0.7);
      int var7 = ei.R(-3549466, var3 * 0.075 * var5);

      for (int var8 = 0; var8 <= 4; var8++) {
         double var9 = 0.6 + 2.6999999999999997 * var8 / 4.0;
         var1.ribbon(this.i(var2, -2.58, var9), this.i(var2, 1.08, var9), 0.012, var7, var7);
      }

      for (int var11 = 0; var11 <= 5; var11++) {
         double var13 = -2.35 + 3.47 * var11 / 5.0;
         var1.ribbon(this.i(var2, var13, 0.2), this.i(var2, var13, 3.5), 0.012, var7, var7);
      }

      int var12 = ei.R(-3549466, var3 * 0.22 * var5);
      var1.ribbon(this.i(var2, 1.12, 0.2), this.i(var2, 1.12, 3.5), 0.016, var12, var12);
   }

   private void B(f1 var1, yt.l var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = Y(var3) * 48.0;
         int var10 = ei.R(var5, var6 * 0.3);
         int var11 = ei.R(var5, 0.0);
         double var12 = 0.16;

         for (int var14 = 0; var14 < 48 && var14 < var8; var14++) {
            double var15 = Math.min(1.0, var8 - var14);
            double var17 = this.i[var14] + (this.i[var14 + 1] - this.i[var14]) * var15;
            double var19 = this.fu[var14] + (this.fu[var14 + 1] - this.fu[var14]) * var15;
            this.J(
               var1,
               this.i(var2, this.i[var14], var12),
               this.i(var2, var17, var12),
               this.i(var2, var17, var19),
               this.i(var2, this.i[var14], this.fu[var14]),
               var11,
               var11,
               var10,
               var10
            );
         }
      }
   }

   private void j(f1 var1, yt.l var2, double var3, int var5, int var6, double var7, double var9) {
      if (!(var3 < 0.35)) {
         double var11 = Y(var3) * 48.0;
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
            double var23 = this.fu[var19];
            double var25 = this.C(var21);
            double var27 = Math.max(var23, var25);
            double var29 = Math.min(var23, var25);

            for (int var31 = var19 + 1; var31 < var21; var31++) {
               var27 = Math.max(var27, this.fu[var31]);
               var29 = Math.min(var29, this.fu[var31]);
            }

            int var48 = var11 < var20 ? 1 : 0;
            double var32 = var48 != 0 ? 1.0 : u8.S((var11 - var20) / 2.0 + 0.5);
            var27 += this.C[var18] * var32;
            var29 -= this.k[var18] * var32;
            double var34 = -2.35 + var14 * (var18 + 0.5);
            int var36 = var25 >= var23 ? 1 : 0;
            int var37 = ei.W(var36 != 0 ? var5 : var6, -10656141, var7 * 0.65);
            double var38 = var9 * (0.55 + 0.2 * (1.0 - var7));
            int var40 = ei.R(var37, var38 * 0.8);
            var1.ribbon(this.i(var2, var34, var29), this.i(var2, var34, var27), 0.018, var40, var40);
            double var41 = Math.min(var23, var25);
            double var43 = Math.max(Math.max(var23, var25), var41 + 0.02);
            int var45 = ei.R(var37, var38);
            this.g(var1, var2, var34 - var16 * 0.5, var41, var34 + var16 * 0.5, var43, var45, var45);
         }
      }
   }

   private double C(double var1) {
      int var3 = Math.min(47, (int)Math.floor(var1));
      return this.fu[var3] + (this.fu[var3 + 1] - this.fu[var3]) * (var1 - var3);
   }

   private void V(f1 var1, yt.l var2, double var3, int var5, double var6) {
      if (!(var3 < 0.35) && !(var6 <= 0.001)) {
         double var8 = Y(var3) * 48.0;
         class_243 var10 = this.i(var2, this.i[0], this.fu[0]);

         for (int var11 = 0; var11 < 48 && var11 < var8; var11++) {
            double var12 = Math.min(1.0, var8 - var11);
            class_243 var14 = this.i(
               var2, this.i[var11] + (this.i[var11 + 1] - this.i[var11]) * var12, this.fu[var11] + (this.fu[var11 + 1] - this.fu[var11]) * var12
            );
            com.larpclient.c.E(var1, var10, var14, 0.055, var5, var5, var6);
            var10 = var14;
         }

         double[] var17 = this.q(var3);
         class_243 var18 = this.i(var2, var17[0], var17[1]);
         double var13 = 0.5 + 0.5 * Math.sin(var3 * 9.0);
         double var15 = var3 < 2.6 ? 1.0 : 1.0 - u8.S((var3 - 2.6) / 0.2);
         var1.V(var18.field_1352, var18.field_1351, var18.field_1350, 0.07 + var13 * 0.03, var3, ei.R(-1, var6 * var15));
         var1.V(var18.field_1352, var18.field_1351, var18.field_1350, 0.16 + var13 * 0.05, -var3, ei.R(var5, var6 * 0.55 * var15));
      }
   }

   private void k(f1 var1, yt.l var2, double var3, int var5, int var6, double var7) {
      if (!(var3 < 0.35)) {
         double var9;
         double var11;
         if (var3 < 2.6) {
            double[] var13 = this.q(var3);
            var9 = var13[1];
            var11 = var13[0];
         } else {
            var9 = Math.max(0.18, 3.3 * (1.0 - Math.min(1.0, d(var3))));
            var11 = -0.22;
         }

         int var20 = ei.R(var3 < 2.6 ? var5 : var6, var7 * 0.45 * u8.S((var3 - 0.35) / 0.3));
         double var14 = 0.11;
         double var16 = 0.07;

         for (double var18 = var11 + 0.1; var18 < 1.1; var18 += var14 + var16) {
            var1.ribbon(this.i(var2, var18, var9), this.i(var2, Math.min(1.1, var18 + var14), var9), 0.014, var20, var20);
         }
      }
   }

   private void T(f1 var1, yt.l var2, double var3, int var5, double var6) {
      if (!(var3 < 2.6)) {
         double var8 = d(var3);
         class_243 var10 = this.i(var2, -0.32, 3.3);
         class_243 var11 = this.P(var2, var3);
         if (var8 <= 1.0) {
            com.larpclient.c.E(var1, var10, var11, 0.085, var5, var5, var6);
         } else {
            com.larpclient.c.E(var1, var10, this.f.z, 0.085, var5, var5, var6);
            com.larpclient.c.E(var1, this.f.z, var11, 0.085, var5, var5, var6);
         }

         double var12 = -0.15083333333333332;
         double var14 = 0.0609;
         double var16 = Math.max(0.12000000000000001, 3.3 * (1.0 - Math.min(1.0, var8)));
         this.g(var1, var2, var12 - var14, var16, var12 + var14, 3.3, ei.R(var5, var6 * 0.55), ei.R(var5, var6 * 0.8));
         if (var8 < 1.0) {
            var1.V(var11.field_1352, var11.field_1351, var11.field_1350, 0.14, var3 * 3.0, ei.R(-1, var6));
         }
      }
   }

   private void R(f1 var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0)) {
         class_243 var9 = this.f.z.method_1031(0.0, 0.03, 0.0);
         double var10 = u8.o(var7, 0.0, 0.8) * var5;
         com.larpclient.c.u(var1, var9, 0.15 + var7 * 3.4, 0.07, var4, var10);
         com.larpclient.c.u(var1, var9.method_1031(0.0, 0.005, 0.0), 0.1 + var7 * 2.2, 0.02, -1, var10 * 0.6);
         double var12 = u8.S(var7 / 0.12) * (1.0 - u8.S((var2 - 5.6 + 0.4) / 0.8)) * var5;
         com.larpclient.c.u(var1, var9, 0.42 + 0.03 * Math.sin(var2 * 6.0), 0.05, var4, var12 * 0.5);
         double var14 = u8.S(var7 / 0.35);

         for (int var16 = 0; var16 < 7; var16++) {
            double var17 = this.z[var16];
            double var19 = this.N[var16] * var14;
            class_243 var21 = var9;

            for (int var22 = 1; var22 <= 4; var22++) {
               double var23 = var22 / 4.0;
               double var25 = (this.f.P(16200 + var16 * 8 + var22) - 0.5) * 0.5 * var23;
               class_243 var27 = var9.method_1031(Math.cos(var17 + var25) * var19 * var23, 0.0, Math.sin(var17 + var25) * var19 * var23);
               com.larpclient.c.E(var1, var21, var27, 0.05 * (1.2 - var23), var4, var4, var12 * 0.85);
               var21 = var27;
            }
         }

         com.larpclient.c.k(var1, this.f, var7, var9.method_1031(0.0, 0.1, 0.0), var4, -11566, 34, 2.1, var5);
      }
   }

   private void c(f1 var1, double var2, int var4, double var5) {
      double var7 = var2 - 2.82;
      if (!(var7 < 0.0) && !(var7 > 1.6)) {
         for (int var9 = 0; var9 < 18; var9++) {
            double var10 = this.fs[var9];
            double var12 = this.Y[var9];
            double var14 = this.T[var9] * var7 - 4.9 * var7 * var7;
            double var16 = var14 < 0.0 ? 0.0 : 1.0;
            double var18 = var10 * var7;
            double var20 = var5 * u8.S((1.6 - var7) / 0.4) * var16;
            if (!(var20 <= 0.002)) {
               class_243 var22 = this.f.z.method_1031(Math.cos(var12) * var18, Math.max(0.0, var14) + 0.04, Math.sin(var12) * var18);
               int var23 = ei.R(ei.W(-14016476, var4, this.f.P(16300 + var9) * 0.35), var20);
               var1.X(
                  var22.field_1352, var22.field_1351, var22.field_1350, 0.06 + this.f.P(16400 + var9) * 0.05, var12 + var7 * 7.0, var7 * 9.0, var7 * 5.0, var23
               );
            }
         }
      }
   }

   private void n(f1 var1, yt.l var2, double var3, double var5) {
      class_243 var7 = this.f.z.method_1031(0.0, -0.4, 0.0);

      for (int var8 = 0; var8 < 16; var8++) {
         double var9 = var3 - this.V[var8];
         if (!(var9 < 0.0)) {
            double[] var11 = this.q(this.V[var8]);
            class_243 var12 = this.i(var2, var11[0], var11[1]).method_1019(var2.normal().method_1021(0.2));
            double var13 = Math.min(var9, Math.max(0.0, 2.6 - this.V[var8]));
            class_243 var15 = var12.method_1019(var2.right().method_1021(this.m[var8] * 0.35 * var13))
               .method_1031(0.0, 0.45 * var13, 0.0)
               .method_1019(var2.normal().method_1021(0.25 * var13));
            double var16 = var3 < 2.6 ? 0.0 : u8.S((var3 - 2.6 - var8 * 0.012) / 0.5);
            class_243 var18 = var15.method_35590(var7, var16 * var16);
            double var19 = var5 * u8.S(var9 / 0.2) * (1.0 - u8.S((var16 - 0.75) / 0.25));
            if (!(var19 <= 0.002)) {
               double var21 = this.A[var8] + var9 * 2.4 + var16 * 8.0;
               var1.X(
                  var18.field_1352,
                  var18.field_1351,
                  var18.field_1350,
                  0.17 * (1.0 - var16 * 0.5),
                  var21,
                  0.4 * Math.sin(var9 * 3.0 + var8),
                  0.3 * Math.cos(var9 * 2.3 + var8),
                  Math.sin(var9 * 6.0 + var8) * 0.6,
                  ei.R(-1, var19)
               );
            }
         }
      }
   }

   private void Y(f1 var1, double var2, double var4) {
      double var6 = u8.S(var2 / 0.5);
      double var8 = 1.0 - u8.S((var2 - 2.6 - 0.25) / 0.3);
      double var10 = var4 * var6 * var8;
      if (!(var10 <= 0.002)) {
         class_243 var12 = this.w();
         class_243 var13 = this.W();
         double var14 = x(var2);
         double var16 = var2 < 2.6 ? 0.0 : 0.5 * Math.sin(Math.min(1.0, (var2 - 2.6) / 0.45) * Math.PI);
         double var18 = -0.775 + var14;
         double var20 = var18 + 1.55 * var6;
         class_243 var22 = this.f.z.method_1031(0.0, 0.012, 0.0);
         double[] var23 = new double[]{var18, var20};
         double[] var24 = new double[]{var16, var16 * 0.35};
         int var25 = ei.R(ei.W(-6217428, -16777216, 0.2), var10);
         this.O(var1, var22, var12, var13, var23, var24, 0.525, ei.R(-1920436, var10));
         this.O(var1, var22.method_1031(0.0, 0.002, 0.0), var12, var13, new double[]{var23[0] + 0.09, var23[1] - 0.09}, var24, 0.43500000000000005, var25);
         this.O(var1, var22.method_1031(0.0, 0.004, 0.0), var12, var13, new double[]{var23[0] + 0.15, var23[1] - 0.15}, var24, 0.375, ei.R(-6217428, var10));
         double var26 = (var23[0] + var23[1]) * 0.5;
         double var28 = 0.22 * var6;
         double var30 = (var24[0] + var24[1]) * 0.5;
         class_243 var32 = var22.method_1031(0.0, 0.006 + var30, 0.0).method_1019(var12.method_1021(var26));
         this.o(
            var1,
            var32.method_1019(var12.method_1021(var28)),
            var32.method_1019(var13.method_1021(var28 * 0.7)),
            var32.method_1019(var12.method_1021(-var28)),
            var32.method_1019(var13.method_1021(-var28 * 0.7)),
            ei.R(-1920436, var10)
         );
         int var33 = ei.R(-792892, var10 * 0.9);

         for (int var34 = 0; var34 < 2; var34++) {
            double var35 = var23[var34];
            double var37 = var34 == 0 ? -1.0 : 1.0;

            for (int var39 = 0; var39 < 9; var39++) {
               double var40 = (var39 / 8.0 - 0.5) * 0.93;
               class_243 var42 = var22.method_1031(0.0, var24[var34] + 0.008, 0.0).method_1019(var12.method_1021(var35)).method_1019(var13.method_1021(var40));
               var1.ribbon(var42, var42.method_1019(var12.method_1021(var37 * 0.1)), 0.018, var33, ei.R(var33, 0.0));
            }
         }
      }
   }

   private void O(f1 var1, class_243 var2, class_243 var3, class_243 var4, double[] var5, double[] var6, double var7, int var9) {
      if (!(var5[1] - var5[0] <= 0.001)) {
         class_243 var10 = var2.method_1019(var3.method_1021(var5[0])).method_1031(0.0, var6[0], 0.0);
         class_243 var11 = var2.method_1019(var3.method_1021(var5[1])).method_1031(0.0, var6[1], 0.0);
         this.o(
            var1,
            var10.method_1019(var4.method_1021(-var7)),
            var10.method_1019(var4.method_1021(var7)),
            var11.method_1019(var4.method_1021(var7)),
            var11.method_1019(var4.method_1021(-var7)),
            var9
         );
      }
   }
}
