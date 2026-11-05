package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class mk {
   private static final Vector3f[] j = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] K = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] O = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] G = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float w = 0.002F;
   private static final float B = -0.194F;
   private static final float k = 0.9F;
   private static final float Y = 0.7F;
   private final _y.w P;
   private final _y.w f;
   private final int[] c;
   private final float[] N;
   private final Matrix4f[] n = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] h = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f C = new Vector3f();
   private final Vector3f L = new Vector3f();
   private float b;

   public mk(_y.w var1) {
      kz.l var2 = kz.D(var1);
      this.P = var1;
      this.f = var2.mesh();
      this.c = var2.joints();
      this.N = (float[])this.f.vertices().clone();
   }

   public _y.w L() {
      return this.P;
   }

   public _y.w b() {
      return this.f;
   }

   public float[] Y() {
      return this.N;
   }

   private static void e(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float A(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] L(float var1, float var2) {
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
         e(this.n[var11], var10, j[var11], var8[var11]);
         e(this.h[var11], this.n[var11], K[var11], var12);
         e(this.n[var11 + 2], var10, O[var11], var7[var11]);
         e(this.h[var11 + 2], this.n[var11 + 2], G[var11], var9[var11]);
      }

      float[] var21 = this.f.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.c.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.c[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.C.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.L.set(this.C);
            this.n[var20].transformPosition(this.C);
            this.h[var20].transformPosition(this.L);
            this.C.lerp(this.L, A(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.C.y);
            }
         }

         this.N[var14] = this.C.x;
         this.N[var14 + 1] = this.C.y;
         this.N[var14 + 2] = this.C.z;
      }

      this.b = var22 - this.f.minimumY();

      for (byte var23 = 1; var23 < this.N.length; var23 += 5) {
         this.N[var23] = this.N[var23] - this.b;
      }

      return this.N;
   }

   public Vector3f w(boolean var1, Vector3f var2) {
      return this.h[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.b, 0.0F);
   }
}
