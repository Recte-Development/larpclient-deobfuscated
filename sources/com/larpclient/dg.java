package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class dg {
   private static final int h = 64;
   private static final int G = 3;
   public static final List<dg.a> O = D();
   private static final double[] P = new double[65];
   private static final double[] a = new double[65];

   private static dg.d K(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new dg.d(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<dg.a> D() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = var1 / 3.0;
         double var4 = (var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(new dg.a(K(var2, var6, 1.0 - var2), K(var4, var6, 1.0 - var4), K(var4, var6 + 1, 1.0 - var4), K(var2, var6 + 1, 1.0 - var2), false));
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(new dg.a(K(0.96, var7, -0.022), K(1.0, var7, 0.0), K(1.0, var7 + 1, 0.0), K(0.96, var7 + 1, -0.022), true));
      }

      return List.copyOf(var0);
   }

   public static int z(int var0, double var1) {
      return var0 & 0xFF000000 | (int)((var0 >>> 16 & 0xFF) * var1) << 16 | (int)((var0 >>> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public static void p(_c var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(fk.h.z);

      for (dg.a var12 : O) {
         i(var0, var1, var12.a, var12.brim, var2, var4, var6, var8, var9);
         i(var0, var1, var12.b, var12.brim, var2, var4, var6, var8, var9);
         i(var0, var1, var12.c, var12.brim, var2, var4, var6, var8, var9);
         i(var0, var1, var12.d, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(fk.h.u);
         R(var0, var1, var2, var6, 0.014, oh.S(var9, 0.22));
         R(var0, var1, var2, var6, 0.004, oh.j(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(fk.h.z);
      }
   }

   private static void R(_c var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(P[var10] * (var2 - var6)), var9, (float)(a[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(P[var10] * (var2 + var6)), var9, (float)(a[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(P[var10 + 1] * (var2 + var6)), var9, (float)(a[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(P[var10 + 1] * (var2 - var6)), var9, (float)(a[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void i(_c var0, Matrix4f var1, dg.d var2, boolean var3, double var4, double var6, double var8, int var10, int var11) {
      int var12 = var3 ? z(var11, 0.58) : z(oh.j(var10, var11, var2.radius), var2.light);
      P(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void P(_c var0, Matrix4f var1, dg.d var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3), var9);
   }

   private dg() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         P[var0] = Math.cos(var0 * Math.PI * 2.0 / 64.0);
         a[var0] = Math.sin(var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public record a(dg.d a, dg.d b, dg.d c, dg.d d, boolean brim) {
   }

   public record d(double x, double rise, double z, double radius, double light) {
   }
}
