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

public final class db {
   public static final int q = 288;
   static final double i = 120.0;
   static final double I = 90.0;
   public static final float v = 0.24F;
   static final double Z = 0.6;
   static final double b = 0.25;
   static final double Y = 0.36;
   static final double y = 0.16;
   static final double B = 0.85;
   static final double x = 0.95;
   static final double A = 0.3;
   static final double t = 1.9;
   static final double O = 2.5;
   static final double w = 0.19;
   static final double h = 0.11;
   static final double n = 0.09;
   static final double E = 0.05;
   static final double r = 0.09;
   public static final double L = 1.8;
   public static final double V = 1.4;
   static final double P = 0.5;
   private final double[] c = new double[288];
   private final double[] l = new double[288];
   private final double[] k = new double[288];
   private final double[] C = new double[288];
   private final double[] M = new double[288];
   private final double[] K = new double[288];
   private final float[] F = new float[288];
   private final float[] j = new float[288];
   private final float[] o = new float[288];
   private final float[] T = new float[288];
   private final float[] W = new float[288];
   private final float[] z = new float[288];
   private final boolean[] u = new boolean[288];
   private final SplittableRandom H;
   private int S;
   private double J = Double.NaN;
   private double m;
   private double e;
   private double D;
   private double s;
   private double N;
   private double G;
   private double g;
   private double U;
   public final Matrix4f f = new Matrix4f();
   public long R = -1L;

   public db(long var1) {
      this.H = new SplittableRandom(var1);
   }

   public int J() {
      return this.S;
   }

   public void k() {
      this.S = 0;
      this.J = Double.NaN;
      this.m = this.e = this.G = this.g = this.U = 0.0;
   }

   public static double U(double var0, double var2) {
      double var4 = 0.7224999999999999;
      double var6 = 1.15;
      return (var6 * var4 + 0.95 * var4 * var4) * var2 * var0 + 0.1368 * var0;
   }

   public void S(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.J;
      double var21 = (var3 - this.D) * (var3 - this.D) + (var5 - this.s) * (var5 - this.s) + (var7 - this.N) * (var7 - this.N);
      if (var19 >= 0.0 && !(var19 > 0.25) && !(var21 > 4.0)) {
         if (var19 != 0.0) {
            float var23 = (float)var19;
            double var24 = 1.0 - Math.exp(-var19 * 12.0);
            this.G = this.G + ((var3 - this.D) / var19 - this.G) * var24;
            this.g = this.g + ((var5 - this.s) / var19 - this.g) * var24;
            this.U = this.U + ((var7 - this.N) / var19 - this.U) * var24;
            double var26 = Math.exp(-2.5 * var19);
            int var28 = 0;

            while (var28 < this.S) {
               this.F[var28] = this.F[var28] + var23;
               if (this.F[var28] >= this.j[var28]) {
                  this.j(var28);
               } else {
                  double var29 = (this.u[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
                  double var31 = this.C[var28] * var9 + this.M[var28] * var11 + this.K[var28] * var13 + var29;
                  this.C[var28] = (this.C[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
                  this.M[var28] = (this.M[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
                  this.K[var28] = (this.K[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
                  this.c[var28] = this.c[var28] + this.C[var28] * var19;
                  this.l[var28] = this.l[var28] + this.M[var28] * var19;
                  this.k[var28] = this.k[var28] + this.K[var28] * var19;
                  this.T[var28] = this.T[var28] + this.W[var28] * var23;
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
            this.e += 90.0 * var19;

            while (this.m >= 1.0 || this.e >= 1.0) {
               int var46 = this.e >= this.m ? 1 : 0;
               if (var46 != 0) {
                  this.e--;
               } else {
                  this.m--;
               }

               if (this.S != 288) {
                  double var47 = this.H.nextDouble();
                  double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.H.nextDouble());
                  double var51 = this.H.nextDouble() * Math.PI * 2.0;
                  double var53 = Math.cos(var51);
                  double var55 = Math.sin(var51);
                  double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.H.nextDouble() * 0.3) * var17 * var15;
                  double var59 = 0.09 * var15 * this.H.nextDouble();
                  int var61 = this.S++;
                  this.u[var61] = (boolean)var46;
                  this.c[var61] = this.D + (var3 - this.D) * var47 + (var32 * var53 + var40 * var55) * var49;
                  this.l[var61] = this.s + (var5 - this.s) * var47 + (var34 * var53 + var42 * var55) * var49;
                  this.k[var61] = this.N + (var7 - this.N) * var47 + (var36 * var53 + var44 * var55) * var49;
                  this.C[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.G * 0.5;
                  this.M[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.g * 0.5;
                  this.K[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.U * 0.5;
                  this.j[var61] = (float)(var46 != 0 ? 0.36 + this.H.nextDouble() * 0.16 : 0.6 + this.H.nextDouble() * 0.25);
                  this.F[var61] = (float)((1.0 - var47) * var19);
                  this.o[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.H.nextDouble() * 0.4));
                  this.T[var61] = (float)((this.H.nextDouble() - 0.5) * 0.5);
                  this.W[var61] = (float)((this.H.nextDouble() - 0.5) * 1.2);
                  this.z[var61] = (float)this.H.nextDouble();
               }
            }

            this.J = var1;
            this.D = var3;
            this.s = var5;
            this.N = var7;
         }
      } else {
         this.k();
         this.J = var1;
         this.D = var3;
         this.s = var5;
         this.N = var7;
      }
   }

   private void j(int var1) {
      int var2 = --this.S;
      this.c[var1] = this.c[var2];
      this.l[var1] = this.l[var2];
      this.k[var1] = this.k[var2];
      this.C[var1] = this.C[var2];
      this.M[var1] = this.M[var2];
      this.K[var1] = this.K[var2];
      this.F[var1] = this.F[var2];
      this.j[var1] = this.j[var2];
      this.o[var1] = this.o[var2];
      this.T[var1] = this.T[var2];
      this.W[var1] = this.W[var2];
      this.z[var1] = this.z[var2];
      this.u[var1] = this.u[var2];
   }

   public void z(db.m var1, int var2, int var3, double var4) {
      int var6 = _3.D(var2, -13631488, 0.3);
      int var7 = _3.D(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.S; var9++) {
            if (this.u[var9] == (var8 == 1 ? 1 : 0)) {
               double var10 = Math.clamp(this.F[var9] / this.j[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.u[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = e(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = _3.D(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = e(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? _3.D(var3, var2, var10 / 0.4) : _3.D(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.y(this.c[var9], this.l[var9], this.k[var9], this.o[var9] * var12, this.u[var9] ? 1.4 : 1.8, this.T[var9], var17 << 24 | var16 & 16777215);
               }
            }
         }
      }
   }

   static double e(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static RenderType Q() {
      if (db.h.G != m5.k) {
         ea.z(db.h.W);
         db.h.G = m5.k;
      }

      return db.h.J;
   }

   private static final class h {
      static final RenderPipeline W = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_fire"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      static final RenderType J = RenderType.create(
         "larp_china_hat_fire",
         RenderSetup.builder(W).withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png")).createRenderSetup()
      );
      private static long G = -1L;
   }

   @FunctionalInterface
   public interface m {
      void y(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}
