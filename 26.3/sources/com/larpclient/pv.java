package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class pv {
   private static final Vector3f[] A = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] O = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] M = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] N = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float f = 0.002F;
   private static final float i = -0.194F;
   private static final float l = 0.9F;
   private static final float a = 0.7F;
   private final qt.v o;
   private final qt.v W;
   private final int[] G;
   private final float[] n;
   private final Matrix4f[] S = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] E = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f F = new Vector3f();
   private final Vector3f m = new Vector3f();
   private float Y;

   public pv(qt.v var1) {
      p1.c var2 = p1.r(var1);
      this.o = var1;
      this.W = var2.mesh();
      this.G = var2.joints();
      this.n = (float[])this.W.vertices().clone();
   }

   public qt.v i() {
      return this.o;
   }

   public qt.v t() {
      return this.W;
   }

   public float[] J() {
      return this.n;
   }

   private static void c(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float T(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] l(float var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      float var4 = var1 * 0.6662F;
      float var5 = (float)Math.cos(var4) * var3;
      float var6 = (float)Math.sin(var4) * var3;
      float[] var7 = new float[]{var5 * 0.9F, -var5 * 0.9F};
      float[] var8 = new float[]{-var5 * 0.7F, var5 * 0.7F};
      float[] var9 = new float[]{Math.max(0.0F, var6) * 0.9F, Math.max(0.0F, -var6) * 0.9F};
      Matrix4f var10 = new Matrix4f();

      for (int var11 = 0; var11 < 2; var11++) {
         float var12 = -0.3F * var3 - (var11 == 0 ? Math.max(0.0F, var8[var11]) : 0.0F);
         c(this.S[var11], var10, A[var11], var8[var11]);
         c(this.E[var11], this.S[var11], O[var11], var12);
         c(this.S[var11 + 2], var10, M[var11], var7[var11]);
         c(this.E[var11 + 2], this.S[var11 + 2], N[var11], var9[var11]);
      }

      float[] var21 = this.W.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.G.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.G[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.F.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.m.set(this.F);
            this.S[var20].transformPosition(this.F);
            this.E[var20].transformPosition(this.m);
            this.F.lerp(this.m, T(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.F.y);
            }
         }

         this.n[var14] = this.F.x;
         this.n[var14 + 1] = this.F.y;
         this.n[var14 + 2] = this.F.z;
      }

      this.Y = var22 - this.W.minimumY();

      for (byte var23 = 1; var23 < this.n.length; var23 += 5) {
         this.n[var23] = this.n[var23] - this.Y;
      }

      return this.n;
   }

   public Vector3f p(boolean var1, Vector3f var2) {
      return this.E[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.Y, 0.0F);
   }
}
