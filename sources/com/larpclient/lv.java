package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class lv {
   private static final Vector3f[] k = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] L = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] x = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] b = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float H = 0.002F;
   private static final float d = -0.194F;
   private static final float g = 0.9F;
   private static final float U = 0.7F;
   private final ed.q a;
   private final ed.q s;
   private final int[] P;
   private final float[] h;
   private final Matrix4f[] R = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] v = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f o = new Vector3f();
   private final Vector3f Q = new Vector3f();
   private float f;

   public lv(ed.q var1) {
      dt.s var2 = dt.Y(var1);
      this.a = var1;
      this.s = var2.mesh();
      this.P = var2.joints();
      this.h = (float[])this.s.vertices().clone();
   }

   public ed.q w() {
      return this.a;
   }

   public ed.q y() {
      return this.s;
   }

   public float[] j() {
      return this.h;
   }

   private static void g(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float U(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] Q(float var1, float var2) {
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
         g(this.R[var11], var10, k[var11], var8[var11]);
         g(this.v[var11], this.R[var11], L[var11], var12);
         g(this.R[var11 + 2], var10, x[var11], var7[var11]);
         g(this.v[var11 + 2], this.R[var11 + 2], b[var11], var9[var11]);
      }

      float[] var21 = this.s.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.P.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.P[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.o.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.Q.set(this.o);
            this.R[var20].transformPosition(this.o);
            this.v[var20].transformPosition(this.Q);
            this.o.lerp(this.Q, U(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.o.y);
            }
         }

         this.h[var14] = this.o.x;
         this.h[var14 + 1] = this.o.y;
         this.h[var14 + 2] = this.o.z;
      }

      this.f = var22 - this.s.minimumY();

      for (byte var23 = 1; var23 < this.h.length; var23 += 5) {
         this.h[var23] = this.h[var23] - this.f;
      }

      return this.h;
   }

   public Vector3f L(boolean var1, Vector3f var2) {
      return this.v[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.f, 0.0F);
   }
}
