package com.larpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ef {
   public static final int u = 1112424753;
   public static final int w = 11;
   public static final int m = 8000;
   public static final int g = 6000;
   public static final int x = 0;
   public static final int k = 1;
   public static final int i = 2;
   public static final int r = 3;
   public static final int Y = 4;
   public static final int E = 5;
   public static final int L = 6;
   public static final int W = 7;
   public static final int o = 8;
   public static final int C = 9;
   public static final int R = 10;
   private static final byte[] v = new byte[]{-1, 0, 1, 1, 3, 1, 5, 0, 7, 0, 9};
   private static final int p = 10;
   private static final float[][] d = new float[][]{
      {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {-5.0F, 2.0F, 0.0F}, {5.0F, 2.0F, 0.0F}, {-1.9F, 12.0F, 0.1F}, {1.9F, 12.0F, 0.1F}
   };
   private static final float G = 0.05859375F;
   private static final float q = 0.35F;
   private static final float U = 0.8F;
   private static final float F = 0.48F;
   private final ef.b D;
   private final float[] b;
   private final float[] n;
   private final float[] Z;
   private final Matrix4f[] j;
   private final float[] V;
   private final float[] N;
   private final float[] H;
   private final int[] a;
   private final int[] A;
   private final Matrix4f z;
   private final Matrix4f J;
   private final Vector3f c;
   private final float M;
   private float h;
   private float B;
   private boolean O;

   public static ef.b g(InputStream var0) throws IOException {
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
         if (var1.readByte() != v[var3]) {
            throw new IOException("Unexpected Banana bone hierarchy");
         }

         for (int var4 = 0; var4 < 3; var4++) {
            var2[var3 * 3 + var4] = o(var1.readFloat());
         }
      }

      float[] var17 = new float[6];

      for (int var18 = 0; var18 < var17.length; var18++) {
         var17[var18] = o(var1.readFloat());
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
               var5[var12 * 3 + var13] = o(var1.readFloat());
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
               return new ef.b(var2, var17, var5, var6, var7, var9, var8, var22, var11 - var10);
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

   private static float o(float var0) throws IOException {
      if (Float.isFinite(var0) && !(Math.abs(var0) > 3.0F)) {
         return var0;
      } else {
         throw new IOException("Invalid Banana coordinate");
      }
   }

   public static float[] U(float[] var0, float var1, float var2, float var3, float var4, float var5, boolean var6) {
      for (int var7 = 0; var7 < d.length; var7++) {
         int var8 = var7 * 10;
         Arrays.fill(var0, var8, var8 + 10, 0.0F);
         var0[var8] = d[var7][0];
         var0[var8 + 1] = d[var7][1];
         var0[var8 + 2] = d[var7][2];
      }

      float var13 = Math.clamp(var2, 0.0F, 1.0F);
      float var14 = var1 * 0.6662F;
      float var9 = (float)Math.cos(var14) * var13;
      float var10 = (float)Math.sin(var14) * var13;
      z(var0, cb.x.y, 3, Math.clamp((float)Math.toRadians(var5) * 0.5F, -0.45F, 0.45F));
      z(var0, cb.x.y, 4, Math.clamp((float)Math.toRadians(var4), -1.3F, 1.3F));
      z(var0, cb.x.j, 3, -var9 * 0.7F);
      z(var0, cb.x.m, 3, var9 * 0.7F);
      z(var0, cb.x.k, 3, var9 * 0.9F);
      z(var0, cb.x.M, 3, -var9 * 0.9F);
      z(var0, cb.x.k, 6, Math.max(0.0F, var10) * 0.9F);
      z(var0, cb.x.M, 6, Math.max(0.0F, -var10) * 0.9F);
      float var11 = (float)Math.cos(var3 * 0.09F) * 0.05F + 0.05F;
      float var12 = (float)Math.sin(var3 * 0.067F) * 0.05F;
      I(var0, cb.x.j, 5, var11);
      I(var0, cb.x.j, 3, var12);
      I(var0, cb.x.m, 5, -var11);
      I(var0, cb.x.m, 3, -var12);
      if (var6) {
         z(var0, cb.x.g, 3, 0.5F);
         I(var0, cb.x.j, 3, 0.4F);
         I(var0, cb.x.m, 3, 0.4F);
         I(var0, cb.x.k, 3, -0.8F);
         I(var0, cb.x.M, 3, -0.8F);
         I(var0, cb.x.k, 6, 0.8F);
         I(var0, cb.x.M, 6, 0.8F);
      }

      return var0;
   }

   public static void K(float[] var0, float[] var1, float var2) {
      if (!(var2 <= 0.0F)) {
         for (int var3 = 0; var3 < d.length; var3++) {
            for (int var4 = 0; var4 <= 6; var4++) {
               int var5 = var3 * 10 + var4;
               float var6 = var1[var5];
               if (Float.isNaN(var6)) {
                  if (var4 == 6 && cb.w(cb.x.values()[var3]) && k(var1)) {
                     var0[var5] -= var0[var5] * var2;
                  }
               } else {
                  var0[var5] += (var6 - var0[var5]) * var2;
               }
            }
         }
      }
   }

   private static boolean k(float[] var0) {
      for (cb.x var4 : new cb.x[]{cb.x.j, cb.x.m, cb.x.k, cb.x.M}) {
         if (!Float.isNaN(var0[var4.ordinal() * 10 + 6])) {
            return true;
         }
      }

      return false;
   }

   public static Matrix4f U(Matrix4f var0, float[] var1, float var2) {
      int var3 = cb.x.D.ordinal() * 10;
      float var4 = W(var1[var3], var2);
      float var5 = W(var1[var3 + 1], var2);
      float var6 = W(var1[var3 + 2], var2);
      return var0.translate(-var4, var5 + 0.7F, -var6)
         .rotateZ(-W(var1[var3 + 5], var2))
         .rotateY(W(var1[var3 + 4], var2))
         .rotateX(-W(var1[var3 + 3], var2))
         .translate(0.0F, -0.7F, 0.0F);
   }

   private static float W(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }

   private static void z(float[] var0, cb.x var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] = var3;
   }

   private static void I(float[] var0, cb.x var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] += var3;
   }

   public ef(ef.b var1) {
      this.b = new float[cb.w];
      this.n = new float[cb.w];
      this.Z = new float[176];
      this.j = new Matrix4f[11];
      this.z = new Matrix4f();
      this.J = new Matrix4f();
      this.c = new Vector3f();
      this.h = -1.0F;
      this.D = var1;

      for (int var2 = 0; var2 < 11; var2++) {
         this.j[var2] = new Matrix4f();
      }

      this.V = new float[var1.vertices() * 3];
      this.N = new float[var1.vertices()];
      this.H = new float[var1.indices().length * 5];
      this.a = new int[var1.indices().length];
      this.M = 0.05859375F * var1.height() / 1.8F;
      int var6 = 0;
      int[] var3 = new int[var1.vertices()];

      for (int var4 = 0; var4 < var1.vertices(); var4++) {
         byte var5 = var1.bones()[var4 * 4];
         if ((var5 == 8 || var5 == 10) && var1.weights()[var4 * 4] > 0.5F) {
            var3[var6++] = var4;
         }
      }

      this.A = Arrays.copyOf(var3, var6);
   }

   public ef.b R() {
      return this.D;
   }

   public float[] y() {
      return this.H;
   }

   public int[] a() {
      return this.a;
   }

   public float X() {
      return this.B;
   }

   public void N(float[] var1, float var2) {
      if (!this.O || var2 != this.h || !Arrays.equals(var1, 0, cb.w, this.n, 0, cb.w)) {
         System.arraycopy(var1, 0, this.b, 0, cb.w);
         System.arraycopy(var1, 0, this.n, 0, cb.w);
         this.h = var2;
         this.F();
         this.C();
         this.O = (boolean)1;
      }
   }

   private void F() {
      float var1 = (this.O(cb.x.k, 0) + this.O(cb.x.M, 0)) / 2.0F;
      float var2 = (this.O(cb.x.k, 1) + this.O(cb.x.M, 1)) / 2.0F;
      float var3 = (this.O(cb.x.k, 2) + this.O(cb.x.M, 2)) / 2.0F;
      this.j[0].translation(this.g(var1), this.L(var2), this.D(var3));
      this.j[1].set(this.j[0]).translate(this.g(this.O(cb.x.g, 0)), this.L(this.O(cb.x.g, 1)), this.D(this.O(cb.x.g, 2)));
      this.X(this.j[1], 1, this.W(cb.x.g));
      this.m(2, cb.x.y, 0.0F, 0.0F, 0.0F);
      this.m(3, cb.x.j, d[2][0], d[2][1], d[2][2]);
      this.m(5, cb.x.m, d[3][0], d[3][1], d[3][2]);
      this.Y(4, 3, cb.x.j);
      this.Y(6, 5, cb.x.m);

      for (int var4 = 0; var4 < 2; var4++) {
         cb.x var5 = var4 == 0 ? cb.x.k : cb.x.M;
         int var6 = var4 == 0 ? 7 : 9;
         this.j[var6].set(this.j[0]).translate(this.g(this.O(var5, 0) - var1), this.L(this.O(var5, 1) - var2), this.D(this.O(var5, 2) - var3));
         this.X(this.j[var6], var6, this.W(var5));
         this.Y(var6 + 1, var6, var5);
      }

      for (int var7 = 0; var7 < 11; var7++) {
         this.j[var7].get(this.Z, var7 * 16);
      }
   }

   private void X(Matrix4f var1, int var2, Matrix4f var3) {
      float var4 = this.D.pivots()[var2 * 3];
      float var5 = this.D.pivots()[var2 * 3 + 1];
      float var6 = this.D.pivots()[var2 * 3 + 2];
      var1.translate(var4, var5, var6).mul(var3).translate(-var4, -var5, -var6);
   }

   private void m(int var1, cb.x var2, float var3, float var4, float var5) {
      int var6 = cb.x.g.ordinal() * 10;
      int var7 = var2.ordinal() * 10;
      this.c.set(var3, var4, var5);
      this.z.identity().rotateZ(this.b[var6 + 5]).rotateY(this.b[var6 + 4]).rotateX(this.b[var6 + 3]);
      this.z.transformDirection(this.c).add(this.b[var6], this.b[var6 + 1], this.b[var6 + 2]);
      float var8 = this.b[var7] - this.c.x;
      float var9 = this.b[var7 + 1] - this.c.y;
      float var10 = this.b[var7 + 2] - this.c.z;
      this.c.set(this.D.pivots()[var1 * 3], this.D.pivots()[var1 * 3 + 1], this.D.pivots()[var1 * 3 + 2]);
      this.j[1].transformPosition(this.c).add(this.g(var8), this.L(var9), this.D(var10));
      this.j[var1].translation(this.c).mul(this.W(var2));
      this.j[var1].translate(-this.D.pivots()[var1 * 3], -this.D.pivots()[var1 * 3 + 1], -this.D.pivots()[var1 * 3 + 2]);
   }

   private void Y(int var1, int var2, cb.x var3) {
      this.j[var1].set(this.j[var2]);
      this.X(this.j[var1], var1, this.J.identity().rotateX(this.b[var3.ordinal() * 10 + 6]));
   }

   private Matrix4f W(cb.x var1) {
      int var2 = var1.ordinal() * 10;
      return this.z.identity().rotateZ(-this.b[var2 + 5]).rotateY(-this.b[var2 + 4]).rotateX(this.b[var2 + 3]);
   }

   private float O(cb.x var1, int var2) {
      return this.b[var1.ordinal() * 10 + var2] - d[var1.ordinal()][var2];
   }

   private float g(float var1) {
      return var1 * this.M;
   }

   private float L(float var1) {
      return -var1 * this.M;
   }

   private float D(float var1) {
      return -var1 * this.M;
   }

   private void C() {
      float[] var1 = this.D.positions();
      float[] var2 = this.D.normals();
      float[] var3 = this.D.weights();
      float[] var4 = this.Z;
      byte[] var5 = this.D.bones();
      float var6 = (float)Math.sqrt(0.9929F);
      int var7 = 0;

      for (int var8 = this.D.vertices(); var7 < var8; var7++) {
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

         this.V[var7 * 3] = var15;
         this.V[var7 * 3 + 1] = var16;
         this.V[var7 * 3 + 2] = var17;
         float var35 = (float)Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
         float var36 = var35 < 1.0E-6F ? 0.0F : (var18 * 0.35F + var19 * 0.8F + var20 * 0.48F) / (var35 * var6);
         this.N[var7] = 0.7F + 0.3F * Math.max(0.0F, var36);
      }

      float var24 = Float.POSITIVE_INFINITY;

      for (int var31 : this.A) {
         var24 = Math.min(var24, this.V[var31 * 3 + 1]);
      }

      this.B = this.A.length == 0 ? 0.0F : var24 * this.h;
      int[] var26 = this.D.indices();
      float[] var28 = this.D.uvs();

      for (int var30 = 0; var30 < var26.length; var30++) {
         int var32 = var26[var30];
         int var33 = var30 * 5;
         this.H[var33] = this.V[var32 * 3];
         this.H[var33 + 1] = this.V[var32 * 3 + 1] - this.B;
         this.H[var33 + 2] = this.V[var32 * 3 + 2];
         this.H[var33 + 3] = var28[var32 * 2];
         this.H[var33 + 4] = var28[var32 * 2 + 1];
         int var34 = Math.round(this.N[var32] * 255.0F);
         this.a[var30] = 0xFF000000 | var34 << 16 | var34 << 8 | var34;
      }
   }

   public Vector3f d(int var1, Vector3f var2) {
      return this.j[var1].transformPosition(var2).sub(0.0F, this.B, 0.0F);
   }

   public Vector3f W(boolean var1) {
      return this.d(var1 ? 4 : 6, this.D.palm(var1));
   }

   public record b(float[] pivots, float[] palms, float[] positions, float[] normals, float[] uvs, byte[] bones, float[] weights, int[] indices, float height) {
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
