package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class ut {
   public static final float l = 0.12F;
   public static final int K = 73;
   private static final int[] o = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] S = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<ut.o> r = X();
   public static final List<ut.u> n;
   public static final List<ut.z> Z = List.of(
      new ut.z(0, 1, 0.028F, true),
      new ut.z(1, 2, 0.024F, true),
      new ut.z(2, 3, 0.019F, true),
      new ut.z(3, 4, 0.012F, false),
      new ut.z(2, 19, 0.013F, false),
      new ut.z(19, 7, 0.008F, false),
      new ut.z(2, 22, 0.012F, false),
      new ut.z(22, 10, 0.007F, false),
      new ut.z(2, 25, 0.01F, false),
      new ut.z(25, 13, 0.005F, false)
   );
   public static final float s = 1.38F;
   public static final float O = 0.16F;
   public static final float t = 2.0F;

   private static List<ut.o> X() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.21F, 0.29F, -0.015F),
            new ut.o(0.48F, 0.47F, 0.045F),
            new ut.o(1.14F, 0.49F, 0.05F),
            new ut.o(1.52F, 0.12F, 0.08F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(1.18F, -0.45F, 0.06F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.68F, -0.55F, 0.03F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.1F, -0.7F, 0.005F),
            new ut.o(0.0F, 0.0F, 0.0F),
            new ut.o(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         ut.o var2 = (ut.o)var0.get(o[var1 * 3]);
         ut.o var3 = (ut.o)var0.get(o[var1 * 3 + 3]);
         ut.o var4 = X(X(var2, var3, 0.5F), (ut.o)var0.get(2), S[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = var5 / 3.0F;
            var0.set(o[var1 * 3 + var5], X(X(var2, var4, var6), X(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static ut.i j(boolean var0, boolean var1, float var2, float var3) {
      return O(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static ut.i O(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new ut.i(
         0.34F * var5,
         0.46F * var5,
         (1.38F - 0.42F * var4) * (1.0F - var5),
         0.16F * (1.0F - var5),
         18.0F * var4 * (1.0F - var5) + var6 * var5,
         1.0F - 0.24F * var5,
         1.0F - 0.08F * var5,
         7.0F * (1.0F - var5) + 2.0F * var5,
         11.0F * (1.0F - var5) + 1.5F * var5,
         0.09F,
         0.025F,
         -6.0F - 3.0F * var4,
         -3.0F,
         0.12F
      );
   }

   public static Matrix4f o(ut.i var0, float var1, float var2, float var3, float var4, int var5) {
      return S(
         new Matrix4f().rotateY(H(180.0F - var1)).translate(0.0F, var0.preY, var0.preZ).rotateX(H(var0.pitch)).translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f G(ut.i var0, float var1, float var2, float var3, int var4, boolean var5) {
      return S(
         new Matrix4f()
            .translate(0.0F, var5 ? 0.125F : 0.0F, 0.0F)
            .translate(0.0F, 1.501F, 0.0F)
            .scale(1.0666667F)
            .scale(-1.0F, -1.0F, 1.0F)
            .translate(0.0F, 1.38F, 0.16F),
         var0,
         var1,
         var2,
         var3,
         var4
      );
   }

   private static Matrix4f S(Matrix4f var0, ut.i var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin(var6) + 0.16 * Math.sin(var6 * 2.0F)) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate(var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(H(-var5 * var8))
         .rotateZ(H(var5 * (var1.splay + 3.5F * (float)Math.cos(var6))))
         .rotateX(H(var1.wingPitch));
   }

   public static ut.c Z(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(r);
      ut.o var5 = (ut.o)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin(var6 - 0.7F);

      for (int var8 = 0; var8 < 13; var8++) {
         ut.o var9 = X(var5, (ut.o)var4.get(o[var8]), 0.5F);
         var4.add(new ut.o(var9.x, var9.y, var9.z + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = H(8.0F + 7.0F * (float)Math.sin(var6 - 0.65F) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = H(-4.0F * var2 + 2.0F * (float)Math.sin(var6 - 0.95F));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            ut.o var12 = (ut.o)var4.get(var11);
            float var13 = Math.clamp(var12.x / 0.48F, 0.0F, 1.0F);
            float var14 = var21 * var13 * var10;
            float var15 = var22 * var13 * var10;
            float var16 = var12.x - var5.x;
            float var17 = var12.y - var5.y;
            float var18 = var12.z - var5.z;
            float var19 = var16 * (float)Math.cos(var14) + var18 * (float)Math.sin(var14);
            float var20 = -var16 * (float)Math.sin(var14) + var18 * (float)Math.cos(var14);
            var4.set(
               var11,
               new ut.o(
                  var5.x + var19 * (float)Math.cos(var15) - var17 * (float)Math.sin(var15),
                  var5.y + var19 * (float)Math.sin(var15) + var17 * (float)Math.cos(var15),
                  var5.z + var20
               )
            );
         }
      }

      return new ut.c(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin(var6 - 0.25F));
   }

   public static Vec3 v(Vec3 var0, Matrix4f var1, int var2, ut.o var3) {
      float var4 = var2 * var3.x;
      float var5 = var3.y;
      float var6 = var3.z;
      return var0.add(
         var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30(),
         var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31(),
         var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32()
      );
   }

   public static ut.v m(Vec3 var0, Matrix4f var1, int var2, ut.c var3, int var4, int var5, float var6) {
      return new ut.v(
         var3.points.stream().map(var3x -> v(var0, var1, var2, var3x)).toList(),
         new Vec3(var1.m20(), var1.m21(), var1.m22()).normalize(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int w(ut.v var0, int var1) {
      float var2 = var1 < r.size() ? r.get(var1).x / 1.52F : 0.4F;
      int var3 = _3.D(var0.primary, var0.secondary, Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= r.size() && (var1 - r.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= r.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= r.size() ? 212 : (var1 < 3 ? 225 : 145);
      return q(_3.D(-15526620, var3, var5), Math.round(var7 * (var3 >>> 24) / 255.0F));
   }

   public static int d(ut.v var0, int var1, boolean var2) {
      double var3 = var1 >= r.size() ? 0.4 : Math.clamp(r.get(var1).x / 1.52F, 0.0F, 1.0F);
      int var5 = _3.D(var0.primary, var0.secondary, var3);
      return q(_3.D(var5, -1, var2 ? 0.22 : 0.38), Math.round((var2 ? 88 : 215) * var0.pulse * (var5 >>> 24) / 255.0F));
   }

   public static boolean A(ut.v var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void Q(s var0, ut.v var1, ut.k var2) {
      List var3 = var1.points;
      if (var2 != ut.k.X) {
         var0.blend(pf.x.E);
         double var9 = (var2 == ut.k.J ? 0.047 : 0.008) * var1.size;

         for (int var11 = 0; var11 < r.size(); var11++) {
            var0.ribbon(
               (Vec3)var3.get(var11),
               (Vec3)var3.get((var11 + 1) % r.size()),
               var9,
               d(var1, var11, var2 == ut.k.J),
               d(var1, (var11 + 1) % r.size(), var2 == ut.k.J)
            );
         }
      } else {
         var0.blend(pf.x.S);

         for (ut.u var5 : n) {
            y(var0, (Vec3)var3.get(var5.a), w(var1, var5.a));
            y(var0, (Vec3)var3.get(var5.b), w(var1, var5.b));
            y(var0, (Vec3)var3.get(var5.c), w(var1, var5.c));
            y(var0, (Vec3)var3.get(var5.d), w(var1, var5.d));
         }

         for (ut.z var10 : Z) {
            int var6 = d(var1, var10.a, false);
            int var7 = d(var1, var10.b, false);
            var0.ribbon((Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var10.width * var1.size, var6, var7);
            if (var10.ridge) {
               S(var0, (Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var1.normal, var10.width * var1.size, var6, var7);
            }
         }

         v(var0, (Vec3)var3.get(1), (Vec3)var3.get(0), (Vec3)var3.get(2), var1.normal, 0.045 * var1.size, d(var1, 1, false));
         v(var0, (Vec3)var3.get(2), (Vec3)var3.get(1), (Vec3)var3.get(3), var1.normal, 0.036 * var1.size, d(var1, 2, false));
      }
   }

   public static void q(ki var0, ut.v var1) {
      if (A(var1)) {
         var0.blend(pf.x.E);
         List var2 = var1.points;

         for (int var3 = 0; var3 < r.size(); var3++) {
            var0.ribbon(
               (Vec3)var2.get(var3), (Vec3)var2.get((var3 + 1) % r.size()), 0.016 * var1.size, d(var1, var3, false), d(var1, (var3 + 1) % r.size(), false)
            );
         }

         for (ut.z var4 : Z) {
            if (var4.a == 2) {
               var0.ribbon((Vec3)var2.get(var4.a), (Vec3)var2.get(var4.b), 0.01 * var1.size, d(var1, var4.a, false), d(var1, var4.b, false));
            }
         }
      }
   }

   private static void S(s var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, int var6, int var7) {
      Vec3 var8 = var3.scale(var4 * 0.4);
      y(var0, var1.add(var8), _3.U(var6, 0.72));
      y(var0, var2.add(var8), _3.U(var7, 0.72));
      y(var0, var2.subtract(var8), _3.U(var7, 0.45));
      y(var0, var1.subtract(var8), _3.U(var6, 0.45));
   }

   private static void v(s var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, double var5, int var7) {
      Vec3 var8 = var3.subtract(var2).normalize().scale(var5);
      Vec3 var9 = var8.cross(var4).normalize().scale(var5);
      y(var0, var1.add(var8), var7);
      y(var0, var1.add(var9), _3.U(var7, 0.75));
      y(var0, var1.subtract(var8), _3.U(var7, 0.55));
      y(var0, var1.subtract(var9), var7);
   }

   private static ut.o X(ut.o var0, ut.o var1, float var2) {
      return new ut.o(var0.x + (var1.x - var0.x) * var2, var0.y + (var1.y - var0.y) * var2, var0.z + (var1.z - var0.z) * var2);
   }

   private static void y(s var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int q(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float H(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private ut() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new ut.u(2, 16 + var1, 17 + var1, 2));
         var0.add(new ut.u(16 + var1, o[var1], o[var1 + 1], 17 + var1));
      }

      var0.add(new ut.u(0, 1, 2, 28));
      var0.add(new ut.u(2, 3, 4, 16));
      n = List.copyOf(var0);
   }

   public record c(List<ut.o> points, float pulse) {
   }

   public record i(
      float preY,
      float preZ,
      float anchorY,
      float anchorZ,
      float pitch,
      float openScale,
      float scale,
      float walkSpread,
      float amplitude,
      float hingeX,
      float hingeZ,
      float splay,
      float wingPitch,
      float frequency
   ) {
   }

   public enum k {
      J,
      X,
      g;
   }

   public record o(float x, float y, float z) {
   }

   public static final class r {
      private boolean J;
      private float S;
      private float L;
      private float M;
      private float p;
      private float g;
      private float h;
      private float R;
      private Vec3 Y;

      public void D(float var1, boolean var2, float var3, boolean var4, Vec3 var5) {
         this.i(var1, var2, var3, var4, false, var5);
      }

      public void i(float var1, boolean var2, float var3, boolean var4, boolean var5, Vec3 var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = this.J && !(var1 < this.S) && !(var1 - this.S > 10.0F) && (this.Y == null || !(var6.distanceToSqr(this.Y) > 64.0)) ? 0 : 1;
         if (var8 != 0) {
            this.L = var2 ? 1.0F : 0.0F;
            this.M = var7;
            this.p = var4 ? 1.0F : 0.0F;
            this.g = var5 ? 1.0F : 0.0F;
            this.h = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.S) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp(-var9 / 0.13F);
            float var11 = 1.0F - (float)Math.exp(-var9 / 0.19F);
            this.L = this.L + ((var2 ? 1 : 0) - this.L) * var10;
            this.p = this.p + ((var4 ? 1 : 0) - this.p) * var10;
            this.g = this.g + ((var5 ? 1 : 0) - this.g) * var10;
            this.M = this.M + (var7 - this.M) * var11;
            this.h = var9 * 20.0F;
         }

         this.S = var1;
         this.Y = var6;
         this.J = (boolean)1;
      }

      public void I(float var1) {
         this.R = (float)((this.R + this.h * var1) % (Math.PI * 2));
      }

      public float e() {
         return this.L;
      }

      public float d() {
         return this.M;
      }

      public float x() {
         return this.p;
      }

      public float B() {
         return this.g;
      }

      public float l() {
         return this.R;
      }
   }

   public record u(int a, int b, int c, int d) {
   }

   public record v(List<Vec3> points, Vec3 normal, int primary, int secondary, float size, float pulse) {
   }

   public record z(int a, int b, float width, boolean ridge) {
   }
}
