package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class n4 {
   public static final float B = 0.12F;
   public static final int s = 73;
   private static final int[] u = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] H = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<n4.j> O = r();
   public static final List<n4.y> m;
   public static final List<n4.d> M = List.of(
      new n4.d(0, 1, 0.028F, true),
      new n4.d(1, 2, 0.024F, true),
      new n4.d(2, 3, 0.019F, true),
      new n4.d(3, 4, 0.012F, false),
      new n4.d(2, 19, 0.013F, false),
      new n4.d(19, 7, 0.008F, false),
      new n4.d(2, 22, 0.012F, false),
      new n4.d(22, 10, 0.007F, false),
      new n4.d(2, 25, 0.01F, false),
      new n4.d(25, 13, 0.005F, false)
   );
   public static final float w = 1.38F;
   public static final float v = 0.16F;
   public static final float j = 2.0F;

   private static List<n4.j> r() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.21F, 0.29F, -0.015F),
            new n4.j(0.48F, 0.47F, 0.045F),
            new n4.j(1.14F, 0.49F, 0.05F),
            new n4.j(1.52F, 0.12F, 0.08F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(1.18F, -0.45F, 0.06F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.68F, -0.55F, 0.03F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.1F, -0.7F, 0.005F),
            new n4.j(0.0F, 0.0F, 0.0F),
            new n4.j(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         n4.j var2 = (n4.j)var0.get(u[var1 * 3]);
         n4.j var3 = (n4.j)var0.get(u[var1 * 3 + 3]);
         n4.j var4 = t(t(var2, var3, 0.5F), (n4.j)var0.get(2), H[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = var5 / 3.0F;
            var0.set(u[var1 * 3 + var5], t(t(var2, var4, var6), t(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static n4.i o(boolean var0, boolean var1, float var2, float var3) {
      return l(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static n4.i l(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new n4.i(
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

   public static Matrix4f n(n4.i var0, float var1, float var2, float var3, float var4, int var5) {
      return A(
         new Matrix4f().rotateY(p(180.0F - var1)).translate(0.0F, var0.preY, var0.preZ).rotateX(p(var0.pitch)).translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f b(n4.i var0, float var1, float var2, float var3, int var4, boolean var5) {
      return A(
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

   private static Matrix4f A(Matrix4f var0, n4.i var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin(var6) + 0.16 * Math.sin(var6 * 2.0F)) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate(var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(p(-var5 * var8))
         .rotateZ(p(var5 * (var1.splay + 3.5F * (float)Math.cos(var6))))
         .rotateX(p(var1.wingPitch));
   }

   public static n4.z y(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(O);
      n4.j var5 = (n4.j)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin(var6 - 0.7F);

      for (int var8 = 0; var8 < 13; var8++) {
         n4.j var9 = t(var5, (n4.j)var4.get(u[var8]), 0.5F);
         var4.add(new n4.j(var9.x, var9.y, var9.z + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = p(8.0F + 7.0F * (float)Math.sin(var6 - 0.65F) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = p(-4.0F * var2 + 2.0F * (float)Math.sin(var6 - 0.95F));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            n4.j var12 = (n4.j)var4.get(var11);
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
               new n4.j(
                  var5.x + var19 * (float)Math.cos(var15) - var17 * (float)Math.sin(var15),
                  var5.y + var19 * (float)Math.sin(var15) + var17 * (float)Math.cos(var15),
                  var5.z + var20
               )
            );
         }
      }

      return new n4.z(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin(var6 - 0.25F));
   }

   public static Vec3 y(Vec3 var0, Matrix4f var1, int var2, n4.j var3) {
      float var4 = var2 * var3.x;
      float var5 = var3.y;
      float var6 = var3.z;
      return var0.add(
         var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30(),
         var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31(),
         var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32()
      );
   }

   public static n4.b f(Vec3 var0, Matrix4f var1, int var2, n4.z var3, int var4, int var5, float var6) {
      return new n4.b(
         var3.points.stream().map(var3x -> y(var0, var1, var2, var3x)).toList(),
         new Vec3(var1.m20(), var1.m21(), var1.m22()).normalize(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int D(n4.b var0, int var1) {
      float var2 = var1 < O.size() ? O.get(var1).x / 1.52F : 0.4F;
      int var3 = vv.Q(var0.primary, var0.secondary, Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= O.size() && (var1 - O.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= O.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= O.size() ? 212 : (var1 < 3 ? 225 : 145);
      return l(vv.Q(-15526620, var3, var5), Math.round(var7 * (var3 >>> 24) / 255.0F));
   }

   public static int B(n4.b var0, int var1, boolean var2) {
      double var3 = var1 >= O.size() ? 0.4 : Math.clamp(O.get(var1).x / 1.52F, 0.0F, 1.0F);
      int var5 = vv.Q(var0.primary, var0.secondary, var3);
      return l(vv.Q(var5, -1, var2 ? 0.22 : 0.38), Math.round((var2 ? 88 : 215) * var0.pulse * (var5 >>> 24) / 255.0F));
   }

   public static boolean D(n4.b var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void N(ia var0, n4.b var1, n4.t var2) {
      List var3 = var1.points;
      if (var2 != n4.t.x) {
         var0.blend(_0.v.c);
         double var9 = (var2 == n4.t.a ? 0.047 : 0.008) * var1.size;

         for (int var11 = 0; var11 < O.size(); var11++) {
            var0.ribbon(
               (Vec3)var3.get(var11),
               (Vec3)var3.get((var11 + 1) % O.size()),
               var9,
               B(var1, var11, var2 == n4.t.a),
               B(var1, (var11 + 1) % O.size(), var2 == n4.t.a)
            );
         }
      } else {
         var0.blend(_0.v.z);

         for (n4.y var5 : m) {
            R(var0, (Vec3)var3.get(var5.a), D(var1, var5.a));
            R(var0, (Vec3)var3.get(var5.b), D(var1, var5.b));
            R(var0, (Vec3)var3.get(var5.c), D(var1, var5.c));
            R(var0, (Vec3)var3.get(var5.d), D(var1, var5.d));
         }

         for (n4.d var10 : M) {
            int var6 = B(var1, var10.a, false);
            int var7 = B(var1, var10.b, false);
            var0.ribbon((Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var10.width * var1.size, var6, var7);
            if (var10.ridge) {
               h(var0, (Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var1.normal, var10.width * var1.size, var6, var7);
            }
         }

         r(var0, (Vec3)var3.get(1), (Vec3)var3.get(0), (Vec3)var3.get(2), var1.normal, 0.045 * var1.size, B(var1, 1, false));
         r(var0, (Vec3)var3.get(2), (Vec3)var3.get(1), (Vec3)var3.get(3), var1.normal, 0.036 * var1.size, B(var1, 2, false));
      }
   }

   public static void F(fz var0, n4.b var1) {
      if (D(var1)) {
         var0.blend(_0.v.c);
         List var2 = var1.points;

         for (int var3 = 0; var3 < O.size(); var3++) {
            var0.ribbon(
               (Vec3)var2.get(var3), (Vec3)var2.get((var3 + 1) % O.size()), 0.016 * var1.size, B(var1, var3, false), B(var1, (var3 + 1) % O.size(), false)
            );
         }

         for (n4.d var4 : M) {
            if (var4.a == 2) {
               var0.ribbon((Vec3)var2.get(var4.a), (Vec3)var2.get(var4.b), 0.01 * var1.size, B(var1, var4.a, false), B(var1, var4.b, false));
            }
         }
      }
   }

   private static void h(ia var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, int var6, int var7) {
      Vec3 var8 = var3.scale(var4 * 0.4);
      R(var0, var1.add(var8), vv.O(var6, 0.72));
      R(var0, var2.add(var8), vv.O(var7, 0.72));
      R(var0, var2.subtract(var8), vv.O(var7, 0.45));
      R(var0, var1.subtract(var8), vv.O(var6, 0.45));
   }

   private static void r(ia var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, double var5, int var7) {
      Vec3 var8 = var3.subtract(var2).normalize().scale(var5);
      Vec3 var9 = var8.cross(var4).normalize().scale(var5);
      R(var0, var1.add(var8), var7);
      R(var0, var1.add(var9), vv.O(var7, 0.75));
      R(var0, var1.subtract(var8), vv.O(var7, 0.55));
      R(var0, var1.subtract(var9), var7);
   }

   private static n4.j t(n4.j var0, n4.j var1, float var2) {
      return new n4.j(var0.x + (var1.x - var0.x) * var2, var0.y + (var1.y - var0.y) * var2, var0.z + (var1.z - var0.z) * var2);
   }

   private static void R(ia var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int l(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float p(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private n4() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new n4.y(2, 16 + var1, 17 + var1, 2));
         var0.add(new n4.y(16 + var1, u[var1], u[var1 + 1], 17 + var1));
      }

      var0.add(new n4.y(0, 1, 2, 28));
      var0.add(new n4.y(2, 3, 4, 16));
      m = List.copyOf(var0);
   }

   public record b(List<Vec3> points, Vec3 normal, int primary, int secondary, float size, float pulse) {
   }

   public record d(int a, int b, float width, boolean ridge) {
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

   public record j(float x, float y, float z) {
   }

   public static final class m {
      private boolean Z;
      private float U;
      private float k;
      private float a;
      private float J;
      private float h;
      private float M;
      private float u;
      private Vec3 Y;

      public void H(float var1, boolean var2, float var3, boolean var4, Vec3 var5) {
         this.H(var1, var2, var3, var4, false, var5);
      }

      public void H(float var1, boolean var2, float var3, boolean var4, boolean var5, Vec3 var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = this.Z && !(var1 < this.U) && !(var1 - this.U > 10.0F) && (this.Y == null || !(var6.distanceToSqr(this.Y) > 64.0)) ? 0 : 1;
         if (var8 != 0) {
            this.k = var2 ? 1.0F : 0.0F;
            this.a = var7;
            this.J = var4 ? 1.0F : 0.0F;
            this.h = var5 ? 1.0F : 0.0F;
            this.M = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.U) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp(-var9 / 0.13F);
            float var11 = 1.0F - (float)Math.exp(-var9 / 0.19F);
            this.k = this.k + ((var2 ? 1 : 0) - this.k) * var10;
            this.J = this.J + ((var4 ? 1 : 0) - this.J) * var10;
            this.h = this.h + ((var5 ? 1 : 0) - this.h) * var10;
            this.a = this.a + (var7 - this.a) * var11;
            this.M = var9 * 20.0F;
         }

         this.U = var1;
         this.Y = var6;
         this.Z = (boolean)1;
      }

      public void y(float var1) {
         this.u = (float)((this.u + this.M * var1) % (Math.PI * 2));
      }

      public float z() {
         return this.k;
      }

      public float N() {
         return this.a;
      }

      public float V() {
         return this.J;
      }

      public float f() {
         return this.h;
      }

      public float m() {
         return this.u;
      }
   }

   public enum t {
      a,
      x,
      o;
   }

   public record y(int a, int b, int c, int d) {
   }

   public record z(List<n4.j> points, float pulse) {
   }
}
