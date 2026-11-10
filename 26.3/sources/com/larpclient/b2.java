package com.larpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class b2 {
   public static final int d = 1112424753;
   public static final int b = 11;
   public static final int C = 8000;
   public static final int m = 6000;
   public static final int D = 0;
   public static final int l = 1;
   public static final int o = 2;
   public static final int G = 3;
   public static final int e = 4;
   public static final int R = 5;
   public static final int y = 6;
   public static final int w = 7;
   public static final int k = 8;
   public static final int r = 9;
   public static final int K = 10;
   private static final byte[] H = new byte[]{-1, 0, 1, 1, 3, 1, 5, 0, 7, 0, 9};
   private static final int E = 10;
   private static final float[][] t = new float[][]{
      {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {-5.0F, 2.0F, 0.0F}, {5.0F, 2.0F, 0.0F}, {-1.9F, 12.0F, 0.1F}, {1.9F, 12.0F, 0.1F}
   };
   private static final float v = 0.05859375F;
   private static final float U = 0.35F;
   private static final float Z = 0.8F;
   private static final float j = 0.48F;
   private final b2.m M;
   private final float[] I = new float[bo.s];
   private final float[] T = new float[bo.s];
   private final float[] L = new float[176];
   private final Matrix4f[] S = new Matrix4f[11];
   private final float[] i;
   private final float[] N;
   private final float[] s;
   private final int[] J;
   private final int[] f;
   private final Matrix4f F = new Matrix4f();
   private final Matrix4f p = new Matrix4f();
   private final Vector3f c = new Vector3f();
   private final float x;
   private float n = -1.0F;
   private float q;
   private boolean u;

   public static b2.m z(InputStream var0) throws IOException {
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
         if (var1.readByte() != H[var3]) {
            throw new IOException("Unexpected Banana bone hierarchy");
         }

         for (int var4 = 0; var4 < 3; var4++) {
            var2[var3 * 3 + var4] = Y(var1.readFloat());
         }
      }

      float[] var17 = new float[6];

      for (int var18 = 0; var18 < var17.length; var18++) {
         var17[var18] = Y(var1.readFloat());
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
               var5[var12 * 3 + var13] = Y(var1.readFloat());
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
               return new b2.m(var2, var17, var5, var6, var7, var9, var8, var22, var11 - var10);
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

   private static float Y(float var0) throws IOException {
      if (Float.isFinite(var0) && !(Math.abs(var0) > 3.0F)) {
         return var0;
      } else {
         throw new IOException("Invalid Banana coordinate");
      }
   }

   public static float[] m(float[] var0, float var1, float var2, float var3, float var4, float var5, boolean var6) {
      for (int var7 = 0; var7 < t.length; var7++) {
         int var8 = var7 * 10;
         Arrays.fill(var0, var8, var8 + 10, 0.0F);
         var0[var8] = t[var7][0];
         var0[var8 + 1] = t[var7][1];
         var0[var8 + 2] = t[var7][2];
      }

      float var13 = Math.clamp(var2, 0.0F, 1.0F);
      float var14 = var1 * 0.6662F;
      float var9 = (float)Math.cos(var14) * var13;
      float var10 = (float)Math.sin(var14) * var13;
      e(var0, bo.i.b, 3, Math.clamp((float)Math.toRadians(var5) * 0.5F, -0.45F, 0.45F));
      e(var0, bo.i.b, 4, Math.clamp((float)Math.toRadians(var4), -1.3F, 1.3F));
      e(var0, bo.i.y, 3, -var9 * 0.7F);
      e(var0, bo.i.u, 3, var9 * 0.7F);
      e(var0, bo.i.E, 3, var9 * 0.9F);
      e(var0, bo.i.Z, 3, -var9 * 0.9F);
      e(var0, bo.i.E, 6, Math.max(0.0F, var10) * 0.9F);
      e(var0, bo.i.Z, 6, Math.max(0.0F, -var10) * 0.9F);
      float var11 = (float)Math.cos(var3 * 0.09F) * 0.05F + 0.05F;
      float var12 = (float)Math.sin(var3 * 0.067F) * 0.05F;
      V(var0, bo.i.y, 5, var11);
      V(var0, bo.i.y, 3, var12);
      V(var0, bo.i.u, 5, -var11);
      V(var0, bo.i.u, 3, -var12);
      if (var6) {
         e(var0, bo.i.L, 3, 0.5F);
         V(var0, bo.i.y, 3, 0.4F);
         V(var0, bo.i.u, 3, 0.4F);
         V(var0, bo.i.E, 3, -0.8F);
         V(var0, bo.i.Z, 3, -0.8F);
         V(var0, bo.i.E, 6, 0.8F);
         V(var0, bo.i.Z, 6, 0.8F);
      }

      return var0;
   }

   public static void k(float[] var0, float[] var1, float var2) {
      if (!(var2 <= 0.0F)) {
         for (int var3 = 0; var3 < t.length; var3++) {
            for (int var4 = 0; var4 <= 6; var4++) {
               int var5 = var3 * 10 + var4;
               float var6 = var1[var5];
               if (Float.isNaN(var6)) {
                  if (var4 == 6 && bo.q(bo.i.values()[var3]) && d(var1)) {
                     var0[var5] -= var0[var5] * var2;
                  }
               } else {
                  var0[var5] += (var6 - var0[var5]) * var2;
               }
            }
         }
      }
   }

   private static boolean d(float[] var0) {
      for (bo.i var4 : new bo.i[]{bo.i.y, bo.i.u, bo.i.E, bo.i.Z}) {
         if (!Float.isNaN(var0[var4.ordinal() * 10 + 6])) {
            return true;
         }
      }

      return false;
   }

   public static Matrix4f F(Matrix4f var0, float[] var1, float var2) {
      int var3 = bo.i.W.ordinal() * 10;
      float var4 = A(var1[var3], var2);
      float var5 = A(var1[var3 + 1], var2);
      float var6 = A(var1[var3 + 2], var2);
      return var0.translate(-var4, var5 + 0.7F, -var6)
         .rotateZ(-A(var1[var3 + 5], var2))
         .rotateY(A(var1[var3 + 4], var2))
         .rotateX(-A(var1[var3 + 3], var2))
         .translate(0.0F, -0.7F, 0.0F);
   }

   private static float A(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }

   private static void e(float[] var0, bo.i var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] = var3;
   }

   private static void V(float[] var0, bo.i var1, int var2, float var3) {
      var0[var1.ordinal() * 10 + var2] += var3;
   }

   public b2(b2.m var1) {
      this.M = var1;

      for (int var2 = 0; var2 < 11; var2++) {
         this.S[var2] = new Matrix4f();
      }

      this.i = new float[var1.vertices() * 3];
      this.N = new float[var1.vertices()];
      this.s = new float[var1.indices().length * 5];
      this.J = new int[var1.indices().length];
      this.x = 0.05859375F * var1.height() / 1.8F;
      int var6 = 0;
      int[] var3 = new int[var1.vertices()];

      for (int var4 = 0; var4 < var1.vertices(); var4++) {
         byte var5 = var1.bones()[var4 * 4];
         if ((var5 == 8 || var5 == 10) && var1.weights()[var4 * 4] > 0.5F) {
            var3[var6++] = var4;
         }
      }

      this.f = Arrays.copyOf(var3, var6);
   }

   public b2.m O() {
      return this.M;
   }

   public float[] H() {
      return this.s;
   }

   public int[] o() {
      return this.J;
   }

   public float i() {
      return this.q;
   }

   public void h(float[] var1, float var2) {
      if (!this.u || var2 != this.n || !Arrays.equals(var1, 0, bo.s, this.T, 0, bo.s)) {
         System.arraycopy(var1, 0, this.I, 0, bo.s);
         System.arraycopy(var1, 0, this.T, 0, bo.s);
         this.n = var2;
         this.v();
         this.r();
         this.u = (boolean)1;
      }
   }

   private void v() {
      float var1 = (this.I(bo.i.E, 0) + this.I(bo.i.Z, 0)) / 2.0F;
      float var2 = (this.I(bo.i.E, 1) + this.I(bo.i.Z, 1)) / 2.0F;
      float var3 = (this.I(bo.i.E, 2) + this.I(bo.i.Z, 2)) / 2.0F;
      this.S[0].translation(this.F(var1), this.d(var2), this.C(var3));
      this.S[1].set(this.S[0]).translate(this.F(this.I(bo.i.L, 0)), this.d(this.I(bo.i.L, 1)), this.C(this.I(bo.i.L, 2)));
      this.t(this.S[1], 1, this.G(bo.i.L));
      this.J(2, bo.i.b, 0.0F, 0.0F, 0.0F);
      this.J(3, bo.i.y, t[2][0], t[2][1], t[2][2]);
      this.J(5, bo.i.u, t[3][0], t[3][1], t[3][2]);
      this.m(4, 3, bo.i.y);
      this.m(6, 5, bo.i.u);

      for (int var4 = 0; var4 < 2; var4++) {
         bo.i var5 = var4 == 0 ? bo.i.E : bo.i.Z;
         int var6 = var4 == 0 ? 7 : 9;
         this.S[var6].set(this.S[0]).translate(this.F(this.I(var5, 0) - var1), this.d(this.I(var5, 1) - var2), this.C(this.I(var5, 2) - var3));
         this.t(this.S[var6], var6, this.G(var5));
         this.m(var6 + 1, var6, var5);
      }

      for (int var7 = 0; var7 < 11; var7++) {
         this.S[var7].get(this.L, var7 * 16);
      }
   }

   private void t(Matrix4f var1, int var2, Matrix4f var3) {
      float var4 = this.M.pivots()[var2 * 3];
      float var5 = this.M.pivots()[var2 * 3 + 1];
      float var6 = this.M.pivots()[var2 * 3 + 2];
      var1.translate(var4, var5, var6).mul(var3).translate(-var4, -var5, -var6);
   }

   private void J(int var1, bo.i var2, float var3, float var4, float var5) {
      int var6 = bo.i.L.ordinal() * 10;
      int var7 = var2.ordinal() * 10;
      this.c.set(var3, var4, var5);
      this.F.identity().rotateZ(this.I[var6 + 5]).rotateY(this.I[var6 + 4]).rotateX(this.I[var6 + 3]);
      this.F.transformDirection(this.c).add(this.I[var6], this.I[var6 + 1], this.I[var6 + 2]);
      float var8 = this.I[var7] - this.c.x;
      float var9 = this.I[var7 + 1] - this.c.y;
      float var10 = this.I[var7 + 2] - this.c.z;
      this.c.set(this.M.pivots()[var1 * 3], this.M.pivots()[var1 * 3 + 1], this.M.pivots()[var1 * 3 + 2]);
      this.S[1].transformPosition(this.c).add(this.F(var8), this.d(var9), this.C(var10));
      this.S[var1].translation(this.c).mul(this.G(var2));
      this.S[var1].translate(-this.M.pivots()[var1 * 3], -this.M.pivots()[var1 * 3 + 1], -this.M.pivots()[var1 * 3 + 2]);
   }

   private void m(int var1, int var2, bo.i var3) {
      this.S[var1].set(this.S[var2]);
      this.t(this.S[var1], var1, this.p.identity().rotateX(this.I[var3.ordinal() * 10 + 6]));
   }

   private Matrix4f G(bo.i var1) {
      int var2 = var1.ordinal() * 10;
      return this.F.identity().rotateZ(-this.I[var2 + 5]).rotateY(-this.I[var2 + 4]).rotateX(this.I[var2 + 3]);
   }

   private float I(bo.i var1, int var2) {
      return this.I[var1.ordinal() * 10 + var2] - t[var1.ordinal()][var2];
   }

   private float F(float var1) {
      return var1 * this.x;
   }

   private float d(float var1) {
      return -var1 * this.x;
   }

   private float C(float var1) {
      return -var1 * this.x;
   }

   private void r() {
      float[] var1 = this.M.positions();
      float[] var2 = this.M.normals();
      float[] var3 = this.M.weights();
      float[] var4 = this.L;
      byte[] var5 = this.M.bones();
      float var6 = (float)Math.sqrt(0.9929F);
      int var7 = 0;

      for (int var8 = this.M.vertices(); var7 < var8; var7++) {
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

         this.i[var7 * 3] = var15;
         this.i[var7 * 3 + 1] = var16;
         this.i[var7 * 3 + 2] = var17;
         float var35 = (float)Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
         float var36 = var35 < 1.0E-6F ? 0.0F : (var18 * 0.35F + var19 * 0.8F + var20 * 0.48F) / (var35 * var6);
         this.N[var7] = 0.7F + 0.3F * Math.max(0.0F, var36);
      }

      float var24 = Float.POSITIVE_INFINITY;

      for (int var31 : this.f) {
         var24 = Math.min(var24, this.i[var31 * 3 + 1]);
      }

      this.q = this.f.length == 0 ? 0.0F : var24 * this.n;
      int[] var26 = this.M.indices();
      float[] var28 = this.M.uvs();

      for (int var30 = 0; var30 < var26.length; var30++) {
         int var32 = var26[var30];
         int var33 = var30 * 5;
         this.s[var33] = this.i[var32 * 3];
         this.s[var33 + 1] = this.i[var32 * 3 + 1] - this.q;
         this.s[var33 + 2] = this.i[var32 * 3 + 2];
         this.s[var33 + 3] = var28[var32 * 2];
         this.s[var33 + 4] = var28[var32 * 2 + 1];
         int var34 = Math.round(this.N[var32] * 255.0F);
         this.J[var30] = 0xFF000000 | var34 << 16 | var34 << 8 | var34;
      }
   }

   public Vector3f F(int var1, Vector3f var2) {
      return this.S[var1].transformPosition(var2).sub(0.0F, this.q, 0.0F);
   }

   public Vector3f k(boolean var1) {
      return this.F(var1 ? 4 : 6, this.M.palm(var1));
   }

   public record m(float[] pivots, float[] palms, float[] positions, float[] normals, float[] uvs, byte[] bones, float[] weights, int[] indices, float height) {
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
