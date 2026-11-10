package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ca {
   private static final Vector3f[] O = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] L = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] l = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] P = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float z = 0.002F;
   private static final float F = -0.194F;
   private static final float S = 0.9F;
   private static final float x = 0.7F;
   private final ka.u o;
   private final ka.u g;
   private final int[] C;
   private final float[] v;
   private final Matrix4f[] c = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] u = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f X = new Vector3f();
   private final Vector3f b = new Vector3f();
   private float A;

   public ca(ka.u var1) {
      b2.r var2 = b2.d(var1);
      this.o = var1;
      this.g = var2.mesh();
      this.C = var2.joints();
      this.v = (float[])this.g.vertices().clone();
   }

   public ka.u t() {
      return this.o;
   }

   public ka.u h() {
      return this.g;
   }

   public float[] u() {
      return this.v;
   }

   private static void I(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float K(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] X(float var1, float var2) {
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
         I(this.c[var11], var10, O[var11], var8[var11]);
         I(this.u[var11], this.c[var11], L[var11], var12);
         I(this.c[var11 + 2], var10, l[var11], var7[var11]);
         I(this.u[var11 + 2], this.c[var11 + 2], P[var11], var9[var11]);
      }

      float[] var21 = this.g.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.C.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.C[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.X.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.b.set(this.X);
            this.c[var20].transformPosition(this.X);
            this.u[var20].transformPosition(this.b);
            this.X.lerp(this.b, K(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.X.y);
            }
         }

         this.v[var14] = this.X.x;
         this.v[var14 + 1] = this.X.y;
         this.v[var14 + 2] = this.X.z;
      }

      this.A = var22 - this.g.minimumY();

      for (byte var23 = 1; var23 < this.v.length; var23 += 5) {
         this.v[var23] = this.v[var23] - this.A;
      }

      return this.v;
   }

   public Vector3f P(boolean var1, Vector3f var2) {
      return this.u[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.A, 0.0F);
   }
}
