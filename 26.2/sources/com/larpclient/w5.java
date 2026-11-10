package com.larpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class w5 {
   public static final int z = 1112424753;
   public static final int Z = 11;
   public static final int A = 8000;
   public static final int F = 6000;
   public static final int h = 0;
   public static final int d = 1;
   public static final int p = 2;
   public static final int V = 3;
   public static final int u = 4;
   public static final int b = 5;
   public static final int M = 6;
   public static final int X = 7;
   public static final int s = 8;
   public static final int J = 9;
   public static final int W = 10;
   private static final byte[] B = new byte[]{-1, 0, 1, 1, 3, 1, 5, 0, 7, 0, 9};
   private static final int L = 10;
   private static final float[][] P = new float[][]{
      {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {-5.0F, 2.0F, 0.0F}, {5.0F, 2.0F, 0.0F}, {-1.9F, 12.0F, 0.1F}, {1.9F, 12.0F, 0.1F}
   };
   private static final float f = 0.05859375F;
   private static final float n = 0.35F;
   private static final float R = 0.8F;
   private static final float Y = 0.48F;
   private final w5.o m;
   private final float[] c = new float[cx.B];
   private final float[] C = new float[cx.B];
   private final float[] q = new float[176];
   private final Matrix4f[] N = new Matrix4f[11];
   private final float[] y;
   private final float[] E;
   private final float[] r;
   private final int[] l;
   private final int[] v;
   private final Matrix4f k = new Matrix4f();
   private final Matrix4f G = new Matrix4f();
   private final Vector3f g = new Vector3f();
   private final float x;
   private float t = -1.0F;
   private float i;
   private boolean I;

   public static w5.o y(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Banana mesh missing");
      }

      DataInputStream var1 = new DataInputStream(var0);
      if (var1.readInt() != 1112424753) {
         throw new IOException("Invalid Banana mesh header");
      }

      if (var1.readInt() != 11) {
         throw new IOException("Unexpected Banana bone count");
      }

      float[] var2 = new float[33];

      for (int var3 = 0; var3 < 11; var3++) {
         if (var1.readByte() != B[var3]) {
            throw new IOException("Unexpected Banana bone hierarchy");
         }

         for (int var4 = 0; var4 < 3; var4++) {
            var2[var3 * 3 + var4] = B(var1.readFloat());
         }
      }

      float[] var17 = new float[6];

      for (int var18 = 0; var18 < var17.length; var18++) {
         var17[var18] = B(var1.readFloat());
      }

      int var19 = var1.readInt();
      if (var19 >= 3 && var19 <= 8000) {
         float[] var5 = new float[var19 * 3];
         float[] var6 = new float[var19 * 3];
         float[] var7 = new float[var19 * 2];
         float[] var8 = new float[var19 * 4];
         byte[] var9 = new byte[var19 * 4];
         float var10 = Float.POSITIVE_INFINITY;
         float var11 = Float.NEGATIVE_INFINITY;

         for (int var12 = 0; var12 < var19; var12++) {
            for (int var13 = 0; var13 < 3; var13++) {
               var5[var12 * 3 + var13] = B(var1.readFloat());
            }

            var10 = Math.min(var10, var5[var12 * 3 + 1]);
            var11 = Math.max(var11, var5[var12 * 3 + 1]);
            float var21 = 0.0F;

            for (int var14 = 0; var14 < 3; var14++) {
               float var15 = var1.readFloat();
               if (!Float.isFinite(var15)) {
                  throw new IOException("Invalid Banana normal");
               }

               var6[var12 * 3 + var14] = var15;
               var21 += var15 * var15;
            }

            if (Math.abs(var21 - 1.0F) > 0.01F) {
               throw new IOException("Banana normal is not unit length");
            }

            for (int var23 = 0; var23 < 2; var23++) {
               float var27 = var1.readFloat();
               if (!Float.isFinite(var27) || var27 < 0.0F || var27 > 1.0F) {
                  throw new IOException("Invalid Banana texture coordinate");
               }

               var7[var12 * 2 + var23] = var27;
            }

            for (int var24 = 0; var24 < 4; var24++) {
               int var28 = var1.readUnsignedByte();
               if (var28 >= 11) {
                  throw new IOException("Invalid Banana bone index");
               }

               var9[var12 * 4 + var24] = (byte)var28;
            }

            int var25 = 0;

            for (int var29 = 0; var29 < 4; var29++) {
               int var16 = var1.readUnsignedByte();
               var25 += var16;
               var8[var12 * 4 + var29] = var16 / 255.0F;
            }

            if (var25 != 255) {
               throw new IOException("Banana weights must total 255");
            }
         }

         int var20 = var1.readInt();
         if (var20 >= 3 && var20 % 3 == 0 && var20 <= 18000) {
            int[] var22 = new int[var20];

            for (int var26 = 0; var26 < var20; var26++) {
               int var30 = var1.readUnsignedShort();
               if (var30 >= var19) {
                  throw new IOException("Banana index out of range");
               }

               var22[var26] = var30;
            }

            if (var1.read() != -1) {
               throw new IOException("Unexpected Banana mesh trailer");
            } else if (!(Math.abs(var10) > 0.001F) && !(var11 - var10 < 0.5F)) {
               return new w5.o(var2, var17, var5, var6, var7, var9, var8, var22, var11 - var10);
            } else {
               throw new IOException("Banana mesh must stand on y=0");
            }
         } else {
            throw new IOException("Invalid Banana triangle budget");
         }
      } else {
         throw new IOException("Invalid Banana vertex budget");
      }
   }

   private static float B(float var0) throws IOException {
      if (Float.isFinite(var0) && !(Math.abs(var0) > 3.0F)) {
         return var0;
      } else {
         throw new IOException("Invalid Banana coordinate");
      }
   }

   public static float[] q(float[] var0, float var1, float var2, float var3, float var4, float var5, boolean var6) {
      for (int var7 = 0; var7 < P.length; var7++) {
         int var8 = var7 * 10;
         Arrays.fill(var0, var8, var8 + 10, 0.0F);
         var0[var8] = P[var7][0];
         var0[var8 + 1] = P[var7][1];
         var0[var8 + 2] = P[var7][2];
      }

      float var13 = Math.clamp(var2, 0.0F, 1.0F);
      float var14 = var1 * 0.6662F;
      float var9 = (float)Math.cos(var14) * var13;
      float var10 = (float)Math.sin(var14) * var13;
      J(var0, cx.q.P, 3, Math.clamp((float)Math.toRadians(var5) * 0.5F, -0.45F, 0.45F));
      J(var0, cx.q.P, 4, Math.clamp((float)Math.toRadians(var4), -1.3F, 1.3F));
      J(var0, cx.q.Q, 3, -var9 * 0.7F);
      J(var0, cx.q.q, 3, var9 * 0.7F);
      J(var0, cx.q.l, 3, var9 * 0.9F);
      J(var0, cx.q.A, 3, -var9 * 0.9F);
      J(var0, cx.q.l, 6, Math.max(0.0F, var10) * 0.9F);
      J(var0, cx.q.A, 6, Math.max(0.0F, -var10) * 0.9F);
      float var11 = (float)Math.cos(var3 * 0.09F) * 0.05F + 0.05F;
      float var12 = (float)Math.sin(var3 * 0.067F) * 0.05F;
      Z(var0, cx.q.Q, 5, var11);
      Z(var0, cx.q.Q, 3, var12);
      Z(var0, cx.q.q, 5, -var11);
      Z(var0, cx.q.q, 3, -var12);
      if (var6) {
         J(var0, cx.q.E, 3, 0.5F);
         Z(var0, cx.q.Q, 3, 0.4F);
         Z(var0, cx.q.q, 3, 0.4F);
         Z(var0, cx.q.l, 3, -0.8F);
         Z(var0, cx.q.A, 3, -0.8F);
         Z(var0, cx.q.l, 6, 0.8F);
         Z(var0, cx.q.A, 6, 0.8F);
      }

      return var0;
   }

   public static void Y(float[] var0, float[] var1, float var2) {
      if (!(var2 <= 0.0F)) {
         for (int var3 = 0; var3 < P.length; var3++) {
            for (int var4 = 0; var4 <= 6; var4++) {
               int var5 = var3 * 10 + var4;
               float var6 = var1[var5];
               if (Float.isNaN(var6)) {
                  if (var4 == 6 && cx.S(cx.q.values()[var3]) && y(var1)) {
                     var0[var5] -= var0[var5] * var2;
                  }
               } else {
                  var0[var5] += (var6 - var0[var5]) * var2;
               }
            }
         }
      }
   }

   private static boolean y(float[] var0) {
      for (cx.q var4 : new cx.q[]{cx.q.Q, cx.q.q, cx.q.l, cx.q.A}) {
         if (!Float.isNaN(var0[var4.ordinal() * 10 + 6])) {
            return true;
         }
      }

      return false;
   }

   public static Matrix4f z(Matrix4f var0, float[] var1, float var2) {
      int var3 = cx.q.m.ordinal() * 10;
      float var4 = S(var1[var3], var2);
      float var5 = S(var1[var3 + 1], var2);
      float var6 = S(var1[var3 + 2], var2);
      return var0.translate(-var4, var5 + 0.7F, -var6)
         .rotateZ(-S(var1[var3 + 5], var2))
         .rotateY(S(var1[var3 + 4], var2))
         .rotateX(-S(var1[var3 + 3], var2))
         .translate(0.0F, -0.7F, 0.0F);
   }

   private static float S(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }

   private static void J(float[] var0, cx.q var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] = var3;
   }

   private static void Z(float[] var0, cx.q var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] += var3;
   }

   public w5(w5.o var1) {
      this.m = var1;

      for (int var2 = 0; var2 < 11; var2++) {
         this.N[var2] = new Matrix4f();
      }

      this.y = new float[var1.vertices() * 3];
      this.E = new float[var1.vertices()];
      this.r = new float[var1.indices().length * 5];
      this.l = new int[var1.indices().length];
      this.x = 0.05859375F * var1.height() / 1.8F;
      int var6 = 0;
      int[] var3 = new int[var1.vertices()];

      for (int var4 = 0; var4 < var1.vertices(); var4++) {
         byte var5 = var1.bones()[var4 * 4];
         if ((var5 == 8 || var5 == 10) && var1.weights()[var4 * 4] > 0.5F) {
            var3[var6++] = var4;
         }
      }

      this.v = Arrays.copyOf(var3, var6);
   }

   public w5.o W() {
      return this.m;
   }

   public float[] K() {
      return this.r;
   }

   public int[] m() {
      return this.l;
   }

   public float j() {
      return this.i;
   }

   public void n(float[] var1, float var2) {
      if (!this.I || var2 != this.t || !Arrays.equals(var1, 0, cx.B, this.C, 0, cx.B)) {
         System.arraycopy(var1, 0, this.c, 0, cx.B);
         System.arraycopy(var1, 0, this.C, 0, cx.B);
         this.t = var2;
         this.z();
         this.T();
         this.I = (boolean)1;
      }
   }

   private void z() {
      float var1 = (this.m(cx.q.l, 0) + this.m(cx.q.A, 0)) / 2.0F;
      float var2 = (this.m(cx.q.l, 1) + this.m(cx.q.A, 1)) / 2.0F;
      float var3 = (this.m(cx.q.l, 2) + this.m(cx.q.A, 2)) / 2.0F;
      this.N[0].translation(this.j(var1), this.u(var2), this.Z(var3));
      this.N[1].set(this.N[0]).translate(this.j(this.m(cx.q.E, 0)), this.u(this.m(cx.q.E, 1)), this.Z(this.m(cx.q.E, 2)));
      this.A(this.N[1], 1, this.K(cx.q.E));
      this.A(2, cx.q.P, 0.0F, 0.0F, 0.0F);
      this.A(3, cx.q.Q, P[2][0], P[2][1], P[2][2]);
      this.A(5, cx.q.q, P[3][0], P[3][1], P[3][2]);
      this.a(4, 3, cx.q.Q);
      this.a(6, 5, cx.q.q);

      for (int var4 = 0; var4 < 2; var4++) {
         cx.q var5 = var4 == 0 ? cx.q.l : cx.q.A;
         int var6 = var4 == 0 ? 7 : 9;
         this.N[var6].set(this.N[0]).translate(this.j(this.m(var5, 0) - var1), this.u(this.m(var5, 1) - var2), this.Z(this.m(var5, 2) - var3));
         this.A(this.N[var6], var6, this.K(var5));
         this.a(var6 + 1, var6, var5);
      }

      for (int var7 = 0; var7 < 11; var7++) {
         this.N[var7].get(this.q, var7 * 16);
      }
   }

   private void A(Matrix4f var1, int var2, Matrix4f var3) {
      float var4 = this.m.pivots()[var2 * 3];
      float var5 = this.m.pivots()[var2 * 3 + 1];
      float var6 = this.m.pivots()[var2 * 3 + 2];
      var1.translate(var4, var5, var6).mul(var3).translate(-var4, -var5, -var6);
   }

   private void A(int var1, cx.q var2, float var3, float var4, float var5) {
      int var6 = cx.q.E.ordinal() * 10;
      int var7 = var2.ordinal() * 10;
      this.g.set(var3, var4, var5);
      this.k.identity().rotateZ(this.c[var6 + 5]).rotateY(this.c[var6 + 4]).rotateX(this.c[var6 + 3]);
      this.k.transformDirection(this.g).add(this.c[var6], this.c[var6 + 1], this.c[var6 + 2]);
      float var8 = this.c[var7] - this.g.x;
      float var9 = this.c[var7 + 1] - this.g.y;
      float var10 = this.c[var7 + 2] - this.g.z;
      this.g.set(this.m.pivots()[var1 * 3], this.m.pivots()[var1 * 3 + 1], this.m.pivots()[var1 * 3 + 2]);
      this.N[1].transformPosition(this.g).add(this.j(var8), this.u(var9), this.Z(var10));
      this.N[var1].translation(this.g).mul(this.K(var2));
      this.N[var1].translate(-this.m.pivots()[var1 * 3], -this.m.pivots()[var1 * 3 + 1], -this.m.pivots()[var1 * 3 + 2]);
   }

   private void a(int var1, int var2, cx.q var3) {
      this.N[var1].set(this.N[var2]);
      this.A(this.N[var1], var1, this.G.identity().rotateX(this.c[var3.ordinal() * 10 + 6]));
   }

   private Matrix4f K(cx.q var1) {
      int var2 = var1.ordinal() * 10;
      return this.k.identity().rotateZ(-this.c[var2 + 5]).rotateY(-this.c[var2 + 4]).rotateX(this.c[var2 + 3]);
   }

   private float m(cx.q var1, int var2) {
      return this.c[var1.ordinal() * 10 + var2] - P[var1.ordinal()][var2];
   }

   private float j(float var1) {
      return var1 * this.x;
   }

   private float u(float var1) {
      return -var1 * this.x;
   }

   private float Z(float var1) {
      return -var1 * this.x;
   }

   private void T() {
      float[] var1 = this.m.positions();
      float[] var2 = this.m.normals();
      float[] var3 = this.m.weights();
      float[] var4 = this.q;
      byte[] var5 = this.m.bones();
      float var6 = (float)Math.sqrt(0.9929F);
      int var7 = 0;

      for (int var8 = this.m.vertices(); var7 < var8; var7++) {
         float var9 = var1[var7 * 3];
         float var10 = var1[var7 * 3 + 1];
         float var11 = var1[var7 * 3 + 2];
         float var12 = var2[var7 * 3];
         float var13 = var2[var7 * 3 + 1];
         float var14 = var2[var7 * 3 + 2];
         float var15 = 0.0F;
         float var16 = 0.0F;
         float var17 = 0.0F;
         float var18 = 0.0F;
         float var19 = 0.0F;
         float var20 = 0.0F;

         for (int var21 = 0; var21 < 4; var21++) {
            float var22 = var3[var7 * 4 + var21];
            if (var22 != 0.0F) {
               int var23 = var5[var7 * 4 + var21] * 16;
               var15 += var22 * (var4[var23] * var9 + var4[var23 + 4] * var10 + var4[var23 + 8] * var11 + var4[var23 + 12]);
               var16 += var22 * (var4[var23 + 1] * var9 + var4[var23 + 5] * var10 + var4[var23 + 9] * var11 + var4[var23 + 13]);
               var17 += var22 * (var4[var23 + 2] * var9 + var4[var23 + 6] * var10 + var4[var23 + 10] * var11 + var4[var23 + 14]);
               var18 += var22 * (var4[var23] * var12 + var4[var23 + 4] * var13 + var4[var23 + 8] * var14);
               var19 += var22 * (var4[var23 + 1] * var12 + var4[var23 + 5] * var13 + var4[var23 + 9] * var14);
               var20 += var22 * (var4[var23 + 2] * var12 + var4[var23 + 6] * var13 + var4[var23 + 10] * var14);
            }
         }

         this.y[var7 * 3] = var15;
         this.y[var7 * 3 + 1] = var16;
         this.y[var7 * 3 + 2] = var17;
         float var35 = (float)Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
         float var36 = var35 < 1.0E-6F ? 0.0F : (var18 * 0.35F + var19 * 0.8F + var20 * 0.48F) / (var35 * var6);
         this.E[var7] = 0.7F + 0.3F * Math.max(0.0F, var36);
      }

      float var24 = Float.POSITIVE_INFINITY;

      for (int var31 : this.v) {
         var24 = Math.min(var24, this.y[var31 * 3 + 1]);
      }

      this.i = this.v.length == 0 ? 0.0F : var24 * this.t;
      int[] var26 = this.m.indices();
      float[] var28 = this.m.uvs();

      for (int var30 = 0; var30 < var26.length; var30++) {
         int var32 = var26[var30];
         int var33 = var30 * 5;
         this.r[var33] = this.y[var32 * 3];
         this.r[var33 + 1] = this.y[var32 * 3 + 1] - this.i;
         this.r[var33 + 2] = this.y[var32 * 3 + 2];
         this.r[var33 + 3] = var28[var32 * 2];
         this.r[var33 + 4] = var28[var32 * 2 + 1];
         int var34 = Math.round(this.E[var32] * 255.0F);
         this.l[var30] = 0xFF000000 | var34 << 16 | var34 << 8 | var34;
      }
   }

   public Vector3f K(int var1, Vector3f var2) {
      return this.N[var1].transformPosition(var2).sub(0.0F, this.i, 0.0F);
   }

   public Vector3f u(boolean var1) {
      return this.K(var1 ? 4 : 6, this.m.palm(var1));
   }

   public record o(float[] pivots, float[] palms, float[] positions, float[] normals, float[] uvs, byte[] bones, float[] weights, int[] indices, float height) {
      public int vertices() {
         return this.positions.length / 3;
      }

      public int triangles() {
         return this.indices.length / 3;
      }

      public Vector3f pivot(int var1) {
         return new Vector3f(this.pivots[var1 * 3], this.pivots[var1 * 3 + 1], this.pivots[var1 * 3 + 2]);
      }

      public Vector3f palm(boolean var1) {
         int var2 = var1 ? 0 : 3;
         return new Vector3f(this.palms[var2], this.palms[var2 + 1], this.palms[var2 + 2]);
      }
   }
}
