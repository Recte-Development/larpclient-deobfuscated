package com.larpclient;

import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class xf {
   public static xf.n O(List<t8.m> var0, double var1, xf.v var3) {
      if (var0.size() >= 2 && !(var1 - ((t8.m)var0.getLast()).time() >= var3.duration)) {
         int var4 = var3.smoothing ? Math.clamp(var3.subdivisions, 1, 4) : 1;
         xf.j var5 = new xf.j(var0.size() * var4 * 12);
         xf.j var6 = new xf.j(var0.size() * var4 * 20 + var0.size() * 4);

         for (int var7 = 1; var7 < var0.size(); var7++) {
            t8.m var8 = (t8.m)var0.get(var7 - 1);
            t8.m var9 = (t8.m)var0.get(var7);
            if (var8.stroke() == var9.stroke() && !(var8.tip().distanceToSqr(var9.tip()) < 1.0E-9)) {
               t8.m var10 = var7 > 1 && ((t8.m)var0.get(var7 - 2)).stroke() == var8.stroke() ? (t8.m)var0.get(var7 - 2) : var8;
               t8.m var11 = var7 + 1 < var0.size() && ((t8.m)var0.get(var7 + 1)).stroke() == var8.stroke() ? (t8.m)var0.get(var7 + 1) : var9;
               int var12 = var7 - 1;

               while (var12 > 0 && ((t8.m)var0.get(var12 - 1)).stroke() == var8.stroke()) {
                  var12--;
               }

               int var13 = var7;

               while (var13 + 1 < var0.size() && ((t8.m)var0.get(var13 + 1)).stroke() == var8.stroke()) {
                  var13++;
               }

               xf.k var14 = a(var10, var8, var9, var11, 0.0, var7 - 1 - var12, var13 - var12, var1, var3);

               for (int var15 = 1; var15 <= var4; var15++) {
                  double var16 = (double)var15 / var4;
                  xf.k var18 = a(var10, var8, var9, var11, var16, var7 - 1 - var12 + var16, var13 - var12, var1, var3);
                  q(var5, var6, var14, var18, var3, var8.serial() + (var9.serial() - var8.serial()) * var16);
                  var14 = var18;
               }

               if (var3.sparks && var9.serial() % 7L == 0L && var7 > 1) {
                  f(var6, var8, var9, var1, var3);
               }
            }
         }

         return new xf.n(var5, var6);
      } else {
         return xf.n.EMPTY;
      }
   }

   private static xf.k a(t8.m var0, t8.m var1, t8.m var2, t8.m var3, double var4, double var6, int var8, double var9, xf.v var11) {
      Vec3 var12 = var11.smoothing ? __.m(var0.tip(), var1.tip(), var2.tip(), var3.tip(), var4) : var1.tip().lerp(var2.tip(), var4);
      Vec3 var13 = var11.smoothing ? __.m(var0.root(), var1.root(), var2.root(), var3.root(), var4) : var1.root().lerp(var2.root(), var4);
      Vec3 var14 = var13.subtract(var12);
      double var15 = var14.length();
      if (var15 < 1.0E-8) {
         var14 = new Vec3(0.0, -1.0, 0.0);
      } else {
         var14 = var14.scale(1.0 / var15);
      }

      double var17 = Math.clamp((var9 - (var1.time() + (var2.time() - var1.time()) * var4)) / var11.duration, 0.0, 1.0);
      double var19 = Math.clamp(var6 / Math.max(1.0, var8 * 0.2), 0.0, 1.0);
      var19 = var19 * var19 * (3.0 - 2.0 * var19);
      double var21 = Math.pow(1.0 - var17, 1.65) * var19;
      double var23 = var15 * var11.width * (0.78 + 0.22 * Math.cos(var17 * 3.0)) * Math.sqrt(var21);
      if (var11.style.equals("Void")) {
         var23 *= 0.7 + 0.3 * (1.0 - var17);
      }

      int var25 = _3.D(var11.secondary, var11.primary, 1.0 - var17 * 0.8);
      if (var11.style.equals("Prismatic")) {
         int var26 = _3.Z((float)((0.53 + var17 * 0.35 + var9 * 0.08) % 1.0), 0.5F, 1.0F);
         var25 = _3.D(var25, var26, 0.45);
      }

      return new xf.k(var12, var14, var23, var25, var21 * var11.intensity);
   }

   private static void q(xf.j var0, xf.j var1, xf.k var2, xf.k var3, xf.v var4, double var5) {
      if (!(var2.width < 1.0E-7) || !(var3.width < 1.0E-7)) {
         boolean var7 = var4.style.equals("Void");
         int var8 = var7 ? _3.D(var2.color, -16317420, 0.95) : var2.color;
         int var9 = var7 ? _3.D(var3.color, -16317420, 0.95) : var3.color;
         L(var0, var2, var3, 1.0, 0.8, var8, var9, 0.0, var7 ? 0.68 : 0.09);
         L(var0, var2, var3, 0.8, 0.12, var8, var9, var7 ? 0.68 : 0.09, var7 ? 0.85 : 0.32);
         L(var0, var2, var3, 0.12, 0.0, var8, var9, var7 ? 0.85 : 0.32, 0.05);
         double var10 = var4.style.equals("Prismatic") ? 0.009 : 0.014;
         w(var1, var2, var3, 0.0, var10, _3.D(var2.color, -1, 0.72), _3.D(var3.color, -1, 0.72), 0.92);
         w(var1, var2, var3, 0.56, var10 * 0.46, var2.color, var3.color, 0.34);
         if (var7) {
            w(var1, var2, var3, 0.95, var10 * 0.65, var2.color, var3.color, 0.7);
         }

         if (var4.style.equals("Prismatic")) {
            double var12 = Math.sin(var5 * 1.3);
            double var14 = Math.max(0.0, var12) * Math.max(0.0, var12);
            w(var1, var2, var3, 0.84, var10 * 0.5, _3.D(var2.color, var4.secondary, 0.7), _3.D(var3.color, var4.secondary, 0.7), 0.48 * var14);
         }

         if (var4.halo) {
            w(var1, var2, var3, 0.0, var10 * 4.5, var2.color, var3.color, 0.085);
            w(var1, var2, var3, 0.0, var10 * 2.5, var2.color, var3.color, 0.13);
         }
      }
   }

   private static void L(xf.j var0, xf.k var1, xf.k var2, double var3, double var5, int var7, int var8, double var9, double var11) {
      V(
         var0,
         var1.at(var3),
         var2.at(var3),
         var2.at(var5),
         var1.at(var5),
         _3.U(var7, var1.opacity * var9),
         _3.U(var8, var2.opacity * var9),
         _3.U(var8, var2.opacity * var11),
         _3.U(var7, var1.opacity * var11)
      );
   }

   private static void w(xf.j var0, xf.k var1, xf.k var2, double var3, double var5, int var7, int var8, double var9) {
      Vec3 var11 = var1.at(var3);
      Vec3 var12 = var2.at(var3);
      Vec3 var13 = var1.axis.scale(var5 * Math.sqrt(Math.max(0.0, var1.opacity)) * 0.5);
      Vec3 var14 = var2.axis.scale(var5 * Math.sqrt(Math.max(0.0, var2.opacity)) * 0.5);
      int var15 = _3.U(var7, var1.opacity * var9);
      int var16 = _3.U(var8, var2.opacity * var9);
      V(var0, var11.add(var13), var12.add(var14), var12.subtract(var14), var11.subtract(var13), var15, var16, var16, var15);
   }

   private static void f(xf.j var0, t8.m var1, t8.m var2, double var3, xf.v var5) {
      double var6 = var3 - var2.time();
      double var8 = Math.min(0.3, var5.duration * 1.2);
      if (!(var6 < 0.0) && !(var6 >= var8)) {
         Vec3 var10 = var2.tip().subtract(var1.tip());
         if (!(var10.lengthSqr() < 1.0E-4)) {
            Vec3 var11 = var2.root().subtract(var2.tip()).normalize();
            double var12 = (var2.serial() & 1L) == 0L ? -1.0 : 1.0;
            Vec3 var14 = var10.normalize().scale(0.3).add(var11.cross(new Vec3(0.0, 0.0, 1.0)).scale(var12 * 0.25));
            Vec3 var15 = var2.tip().add(var14.scale(var6)).add(0.0, -0.18 * var6 * var6, 0.0);
            Vec3 var16 = var15.subtract(var14.scale(0.035));
            Vec3 var17 = new Vec3(-var14.y, var14.x, 0.0).normalize().scale(0.0025 * (1.0 - var6 / var8));
            int var18 = _3.U(_3.D(var5.primary, -1, 0.55), (1.0 - var6 / var8) * var5.intensity * 0.8);
            V(var0, var15.add(var17), var16.add(var17), var16.subtract(var17), var15.subtract(var17), var18, 0, 0, var18);
         }
      }
   }

   private static void V(xf.j var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, int var5, int var6, int var7, int var8) {
      var0.S(var1, var5);
      var0.S(var2, var6);
      var0.S(var3, var7);
      var0.S(var4, var8);
   }

   private xf() {
   }

   public static final class j {
      private final float[] l;
      private final int[] C;
      private int u;

      private j(int var1) {
         this.l = new float[var1 * 3];
         this.C = new int[var1];
      }

      public int f() {
         return this.u;
      }

      public boolean l() {
         return this.u == 0;
      }

      public float Y(int var1) {
         return this.l[var1 * 3];
      }

      public float o(int var1) {
         return this.l[var1 * 3 + 1];
      }

      public float k(int var1) {
         return this.l[var1 * 3 + 2];
      }

      public int s(int var1) {
         return this.C[var1];
      }

      private void S(Vec3 var1, int var2) {
         this.l[this.u * 3] = (float)var1.x;
         this.l[this.u * 3 + 1] = (float)var1.y;
         this.l[this.u * 3 + 2] = (float)var1.z;
         this.C[this.u++] = var2;
      }
   }

   private record k(Vec3 tip, Vec3 axis, double width, int color, double opacity) {
      Vec3 at(double var1) {
         return this.tip.add(this.axis.scale(this.width * var1));
      }
   }

   public record n(xf.j body, xf.j light) {
      public static final xf.n EMPTY = new xf.n(new xf.j(0), new xf.j(0));

      public int vertices() {
         return this.body.f() + this.light.f();
      }
   }

   public record v(
      String style,
      double duration,
      double width,
      double intensity,
      int primary,
      int secondary,
      boolean halo,
      boolean sparks,
      boolean smoothing,
      int subdivisions
   ) {
   }
}
