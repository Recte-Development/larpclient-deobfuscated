package com.larpclient;

import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class z6 {
   public static z6.s e(List<sk.u> var0, double var1, z6.g var3) {
      if (var0.size() >= 2 && !(var1 - ((sk.u)var0.getLast()).time() >= var3.duration)) {
         int var4 = var3.smoothing ? Math.clamp(var3.subdivisions, 1, 4) : 1;
         z6.b var5 = new z6.b(var0.size() * var4 * 12);
         z6.b var6 = new z6.b(var0.size() * var4 * 20 + var0.size() * 4);

         for (int var7 = 1; var7 < var0.size(); var7++) {
            sk.u var8 = (sk.u)var0.get(var7 - 1);
            sk.u var9 = (sk.u)var0.get(var7);
            if (var8.stroke() == var9.stroke() && !(var8.tip().distanceToSqr(var9.tip()) < 1.0E-9)) {
               sk.u var10 = var7 > 1 && ((sk.u)var0.get(var7 - 2)).stroke() == var8.stroke() ? (sk.u)var0.get(var7 - 2) : var8;
               sk.u var11 = var7 + 1 < var0.size() && ((sk.u)var0.get(var7 + 1)).stroke() == var8.stroke() ? (sk.u)var0.get(var7 + 1) : var9;
               int var12 = var7 - 1;

               while (var12 > 0 && ((sk.u)var0.get(var12 - 1)).stroke() == var8.stroke()) {
                  var12--;
               }

               int var13 = var7;

               while (var13 + 1 < var0.size() && ((sk.u)var0.get(var13 + 1)).stroke() == var8.stroke()) {
                  var13++;
               }

               z6.x var14 = Z(var10, var8, var9, var11, 0.0, var7 - 1 - var12, var13 - var12, var1, var3);

               for (int var15 = 1; var15 <= var4; var15++) {
                  double var16 = (double)var15 / var4;
                  z6.x var18 = Z(var10, var8, var9, var11, var16, var7 - 1 - var12 + var16, var13 - var12, var1, var3);
                  G(var5, var6, var14, var18, var3, var8.serial() + (var9.serial() - var8.serial()) * var16);
                  var14 = var18;
               }

               if (var3.sparks && var9.serial() % 7L == 0L && var7 > 1) {
                  a(var6, var8, var9, var1, var3);
               }
            }
         }

         return new z6.s(var5, var6);
      } else {
         return z6.s.EMPTY;
      }
   }

   private static z6.x Z(sk.u var0, sk.u var1, sk.u var2, sk.u var3, double var4, double var6, int var8, double var9, z6.g var11) {
      Vec3 var12 = var11.smoothing ? u_.B(var0.tip(), var1.tip(), var2.tip(), var3.tip(), var4) : var1.tip().lerp(var2.tip(), var4);
      Vec3 var13 = var11.smoothing ? u_.B(var0.root(), var1.root(), var2.root(), var3.root(), var4) : var1.root().lerp(var2.root(), var4);
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

      int var25 = uh.q(var11.secondary, var11.primary, 1.0 - var17 * 0.8);
      if (var11.style.equals("Prismatic")) {
         int var26 = uh.n((float)((0.53 + var17 * 0.35 + var9 * 0.08) % 1.0), 0.5F, 1.0F);
         var25 = uh.q(var25, var26, 0.45);
      }

      return new z6.x(var12, var14, var23, var25, var21 * var11.intensity);
   }

   private static void G(z6.b var0, z6.b var1, z6.x var2, z6.x var3, z6.g var4, double var5) {
      if (!(var2.width < 1.0E-7) || !(var3.width < 1.0E-7)) {
         boolean var7 = var4.style.equals("Void");
         int var8 = var7 ? uh.q(var2.color, -16317420, 0.95) : var2.color;
         int var9 = var7 ? uh.q(var3.color, -16317420, 0.95) : var3.color;
         g(var0, var2, var3, 1.0, 0.8, var8, var9, 0.0, var7 ? 0.68 : 0.09);
         g(var0, var2, var3, 0.8, 0.12, var8, var9, var7 ? 0.68 : 0.09, var7 ? 0.85 : 0.32);
         g(var0, var2, var3, 0.12, 0.0, var8, var9, var7 ? 0.85 : 0.32, 0.05);
         double var10 = var4.style.equals("Prismatic") ? 0.009 : 0.014;
         Z(var1, var2, var3, 0.0, var10, uh.q(var2.color, -1, 0.72), uh.q(var3.color, -1, 0.72), 0.92);
         Z(var1, var2, var3, 0.56, var10 * 0.46, var2.color, var3.color, 0.34);
         if (var7) {
            Z(var1, var2, var3, 0.95, var10 * 0.65, var2.color, var3.color, 0.7);
         }

         if (var4.style.equals("Prismatic")) {
            double var12 = Math.sin(var5 * 1.3);
            double var14 = Math.max(0.0, var12) * Math.max(0.0, var12);
            Z(var1, var2, var3, 0.84, var10 * 0.5, uh.q(var2.color, var4.secondary, 0.7), uh.q(var3.color, var4.secondary, 0.7), 0.48 * var14);
         }

         if (var4.halo) {
            Z(var1, var2, var3, 0.0, var10 * 4.5, var2.color, var3.color, 0.085);
            Z(var1, var2, var3, 0.0, var10 * 2.5, var2.color, var3.color, 0.13);
         }
      }
   }

   private static void g(z6.b var0, z6.x var1, z6.x var2, double var3, double var5, int var7, int var8, double var9, double var11) {
      J(
         var0,
         var1.at(var3),
         var2.at(var3),
         var2.at(var5),
         var1.at(var5),
         uh.F(var7, var1.opacity * var9),
         uh.F(var8, var2.opacity * var9),
         uh.F(var8, var2.opacity * var11),
         uh.F(var7, var1.opacity * var11)
      );
   }

   private static void Z(z6.b var0, z6.x var1, z6.x var2, double var3, double var5, int var7, int var8, double var9) {
      Vec3 var11 = var1.at(var3);
      Vec3 var12 = var2.at(var3);
      Vec3 var13 = var1.axis.scale(var5 * Math.sqrt(Math.max(0.0, var1.opacity)) * 0.5);
      Vec3 var14 = var2.axis.scale(var5 * Math.sqrt(Math.max(0.0, var2.opacity)) * 0.5);
      int var15 = uh.F(var7, var1.opacity * var9);
      int var16 = uh.F(var8, var2.opacity * var9);
      J(var0, var11.add(var13), var12.add(var14), var12.subtract(var14), var11.subtract(var13), var15, var16, var16, var15);
   }

   private static void a(z6.b var0, sk.u var1, sk.u var2, double var3, z6.g var5) {
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
            int var18 = uh.F(uh.q(var5.primary, -1, 0.55), (1.0 - var6 / var8) * var5.intensity * 0.8);
            J(var0, var15.add(var17), var16.add(var17), var16.subtract(var17), var15.subtract(var17), var18, 0, 0, var18);
         }
      }
   }

   private static void J(z6.b var0, Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4, int var5, int var6, int var7, int var8) {
      var0.r(var1, var5);
      var0.r(var2, var6);
      var0.r(var3, var7);
      var0.r(var4, var8);
   }

   private z6() {
   }

   public static final class b {
      private final float[] N;
      private final int[] C;
      private int F;

      private b(int var1) {
         this.N = new float[var1 * 3];
         this.C = new int[var1];
      }

      public int L() {
         return this.F;
      }

      public boolean Y() {
         return this.F == 0;
      }

      public float f(int var1) {
         return this.N[var1 * 3];
      }

      public float C(int var1) {
         return this.N[var1 * 3 + 1];
      }

      public float s(int var1) {
         return this.N[var1 * 3 + 2];
      }

      public int v(int var1) {
         return this.C[var1];
      }

      private void r(Vec3 var1, int var2) {
         this.N[this.F * 3] = (float)var1.x;
         this.N[this.F * 3 + 1] = (float)var1.y;
         this.N[this.F * 3 + 2] = (float)var1.z;
         this.C[this.F++] = var2;
      }
   }

   public record g(
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

   public record s(z6.b body, z6.b light) {
      public static final z6.s EMPTY = new z6.s(new z6.b(0), new z6.b(0));

      public int vertices() {
         return this.body.L() + this.light.L();
      }
   }

   private record x(Vec3 tip, Vec3 axis, double width, int color, double opacity) {
      Vec3 at(double var1) {
         return this.tip.add(this.axis.scale(this.width * var1));
      }
   }
}
