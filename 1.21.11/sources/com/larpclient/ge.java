package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class ge {
   private static final int p = 64;
   private static final int P = 3;
   public static final List<ge.t> I = E();
   private static final double[] Z = new double[65];
   private static final double[] d = new double[65];

   private static ge.u R(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 64.0;
      double var7 = Math.cos(var5);
      double var9 = Math.sin(var5);
      double var11 = 0.76 + 0.18 * Math.max(0.0, -0.6 * var7 + 0.8 * var9);
      return new ge.u(var7 * var0, var3, var9 * var0, var0, var11);
   }

   private static List<ge.t> E() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 3; var1++) {
         double var2 = var1 / 3.0;
         double var4 = (var1 + 1) / 3.0;

         for (int var6 = 0; var6 < 64; var6++) {
            var0.add(new ge.t(R(var2, var6, 1.0 - var2), R(var4, var6, 1.0 - var4), R(var4, var6 + 1, 1.0 - var4), R(var2, var6 + 1, 1.0 - var2), false));
         }
      }

      for (int var7 = 0; var7 < 64; var7++) {
         var0.add(new ge.t(R(0.96, var7, -0.022), R(1.0, var7, 0.0), R(1.0, var7 + 1, 0.0), R(0.96, var7 + 1, -0.022), true));
      }

      return List.copyOf(var0);
   }

   public static int R(int var0, double var1) {
      return var0 & 0xFF000000 | (int)((var0 >>> 16 & 0xFF) * var1) << 16 | (int)((var0 >>> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public static void l(gq var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      var0.blend(ti.d.G);

      for (ge.t var12 : I) {
         F(var0, var1, var12.a, var12.brim, var2, var4, var6, var8, var9);
         F(var0, var1, var12.b, var12.brim, var2, var4, var6, var8, var9);
         F(var0, var1, var12.c, var12.brim, var2, var4, var6, var8, var9);
         F(var0, var1, var12.d, var12.brim, var2, var4, var6, var8, var9);
      }

      if (var10) {
         var0.blend(ti.d.l);
         m(var0, var1, var2, var6, 0.014, ei.R(var9, 0.22));
         m(var0, var1, var2, var6, 0.004, ei.W(var9, var9 & 0xFF000000 | 16777215, 0.52));
         var0.blend(ti.d.G);
      }
   }

   private static void m(gq var0, Matrix4f var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(-0.503 - var4);

      for (int var10 = 0; var10 < 64; var10++) {
         var0.modelVertex(var1, (float)(Z[var10] * (var2 - var6)), var9, (float)(d[var10] * (var2 - var6)), var8);
         var0.modelVertex(var1, (float)(Z[var10] * (var2 + var6)), var9, (float)(d[var10] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(Z[var10 + 1] * (var2 + var6)), var9, (float)(d[var10 + 1] * (var2 + var6)), var8);
         var0.modelVertex(var1, (float)(Z[var10 + 1] * (var2 - var6)), var9, (float)(d[var10 + 1] * (var2 - var6)), var8);
      }
   }

   private static void F(gq var0, Matrix4f var1, ge.u var2, boolean var3, double var4, double var6, double var8, int var10, int var11) {
      int var12 = var3 ? R(var11, 0.58) : R(ei.W(var10, var11, var2.radius), var2.light);
      h(var0, var1, var2, var4, var6, var8, var12);
   }

   private static void h(gq var0, Matrix4f var1, ge.u var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3), var9);
   }

   private ge() {
   }

   static {
      for (int var0 = 0; var0 <= 64; var0++) {
         Z[var0] = Math.cos(var0 * Math.PI * 2.0 / 64.0);
         d[var0] = Math.sin(var0 * Math.PI * 2.0 / 64.0);
      }
   }

   public record t(ge.u a, ge.u b, ge.u c, ge.u d, boolean brim) {
   }

   public record u(double x, double rise, double z, double radius, double light) {
   }
}
