package com.larpclient;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.SplittableRandom;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class dj {
   public static final int D = 288;
   static final double x = 120.0;
   static final double h = 90.0;
   public static final float Z = 0.24F;
   static final double c = 0.6;
   static final double r = 0.25;
   static final double V = 0.36;
   static final double T = 0.16;
   static final double d = 0.85;
   static final double W = 0.95;
   static final double B = 0.3;
   static final double g = 1.9;
   static final double j = 2.5;
   static final double y = 0.19;
   static final double I = 0.11;
   static final double q = 0.09;
   static final double m = 0.05;
   static final double a = 0.09;
   public static final double J = 1.8;
   public static final double f = 1.4;
   static final double e = 0.5;
   private final double[] L = new double[288];
   private final double[] X = new double[288];
   private final double[] P = new double[288];
   private final double[] M = new double[288];
   private final double[] C = new double[288];
   private final double[] N = new double[288];
   private final float[] i = new float[288];
   private final float[] w = new float[288];
   private final float[] R = new float[288];
   private final float[] n = new float[288];
   private final float[] E = new float[288];
   private final float[] u = new float[288];
   private final boolean[] b = new boolean[288];
   private final SplittableRandom U;
   private int o;
   private double K = Double.NaN;
   private double s;
   private double G;
   private double p;
   private double t;
   private double H;
   private double l;
   private double v;
   private double F;
   public final Matrix4f A = new Matrix4f();
   public long S = -1L;

   public dj(long var1) {
      this.U = new SplittableRandom(var1);
   }

   public int g() {
      return this.o;
   }

   public void u() {
      this.o = 0;
      this.K = Double.NaN;
      this.s = this.G = this.l = this.v = this.F = 0.0;
   }

   public static double g(double var0, double var2) {
      double var4 = 0.7224999999999999;
      double var6 = 1.15;
      return (var6 * var4 + 0.95 * var4 * var4) * var2 * var0 + 0.1368 * var0;
   }

   public void m(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.K;
      double var21 = (var3 - this.p) * (var3 - this.p) + (var5 - this.t) * (var5 - this.t) + (var7 - this.H) * (var7 - this.H);
      if (var19 >= 0.0 && !(var19 > 0.25) && !(var21 > 4.0)) {
         if (var19 != 0.0) {
            float var23 = (float)var19;
            double var24 = 1.0 - Math.exp(-var19 * 12.0);
            this.l = this.l + ((var3 - this.p) / var19 - this.l) * var24;
            this.v = this.v + ((var5 - this.t) / var19 - this.v) * var24;
            this.F = this.F + ((var7 - this.H) / var19 - this.F) * var24;
            double var26 = Math.exp(-2.5 * var19);
            int var28 = 0;

            while (var28 < this.o) {
               this.i[var28] = this.i[var28] + var23;
               if (this.i[var28] >= this.w[var28]) {
                  this.X(var28);
               } else {
                  double var29 = (this.b[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
                  double var31 = this.M[var28] * var9 + this.C[var28] * var11 + this.N[var28] * var13 + var29;
                  this.M[var28] = (this.M[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
                  this.C[var28] = (this.C[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
                  this.N[var28] = (this.N[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
                  this.L[var28] = this.L[var28] + this.M[var28] * var19;
                  this.X[var28] = this.X[var28] + this.C[var28] * var19;
                  this.P[var28] = this.P[var28] + this.N[var28] * var19;
                  this.n[var28] = this.n[var28] + this.E[var28] * var23;
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
            this.s += 120.0 * var19;
            this.G += 90.0 * var19;

            while (this.s >= 1.0 || this.G >= 1.0) {
               int var46 = this.G >= this.s ? 1 : 0;
               if (var46 != 0) {
                  this.G--;
               } else {
                  this.s--;
               }

               if (this.o != 288) {
                  double var47 = this.U.nextDouble();
                  double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.U.nextDouble());
                  double var51 = this.U.nextDouble() * Math.PI * 2.0;
                  double var53 = Math.cos(var51);
                  double var55 = Math.sin(var51);
                  double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.U.nextDouble() * 0.3) * var17 * var15;
                  double var59 = 0.09 * var15 * this.U.nextDouble();
                  int var61 = this.o++;
                  this.b[var61] = (boolean)var46;
                  this.L[var61] = this.p + (var3 - this.p) * var47 + (var32 * var53 + var40 * var55) * var49;
                  this.X[var61] = this.t + (var5 - this.t) * var47 + (var34 * var53 + var42 * var55) * var49;
                  this.P[var61] = this.H + (var7 - this.H) * var47 + (var36 * var53 + var44 * var55) * var49;
                  this.M[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.l * 0.5;
                  this.C[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.v * 0.5;
                  this.N[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.F * 0.5;
                  this.w[var61] = (float)(var46 != 0 ? 0.36 + this.U.nextDouble() * 0.16 : 0.6 + this.U.nextDouble() * 0.25);
                  this.i[var61] = (float)((1.0 - var47) * var19);
                  this.R[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.U.nextDouble() * 0.4));
                  this.n[var61] = (float)((this.U.nextDouble() - 0.5) * 0.5);
                  this.E[var61] = (float)((this.U.nextDouble() - 0.5) * 1.2);
                  this.u[var61] = (float)this.U.nextDouble();
               }
            }

            this.K = var1;
            this.p = var3;
            this.t = var5;
            this.H = var7;
         }
      } else {
         this.u();
         this.K = var1;
         this.p = var3;
         this.t = var5;
         this.H = var7;
      }
   }

   private void X(int var1) {
      int var2 = --this.o;
      this.L[var1] = this.L[var2];
      this.X[var1] = this.X[var2];
      this.P[var1] = this.P[var2];
      this.M[var1] = this.M[var2];
      this.C[var1] = this.C[var2];
      this.N[var1] = this.N[var2];
      this.i[var1] = this.i[var2];
      this.w[var1] = this.w[var2];
      this.R[var1] = this.R[var2];
      this.n[var1] = this.n[var2];
      this.E[var1] = this.E[var2];
      this.u[var1] = this.u[var2];
      this.b[var1] = this.b[var2];
   }

   public void m(dj.v var1, int var2, int var3, double var4) {
      int var6 = vv.Q(var2, -13631488, 0.3);
      int var7 = vv.Q(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.o; var9++) {
            if (this.b[var9] == (var8 == 1 ? 1 : 0)) {
               double var10 = Math.clamp(this.i[var9] / this.w[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.b[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = u(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = vv.Q(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = u(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? vv.Q(var3, var2, var10 / 0.4) : vv.Q(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.d(this.L[var9], this.X[var9], this.P[var9], this.R[var9] * var12, this.b[var9] ? 1.4 : 1.8, this.n[var9], var17 << 24 | var16 & 16777215);
               }
            }
         }
      }
   }

   static double u(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static RenderType H() {
      if (dj.f.i != nl.q) {
         se.j(dj.f.A);
         dj.f.i = nl.q;
      }

      return dj.f.L;
   }

   private static final class f {
      static final RenderPipeline A = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_fire"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      static final RenderType L = RenderType.create(
         "larp_china_hat_fire",
         RenderSetup.builder(A).withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png")).createRenderSetup()
      );
      private static long i = -1L;
   }

   @FunctionalInterface
   public interface v {
      void d(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}
