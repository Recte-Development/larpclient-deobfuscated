package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class g6 {
   private static final Vector3f[] g = new Vector3f[]{new Vector3f(-0.031F, 0.08F, -0.067F), new Vector3f(0.088F, 0.08F, -0.067F)};
   private static final Vector3f[] B = new Vector3f[]{new Vector3f(-0.038F, 0.002F, -0.068F), new Vector3f(0.096F, 0.002F, -0.068F)};
   private static final Vector3f[] w = new Vector3f[]{new Vector3f(0.001F, -0.105F, -0.062F), new Vector3f(0.055F, -0.105F, -0.062F)};
   private static final Vector3f[] y = new Vector3f[]{new Vector3f(-0.001F, -0.194F, -0.067F), new Vector3f(0.057F, -0.194F, -0.067F)};
   private static final float Y = 0.002F;
   private static final float S = -0.194F;
   private static final float H = 0.9F;
   private static final float r = 0.7F;
   private final tt.r i;
   private final tt.r s;
   private final int[] l;
   private final float[] a;
   private final Matrix4f[] R = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Matrix4f[] h = new Matrix4f[]{new Matrix4f(), new Matrix4f(), new Matrix4f(), new Matrix4f()};
   private final Vector3f q = new Vector3f();
   private final Vector3f u = new Vector3f();
   private float P;

   public g6(tt.r var1) {
      l.i var2 = com.larpclient.l.J(var1);
      this.i = var1;
      this.s = var2.mesh();
      this.l = var2.joints();
      this.a = (float[])this.s.vertices().clone();
   }

   public tt.r a() {
      return this.i;
   }

   public tt.r Y() {
      return this.s;
   }

   public float[] X() {
      return this.a;
   }

   private static void U(Matrix4f var0, Matrix4f var1, Vector3f var2, float var3) {
      var0.set(var1).translate(var2).rotateX(var3).translate(-var2.x, -var2.y, -var2.z);
   }

   private static float Z(float var0) {
      var0 = Math.clamp(var0, 0.0F, 1.0F);
      return var0 * var0 * (3.0F - 2.0F * var0);
   }

   public float[] f(float var1, float var2) {
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
         U(this.R[var11], var10, g[var11], var8[var11]);
         U(this.h[var11], this.R[var11], B[var11], var12);
         U(this.R[var11 + 2], var10, w[var11], var7[var11]);
         U(this.h[var11 + 2], this.R[var11 + 2], y[var11], var9[var11]);
      }

      float[] var21 = this.s.vertices();
      float var22 = Float.POSITIVE_INFINITY;

      for (int var13 = 0; var13 < this.l.length; var13++) {
         int var14 = var13 * 5;
         int var15 = this.l[var13];
         float var16 = var21[var14];
         float var17 = var21[var14 + 1];
         float var18 = var21[var14 + 2];
         this.q.set(var16, var17, var18);
         if (var15 > 0) {
            int var19 = var15 >= 3 ? 1 : 0;
            int var20 = (var15 - 1) % 2 + (var19 != 0 ? 2 : 0);
            this.u.set(this.q);
            this.R[var20].transformPosition(this.q);
            this.h[var20].transformPosition(this.u);
            this.q.lerp(this.u, Z(((var19 != 0 ? -0.194F : 0.002F) + 0.013F - var17) / 0.026F));
            if (var19 != 0) {
               var22 = Math.min(var22, this.q.y);
            }
         }

         this.a[var14] = this.q.x;
         this.a[var14 + 1] = this.q.y;
         this.a[var14 + 2] = this.q.z;
      }

      this.P = var22 - this.s.minimumY();

      for (byte var23 = 1; var23 < this.a.length; var23 += 5) {
         this.a[var23] = this.a[var23] - this.P;
      }

      return this.a;
   }

   public Vector3f x(boolean var1, Vector3f var2) {
      return this.h[var1 ? 0 : 1].transformPosition(var2).sub(0.0F, this.P, 0.0F);
   }
}
