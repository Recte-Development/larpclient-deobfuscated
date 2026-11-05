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

public final class jp {
   public static final int F = 288;
   static final double f = 120.0;
   static final double A = 90.0;
   public static final float M = 0.24F;
   static final double H = 0.6;
   static final double Q = 0.25;
   static final double C = 0.36;
   static final double w = 0.16;
   static final double T = 0.85;
   static final double g = 0.95;
   static final double J = 0.3;
   static final double O = 1.9;
   static final double r = 2.5;
   static final double d = 0.19;
   static final double j = 0.11;
   static final double G = 0.09;
   static final double s = 0.05;
   static final double l = 0.09;
   public static final double o = 1.8;
   public static final double I = 1.4;
   static final double v = 0.5;
   private final double[] R = new double[288];
   private final double[] U = new double[288];
   private final double[] u = new double[288];
   private final double[] N = new double[288];
   private final double[] V = new double[288];
   private final double[] P = new double[288];
   private final float[] Y = new float[288];
   private final float[] p = new float[288];
   private final float[] t = new float[288];
   private final float[] y = new float[288];
   private final float[] c = new float[288];
   private final float[] X = new float[288];
   private final boolean[] L = new boolean[288];
   private final SplittableRandom Z;
   private int W;
   private double S = Double.NaN;
   private double m;
   private double n;
   private double B;
   private double a;
   private double h;
   private double b;
   private double z;
   private double x;
   public final Matrix4f k = new Matrix4f();
   public long K = -1L;

   public jp(long var1) {
      this.Z = new SplittableRandom(var1);
   }

   public int Y() {
      return this.W;
   }

   public void s() {
      this.W = 0;
      this.S = Double.NaN;
      this.m = this.n = this.b = this.z = this.x = 0.0;
   }

   public static double G(double var0, double var2) {
      double var4 = 0.7224999999999999;
      double var6 = 1.15;
      return (var6 * var4 + 0.95 * var4 * var4) * var2 * var0 + 0.1368 * var0;
   }

   public void M(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.S;
      double var21 = (var3 - this.B) * (var3 - this.B) + (var5 - this.a) * (var5 - this.a) + (var7 - this.h) * (var7 - this.h);
      if (var19 >= 0.0 && !(var19 > 0.25) && !(var21 > 4.0)) {
         if (var19 != 0.0) {
            float var23 = (float)var19;
            double var24 = 1.0 - Math.exp(-var19 * 12.0);
            this.b = this.b + ((var3 - this.B) / var19 - this.b) * var24;
            this.z = this.z + ((var5 - this.a) / var19 - this.z) * var24;
            this.x = this.x + ((var7 - this.h) / var19 - this.x) * var24;
            double var26 = Math.exp(-2.5 * var19);
            int var28 = 0;

            while (var28 < this.W) {
               this.Y[var28] = this.Y[var28] + var23;
               if (this.Y[var28] >= this.p[var28]) {
                  this.Y(var28);
               } else {
                  double var29 = (this.L[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
                  double var31 = this.N[var28] * var9 + this.V[var28] * var11 + this.P[var28] * var13 + var29;
                  this.N[var28] = (this.N[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
                  this.V[var28] = (this.V[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
                  this.P[var28] = (this.P[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
                  this.R[var28] = this.R[var28] + this.N[var28] * var19;
                  this.U[var28] = this.U[var28] + this.V[var28] * var19;
                  this.u[var28] = this.u[var28] + this.P[var28] * var19;
                  this.y[var28] = this.y[var28] + this.c[var28] * var23;
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
            this.m += 120.0 * var19;
            this.n += 90.0 * var19;

            while (this.m >= 1.0 || this.n >= 1.0) {
               int var46 = this.n >= this.m ? 1 : 0;
               if (var46 != 0) {
                  this.n--;
               } else {
                  this.m--;
               }

               if (this.W != 288) {
                  double var47 = this.Z.nextDouble();
                  double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.Z.nextDouble());
                  double var51 = this.Z.nextDouble() * Math.PI * 2.0;
                  double var53 = Math.cos(var51);
                  double var55 = Math.sin(var51);
                  double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.Z.nextDouble() * 0.3) * var17 * var15;
                  double var59 = 0.09 * var15 * this.Z.nextDouble();
                  int var61 = this.W++;
                  this.L[var61] = (boolean)var46;
                  this.R[var61] = this.B + (var3 - this.B) * var47 + (var32 * var53 + var40 * var55) * var49;
                  this.U[var61] = this.a + (var5 - this.a) * var47 + (var34 * var53 + var42 * var55) * var49;
                  this.u[var61] = this.h + (var7 - this.h) * var47 + (var36 * var53 + var44 * var55) * var49;
                  this.N[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.b * 0.5;
                  this.V[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.z * 0.5;
                  this.P[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.x * 0.5;
                  this.p[var61] = (float)(var46 != 0 ? 0.36 + this.Z.nextDouble() * 0.16 : 0.6 + this.Z.nextDouble() * 0.25);
                  this.Y[var61] = (float)((1.0 - var47) * var19);
                  this.t[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.Z.nextDouble() * 0.4));
                  this.y[var61] = (float)((this.Z.nextDouble() - 0.5) * 0.5);
                  this.c[var61] = (float)((this.Z.nextDouble() - 0.5) * 1.2);
                  this.X[var61] = (float)this.Z.nextDouble();
               }
            }

            this.S = var1;
            this.B = var3;
            this.a = var5;
            this.h = var7;
         }
      } else {
         this.s();
         this.S = var1;
         this.B = var3;
         this.a = var5;
         this.h = var7;
      }
   }

   private void Y(int var1) {
      int var2 = --this.W;
      this.R[var1] = this.R[var2];
      this.U[var1] = this.U[var2];
      this.u[var1] = this.u[var2];
      this.N[var1] = this.N[var2];
      this.V[var1] = this.V[var2];
      this.P[var1] = this.P[var2];
      this.Y[var1] = this.Y[var2];
      this.p[var1] = this.p[var2];
      this.t[var1] = this.t[var2];
      this.y[var1] = this.y[var2];
      this.c[var1] = this.c[var2];
      this.X[var1] = this.X[var2];
      this.L[var1] = this.L[var2];
   }

   public void l(jp.k var1, int var2, int var3, double var4) {
      int var6 = oh.j(var2, -13631488, 0.3);
      int var7 = oh.j(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.W; var9++) {
            if (this.L[var9] == (var8 == 1 ? 1 : 0)) {
               double var10 = Math.clamp(this.Y[var9] / this.p[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.L[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = O(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = oh.j(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = O(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? oh.j(var3, var2, var10 / 0.4) : oh.j(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.d(this.R[var9], this.U[var9], this.u[var9], this.t[var9] * var12, this.L[var9] ? 1.4 : 1.8, this.y[var9], var17 << 24 | var16 & 16777215);
               }
            }
         }
      }
   }

   static double O(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static RenderType p() {
      if (jp.b.X != jk.v) {
         e4.U(jp.b.P);
         jp.b.X = jk.v;
      }

      return jp.b.V;
   }

   private static final class b {
      static final RenderPipeline P = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_fire"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      static final RenderType V = RenderType.create(
         "larp_china_hat_fire",
         RenderSetup.builder(P).withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png")).createRenderSetup()
      );
      private static long X = -1L;
   }

   @FunctionalInterface
   public interface k {
      void d(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}
