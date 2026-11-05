package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class rt {
   public static final float k = 0.12F;
   public static final int a = 73;
   private static final int[] o = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
   private static final float[] v = new float[]{0.27F, 0.2F, 0.2F, 0.1F};
   public static final List<rt.q> N = l();
   public static final List<rt.r> T;
   public static final List<rt.l> u = List.of(
      new rt.l(0, 1, 0.028F, true),
      new rt.l(1, 2, 0.024F, true),
      new rt.l(2, 3, 0.019F, true),
      new rt.l(3, 4, 0.012F, false),
      new rt.l(2, 19, 0.013F, false),
      new rt.l(19, 7, 0.008F, false),
      new rt.l(2, 22, 0.012F, false),
      new rt.l(22, 10, 0.007F, false),
      new rt.l(2, 25, 0.01F, false),
      new rt.l(25, 13, 0.005F, false)
   );
   public static final float h = 1.38F;
   public static final float s = 0.16F;
   public static final float f = 2.0F;

   private static List<rt.q> l() {
      ArrayList var0 = new ArrayList<>(
         List.of(
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.21F, 0.29F, -0.015F),
            new rt.q(0.48F, 0.47F, 0.045F),
            new rt.q(1.14F, 0.49F, 0.05F),
            new rt.q(1.52F, 0.12F, 0.08F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(1.18F, -0.45F, 0.06F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.68F, -0.55F, 0.03F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.1F, -0.7F, 0.005F),
            new rt.q(0.0F, 0.0F, 0.0F),
            new rt.q(0.0F, 0.0F, 0.0F)
         )
      );

      for (int var1 = 0; var1 < 4; var1++) {
         rt.q var2 = (rt.q)var0.get(o[var1 * 3]);
         rt.q var3 = (rt.q)var0.get(o[var1 * 3 + 3]);
         rt.q var4 = K(K(var2, var3, 0.5F), (rt.q)var0.get(2), v[var1] * 2.0F);

         for (int var5 = 1; var5 < 3; var5++) {
            float var6 = var5 / 3.0F;
            var0.set(o[var1 * 3 + var5], K(K(var2, var4, var6), K(var4, var3, var6), var6));
         }
      }

      return List.copyOf(var0);
   }

   public static rt.j e(boolean var0, boolean var1, float var2, float var3) {
      return k(var0 ? 1.0F : 0.0F, var1 ? 1.0F : 0.0F, var2, var3);
   }

   public static rt.j k(float var0, float var1, float var2, float var3) {
      float var4 = Math.clamp(var0, 0.0F, 1.0F);
      float var5 = Math.clamp(var1, 0.0F, 1.0F);
      float var6 = Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) * (-90.0F - var3);
      return new rt.j(
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

   public static Matrix4f C(rt.j var0, float var1, float var2, float var3, float var4, int var5) {
      return m(
         new Matrix4f().rotateY(r(180.0F - var1)).translate(0.0F, var0.preY, var0.preZ).rotateX(r(var0.pitch)).translate(0.0F, var0.anchorY, var0.anchorZ),
         var0,
         var2,
         var3,
         var4,
         var5
      );
   }

   public static Matrix4f A(rt.j var0, float var1, float var2, float var3, int var4, boolean var5) {
      return m(
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

   private static Matrix4f m(Matrix4f var0, rt.j var1, float var2, float var3, float var4, int var5) {
      float var6 = var2 * var1.frequency;
      float var7 = (float)(Math.sin(var6) + 0.16 * Math.sin(var6 * 2.0F)) * var1.amplitude;
      float var8 = (18.0F + var7 + Math.clamp(var3, 0.0F, 1.0F) * var1.walkSpread) * var1.openScale;
      return var0.scale(var4 * var1.scale)
         .translate(var5 * var1.hingeX, 0.0F, var1.hingeZ)
         .rotateY(r(-var5 * var8))
         .rotateZ(r(var5 * (var1.splay + 3.5F * (float)Math.cos(var6))))
         .rotateX(r(var1.wingPitch));
   }

   public static rt.b c(float var0, float var1, float var2, float var3) {
      ArrayList var4 = new ArrayList<>(N);
      rt.q var5 = (rt.q)var4.get(2);
      float var6 = var0 * 0.12F;
      float var7 = 0.075F + 0.018F * (float)Math.sin(var6 - 0.7F);

      for (int var8 = 0; var8 < 13; var8++) {
         rt.q var9 = K(var5, (rt.q)var4.get(o[var8]), 0.5F);
         var4.add(new rt.q(var9.x, var9.y, var9.z + (var8 % 3 != 0 ? var7 : 0.008F)));
      }

      float var21 = r(8.0F + 7.0F * (float)Math.sin(var6 - 0.65F) + 5.0F * Math.clamp(var1, 0.0F, 1.0F) + 7.0F * var2);
      float var22 = r(-4.0F * var2 + 2.0F * (float)Math.sin(var6 - 0.95F));
      float var10 = 1.0F - 0.7F * Math.clamp(var3, 0.0F, 1.0F);

      for (int var11 = 0; var11 < var4.size(); var11++) {
         if (var11 != 0 && var11 != 1 && var11 != 2) {
            rt.q var12 = (rt.q)var4.get(var11);
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
               new rt.q(
                  var5.x + var19 * (float)Math.cos(var15) - var17 * (float)Math.sin(var15),
                  var5.y + var19 * (float)Math.sin(var15) + var17 * (float)Math.cos(var15),
                  var5.z + var20
               )
            );
         }
      }

      return new rt.b(List.copyOf(var4), 0.88F + 0.12F * (float)Math.sin(var6 - 0.25F));
   }

   public static Vec3 u(Vec3 var0, Matrix4f var1, int var2, rt.q var3) {
      float var4 = var2 * var3.x;
      float var5 = var3.y;
      float var6 = var3.z;
      return var0.add(
         var1.m00() * var4 + var1.m10() * var5 + var1.m20() * var6 + var1.m30(),
         var1.m01() * var4 + var1.m11() * var5 + var1.m21() * var6 + var1.m31(),
         var1.m02() * var4 + var1.m12() * var5 + var1.m22() * var6 + var1.m32()
      );
   }

   public static rt.i i(Vec3 var0, Matrix4f var1, int var2, rt.b var3, int var4, int var5, float var6) {
      return new rt.i(
         var3.points.stream().map(var3x -> u(var0, var1, var2, var3x)).toList(),
         new Vec3(var1.m20(), var1.m21(), var1.m22()).normalize(),
         var4,
         var5,
         var6,
         var3.pulse
      );
   }

   public static int Q(rt.i var0, int var1) {
      float var2 = var1 < N.size() ? N.get(var1).x / 1.52F : 0.4F;
      int var3 = oh.j(var0.primary, var0.secondary, Math.clamp(var2, 0.0F, 1.0F));
      int var4 = var1 >= N.size() && (var1 - N.size()) % 3 != 0 ? 1 : 0;
      double var5 = var4 != 0 ? 0.9 : (var1 >= N.size() ? 0.56 : (var1 < 3 ? 0.3 : 0.62));
      int var7 = var1 >= N.size() ? 212 : (var1 < 3 ? 225 : 145);
      return n(oh.j(-15526620, var3, var5), Math.round(var7 * (var3 >>> 24) / 255.0F));
   }

   public static int l(rt.i var0, int var1, boolean var2) {
      double var3 = var1 >= N.size() ? 0.4 : Math.clamp(N.get(var1).x / 1.52F, 0.0F, 1.0F);
      int var5 = oh.j(var0.primary, var0.secondary, var3);
      return n(oh.j(var5, -1, var2 ? 0.22 : 0.38), Math.round((var2 ? 88 : 215) * var0.pulse * (var5 >>> 24) / 255.0F));
   }

   public static boolean W(rt.i var0) {
      return var0.primary >>> 24 > 0 || var0.secondary >>> 24 > 0;
   }

   public static void X(_c var0, rt.i var1, rt.f var2) {
      List var3 = var1.points;
      if (var2 != rt.f.z) {
         var0.blend(fk.h.u);
         double var9 = (var2 == rt.f.c ? 0.047 : 0.008) * var1.size;

         for (int var11 = 0; var11 < N.size(); var11++) {
            var0.ribbon(
               (Vec3)var3.get(var11),
               (Vec3)var3.get((var11 + 1) % N.size()),
               var9,
               l(var1, var11, var2 == rt.f.c),
               l(var1, (var11 + 1) % N.size(), var2 == rt.f.c)
            );
         }
      } else {
         var0.blend(fk.h.z);

         for (rt.r var5 : T) {
            t(var0, (Vec3)var3.get(var5.a), Q(var1, var5.a));
            t(var0, (Vec3)var3.get(var5.b), Q(var1, var5.b));
            t(var0, (Vec3)var3.get(var5.c), Q(var1, var5.c));
            t(var0, (Vec3)var3.get(var5.d), Q(var1, var5.d));
         }

         for (rt.l var10 : u) {
            int var6 = l(var1, var10.a, false);
            int var7 = l(var1, var10.b, false);
            var0.ribbon((Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var10.width * var1.size, var6, var7);
            if (var10.ridge) {
               o(var0, (Vec3)var3.get(var10.a), (Vec3)var3.get(var10.b), var1.normal, var10.width * var1.size, var6, var7);
            }
         }

         g(var0, (Vec3)var3.get(1), (Vec3)var3.get(0), (Vec3)var3.get(2), var1.normal, 0.045 * var1.size, l(var1, 1, false));
         g(var0, (Vec3)var3.get(2), (Vec3)var3.get(1), (Vec3)var3.get(3), var1.normal, 0.036 * var1.size, l(var1, 2, false));
      }
   }

   public static void u(r6 var0, rt.i var1) {
      if (W(var1)) {
         var0.blend(fk.h.u);
         List var2 = var1.points;

         for (int var3 = 0; var3 < N.size(); var3++) {
            var0.ribbon(
               (Vec3)var2.get(var3), (Vec3)var2.get((var3 + 1) % N.size()), 0.016 * var1.size, l(var1, var3, false), l(var1, (var3 + 1) % N.size(), false)
            );
         }

         for (rt.l var4 : u) {
            if (var4.a == 2) {
               var0.ribbon((Vec3)var2.get(var4.a), (Vec3)var2.get(var4.b), 0.01 * var1.size, l(var1, var4.a, false), l(var1, var4.b, false));
            }
         }
      }
   }

   private static void o(_c var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, int var6, int var7) {
      Vec3 var8 = var3.scale(var4 * 0.4);
      t(var0, var1.add(var8), oh.S(var6, 0.72));
      t(var0, var2.add(var8), oh.S(var7, 0.72));
      t(var0, var2.subtract(var8), oh.S(var7, 0.45));
      t(var0, var1.subtract(var8), oh.S(var6, 0.45));
   }

   private static void g(_c var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, double var5, int var7) {
      Vec3 var8 = var3.subtract(var2).normalize().scale(var5);
      Vec3 var9 = var8.cross(var4).normalize().scale(var5);
      t(var0, var1.add(var8), var7);
      t(var0, var1.add(var9), oh.S(var7, 0.75));
      t(var0, var1.subtract(var8), oh.S(var7, 0.55));
      t(var0, var1.subtract(var9), var7);
   }

   private static rt.q K(rt.q var0, rt.q var1, float var2) {
      return new rt.q(var0.x + (var1.x - var0.x) * var2, var0.y + (var1.y - var0.y) * var2, var0.z + (var1.z - var0.z) * var2);
   }

   private static void t(_c var0, Vec3 var1, int var2) {
      var0.vertex(var1.x, var1.y, var1.z, var2);
   }

   private static int n(int var0, int var1) {
      return var1 << 24 | var0 & 16777215;
   }

   private static float r(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private rt() {
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 12; var1++) {
         var0.add(new rt.r(2, 16 + var1, 17 + var1, 2));
         var0.add(new rt.r(16 + var1, o[var1], o[var1 + 1], 17 + var1));
      }

      var0.add(new rt.r(0, 1, 2, 28));
      var0.add(new rt.r(2, 3, 4, 16));
      T = List.copyOf(var0);
   }

   public record b(List<rt.q> points, float pulse) {
   }

   public enum f {
      c,
      z,
      k;
   }

   public record i(List<Vec3> points, Vec3 normal, int primary, int secondary, float size, float pulse) {
   }

   public record j(
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

   public record l(int a, int b, float width, boolean ridge) {
   }

   public static final class p {
      private boolean y;
      private float J;
      private float C;
      private float G;
      private float u;
      private float E;
      private float q;
      private float x;
      private Vec3 L;

      public void W(float var1, boolean var2, float var3, boolean var4, Vec3 var5) {
         this.P(var1, var2, var3, var4, false, var5);
      }

      public void P(float var1, boolean var2, float var3, boolean var4, boolean var5, Vec3 var6) {
         float var7 = Math.clamp(var3, 0.0F, 1.0F);
         int var8 = this.y && !(var1 < this.J) && !(var1 - this.J > 10.0F) && (this.L == null || !(var6.distanceToSqr(this.L) > 64.0)) ? 0 : 1;
         if (var8 != 0) {
            this.C = var2 ? 1.0F : 0.0F;
            this.G = var7;
            this.u = var4 ? 1.0F : 0.0F;
            this.E = var5 ? 1.0F : 0.0F;
            this.q = 0.0F;
         } else {
            float var9 = Math.clamp((var1 - this.J) * 0.05F, 0.0F, 0.1F);
            float var10 = 1.0F - (float)Math.exp(-var9 / 0.13F);
            float var11 = 1.0F - (float)Math.exp(-var9 / 0.19F);
            this.C = this.C + ((var2 ? 1 : 0) - this.C) * var10;
            this.u = this.u + ((var4 ? 1 : 0) - this.u) * var10;
            this.E = this.E + ((var5 ? 1 : 0) - this.E) * var10;
            this.G = this.G + (var7 - this.G) * var11;
            this.q = var9 * 20.0F;
         }

         this.J = var1;
         this.L = var6;
         this.y = (boolean)1;
      }

      public void r(float var1) {
         this.x = (float)((this.x + this.q * var1) % (Math.PI * 2));
      }

      public float T() {
         return this.C;
      }

      public float u() {
         return this.G;
      }

      public float e() {
         return this.u;
      }

      public float A() {
         return this.E;
      }

      public float E() {
         return this.x;
      }
   }

   public record q(float x, float y, float z) {
   }

   public record r(int a, int b, int c, int d) {
   }
}
