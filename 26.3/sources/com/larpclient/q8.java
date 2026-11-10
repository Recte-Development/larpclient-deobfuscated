package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class q8 {
   private static final int J = 64;
   private static final int X = 3;
   public static final List<q8.x> e = y();
   private static final double[] w = new double[65];
   private static final double[] j = new double[65];

   private static q8.w D(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new q8.w(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<q8.x> y() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = var1 / 3.0;
         double var4 = (var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(new q8.x(D(var2, var6, 1.0 - var2), D(var4, var6, 1.0 - var4), D(var4, var6 + 1, 1.0 - var4), D(var2, var6 + 1, 1.0 - var2), false));
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(new q8.x(D(0.96, var7, -0.022), D(1.0, var7, 0.0), D(1.0, var7 + 1, 0.0), D(0.96, var7 + 1, -0.022), true));
      }

      return List.copyOf(var0);
   }

   public static int e(int var0, double var1) {
      return var0 & 0xFF000000 | (int)((var0 >>> 16 & 0xFF) * var1) << 16 | (int)((var0 >>> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public static void n(ia var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(_0.v.z);

      for (q8.x var12 : e) {
         a(var0, var1, var12.a, var12.brim, var2, var4, var6, var8, var9);
         a(var0, var1, var12.b, var12.brim, var2, var4, var6, var8, var9);
         a(var0, var1, var12.c, var12.brim, var2, var4, var6, var8, var9);
         a(var0, var1, var12.d, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(_0.v.c);
         z(var0, var1, var2, var6, 0.014, vv.O(var9, 0.22));
         z(var0, var1, var2, var6, 0.004, vv.Q(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(_0.v.z);
      }
   }

   private static void z(ia var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(w[var10] * (var2 - var6)), var9, (float)(j[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(w[var10] * (var2 + var6)), var9, (float)(j[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(w[var10 + 1] * (var2 + var6)), var9, (float)(j[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(w[var10 + 1] * (var2 - var6)), var9, (float)(j[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void a(ia var0, Matrix4f var1, q8.w var2, boolean var3, double var4, double var6, double var8, int var10, int var11) {
      int var12 = var3 ? e(var11, 0.58) : e(vv.Q(var10, var11, var2.radius), var2.light);
      S(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void S(ia var0, Matrix4f var1, q8.w var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3), var9);
   }

   private q8() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         w[var0] = Math.cos(var0 * Math.PI * 2.0 / 64.0);
         j[var0] = Math.sin(var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public record w(double x, double rise, double z, double radius, double light) {
   }

   public record x(q8.w a, q8.w b, q8.w c, q8.w d, boolean brim) {
   }
}
