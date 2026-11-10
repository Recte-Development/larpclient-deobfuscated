package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.SplittableRandom;
import net.minecraft.class_10799;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import org.joml.Matrix4f;

public final class g7 {
   public static final int x = 288;
   static final double n = 120.0;
   static final double y = 90.0;
   public static final float Q = 0.24F;
   static final double U = 0.6;
   static final double X = 0.25;
   static final double g = 0.36;
   static final double d = 0.16;
   static final double a = 0.85;
   static final double D = 0.95;
   static final double K = 0.3;
   static final double W = 1.9;
   static final double w = 2.5;
   static final double M = 0.19;
   static final double f = 0.11;
   static final double C = 0.09;
   static final double m = 0.05;
   static final double b = 0.09;
   public static final double A = 1.8;
   public static final double p = 1.4;
   static final double V = 0.5;
   private final double[] J = new double[288];
   private final double[] o = new double[288];
   private final double[] H = new double[288];
   private final double[] L = new double[288];
   private final double[] k = new double[288];
   private final double[] i = new double[288];
   private final float[] h = new float[288];
   private final float[] I = new float[288];
   private final float[] P = new float[288];
   private final float[] R = new float[288];
   private final float[] q = new float[288];
   private final float[] E = new float[288];
   private final boolean[] Z = new boolean[288];
   private final SplittableRandom c;
   private int O;
   private double s = Double.NaN;
   private double j;
   private double u;
   private double v;
   private double F;
   private double t;
   private double G;
   private double S;
   private double Y;
   public final Matrix4f r = new Matrix4f();
   public long l = -1L;

   public g7(long var1) {
      this.c = new SplittableRandom(var1);
   }

   public int H() {
      return this.O;
   }

   public void A() {
      this.O = 0;
      this.s = Double.NaN;
      this.j = this.u = this.G = this.S = this.Y = 0.0;
   }

   public static double s(double var0, double var2) {
      double var4 = 0.7224999999999999;
      double var6 = 1.15;
      return (var6 * var4 + 0.95 * var4 * var4) * var2 * var0 + 0.1368 * var0;
   }

   public void g(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.s;
      double var21 = (var3 - this.v) * (var3 - this.v) + (var5 - this.F) * (var5 - this.F) + (var7 - this.t) * (var7 - this.t);
      if (var19 >= 0.0 && !(var19 > 0.25) && !(var21 > 4.0)) {
         if (var19 != 0.0) {
            float var23 = (float)var19;
            double var24 = 1.0 - Math.exp(-var19 * 12.0);
            this.G = this.G + ((var3 - this.v) / var19 - this.G) * var24;
            this.S = this.S + ((var5 - this.F) / var19 - this.S) * var24;
            this.Y = this.Y + ((var7 - this.t) / var19 - this.Y) * var24;
            double var26 = Math.exp(-2.5 * var19);
            int var28 = 0;

            while (var28 < this.O) {
               this.h[var28] = this.h[var28] + var23;
               if (this.h[var28] >= this.I[var28]) {
                  this.K(var28);
               } else {
                  double var29 = (this.Z[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
                  double var31 = this.L[var28] * var9 + this.k[var28] * var11 + this.i[var28] * var13 + var29;
                  this.L[var28] = (this.L[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
                  this.k[var28] = (this.k[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
                  this.i[var28] = (this.i[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
                  this.J[var28] = this.J[var28] + this.L[var28] * var19;
                  this.o[var28] = this.o[var28] + this.k[var28] * var19;
                  this.H[var28] = this.H[var28] + this.i[var28] * var19;
                  this.R[var28] = this.R[var28] + this.q[var28] * var23;
                  var28++;
               }
            }

            double var62 = Math.abs(var11) < 0.9 ? 0.0 : 1.0;
            double var30 = Math.abs(var11) < 0.9 ? 1.0 : 0.0;
            double var32 = var30 * var13 - 0.0 * var11;
            double var34 = 0.0 * var9 - var62 * var13;
            double var36 = var62 * var11 - var30 * var9;
            double var38 = Math.sqrt(var32 * var32 + var34 * var34 + var36 * var36);
            var32 /= var38;
            var34 /= var38;
            var36 /= var38;
            double var40 = var11 * var36 - var13 * var34;
            double var42 = var13 * var32 - var9 * var36;
            double var44 = var9 * var34 - var11 * var32;
            this.j += 120.0 * var19;
            this.u += 90.0 * var19;

            while (this.j >= 1.0 || this.u >= 1.0) {
               int var46 = this.u >= this.j ? 1 : 0;
               if (var46 != 0) {
                  this.u--;
               } else {
                  this.j--;
               }

               if (this.O != 288) {
                  double var47 = this.c.nextDouble();
                  double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.c.nextDouble());
                  double var51 = this.c.nextDouble() * Math.PI * 2.0;
                  double var53 = Math.cos(var51);
                  double var55 = Math.sin(var51);
                  double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.c.nextDouble() * 0.3) * var17 * var15;
                  double var59 = 0.09 * var15 * this.c.nextDouble();
                  int var61 = this.O++;
                  this.Z[var61] = (boolean)var46;
                  this.J[var61] = this.v + (var3 - this.v) * var47 + (var32 * var53 + var40 * var55) * var49;
                  this.o[var61] = this.F + (var5 - this.F) * var47 + (var34 * var53 + var42 * var55) * var49;
                  this.H[var61] = this.t + (var7 - this.t) * var47 + (var36 * var53 + var44 * var55) * var49;
                  this.L[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.G * 0.5;
                  this.k[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.S * 0.5;
                  this.i[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.Y * 0.5;
                  this.I[var61] = (float)(var46 != 0 ? 0.36 + this.c.nextDouble() * 0.16 : 0.6 + this.c.nextDouble() * 0.25);
                  this.h[var61] = (float)((1.0 - var47) * var19);
                  this.P[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.c.nextDouble() * 0.4));
                  this.R[var61] = (float)((this.c.nextDouble() - 0.5) * 0.5);
                  this.q[var61] = (float)((this.c.nextDouble() - 0.5) * 1.2);
                  this.E[var61] = (float)this.c.nextDouble();
               }
            }

            this.s = var1;
            this.v = var3;
            this.F = var5;
            this.t = var7;
         }
      } else {
         this.A();
         this.s = var1;
         this.v = var3;
         this.F = var5;
         this.t = var7;
      }
   }

   private void K(int var1) {
      int var2 = --this.O;
      this.J[var1] = this.J[var2];
      this.o[var1] = this.o[var2];
      this.H[var1] = this.H[var2];
      this.L[var1] = this.L[var2];
      this.k[var1] = this.k[var2];
      this.i[var1] = this.i[var2];
      this.h[var1] = this.h[var2];
      this.I[var1] = this.I[var2];
      this.P[var1] = this.P[var2];
      this.R[var1] = this.R[var2];
      this.q[var1] = this.q[var2];
      this.E[var1] = this.E[var2];
      this.Z[var1] = this.Z[var2];
   }

   public void F(g7.r var1, int var2, int var3, double var4) {
      int var6 = ei.W(var2, -13631488, 0.3);
      int var7 = ei.W(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.O; var9++) {
            if (this.Z[var9] == (var8 == 1 ? 1 : 0)) {
               double var10 = Math.clamp(this.h[var9] / this.I[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.Z[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = e(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = ei.W(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = e(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? ei.W(var3, var2, var10 / 0.4) : ei.W(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.Z(this.J[var9], this.o[var9], this.H[var9], this.P[var9] * var12, this.Z[var9] ? 1.4 : 1.8, this.R[var9], var17 << 24 | var16 & 16777215);
               }
            }
         }
      }
   }

   static double e(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static class_1921 p() {
      if (g7.j.u != t_.g) {
         f2.a(g7.j.v);
         g7.j.u = t_.g;
      }

      return g7.j.h;
   }

   private static final class j {
      static final RenderPipeline v = RenderPipeline.builder(new Snippet[]{class_10799.field_56864})
         .withLocation(class_2960.method_60655("larp-client", "china_hat_fire"))
         .withFragmentShader(class_2960.method_60655("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withCull(false)
         .build();
      static final class_1921 h = class_1921.method_75940(
         "larp_china_hat_fire",
         class_12247.method_75927(v).method_75934("Sampler0", class_2960.method_60655("larp-client", "textures/particles/flame.png")).method_75938()
      );
      private static long u = -1L;
   }

   @FunctionalInterface
   public interface r {
      void Z(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}
