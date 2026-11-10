package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class tf {
   private static final int x = 64;
   private static final int p = 3;
   public static final List<tf.p> D = k();
   private static final double[] d = new double[65];
   private static final double[] v = new double[65];

   private static tf.l b(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new tf.l(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<tf.p> k() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = var1 / 3.0;
         double var4 = (var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(new tf.p(b(var2, var6, 1.0 - var2), b(var4, var6, 1.0 - var4), b(var4, var6 + 1, 1.0 - var4), b(var2, var6 + 1, 1.0 - var2), false));
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(new tf.p(b(0.96, var7, -0.022), b(1.0, var7, 0.0), b(1.0, var7 + 1, 0.0), b(0.96, var7 + 1, -0.022), true));
      }

      return List.copyOf(var0);
   }

   public static int R(int var0, double var1) {
      return var0 & 0xFF000000 | (int)((var0 >>> 16 & 0xFF) * var1) << 16 | (int)((var0 >>> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public static void T(d var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(gl.c.K);

      for (tf.p var12 : D) {
         Q(var0, var1, var12.a, var12.brim, var2, var4, var6, var8, var9);
         Q(var0, var1, var12.b, var12.brim, var2, var4, var6, var8, var9);
         Q(var0, var1, var12.c, var12.brim, var2, var4, var6, var8, var9);
         Q(var0, var1, var12.d, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(gl.c.E);
         S(var0, var1, var2, var6, 0.014, uh.F(var9, 0.22));
         S(var0, var1, var2, var6, 0.004, uh.q(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(gl.c.K);
      }
   }

   private static void S(d var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(d[var10] * (var2 - var6)), var9, (float)(v[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(d[var10] * (var2 + var6)), var9, (float)(v[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(d[var10 + 1] * (var2 + var6)), var9, (float)(v[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(d[var10 + 1] * (var2 - var6)), var9, (float)(v[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void Q(d var0, Matrix4f var1, tf.l var2, boolean var3, double var4, double var6, double var8, int var10, int var11) {
      int var12 = var3 ? R(var11, 0.58) : R(uh.q(var10, var11, var2.radius), var2.light);
      N(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void N(d var0, Matrix4f var1, tf.l var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3), var9);
   }

   private tf() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         d[var0] = Math.cos(var0 * Math.PI * 2.0 / 64.0);
         v[var0] = Math.sin(var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public record l(double x, double rise, double z, double radius, double light) {
   }

   public record p(tf.l a, tf.l b, tf.l c, tf.l d, boolean brim) {
   }
}
