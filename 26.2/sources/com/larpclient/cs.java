package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class cs {
   public static final int p = 16;
   public static final double K = 0.25;
   public static final double V = 0.0315;
   public static final double H = 0.085;
   private static final double x = 0.028;
   private static final int v = 48;
   private static final int N = 12;
   private static final int F = 8;
   private static final int t = 10;
   private static cs.y O;

   private cs() {
   }

   public static List<cs.v> e(double var0, double var2, double var4) {
      cs.y var6 = O;
      if (var6 != null && var6.radius == var0 && var6.height == var2 && var6.offset == var4) {
         return var6.quads;
      }

      List var7 = u(var0, var2, var4);
      O = new cs.y(var0, var2, var4, var7);
      return var7;
   }

   private static double[] S(double var0) {
      double var2 = Math.signum(var0);
      double var4 = Math.abs(var0);
      double var6 = 0.222;
      if (var4 <= var6) {
         return new double[]{var0, 0.0, 0.0, 1.0};
      } else {
         double var8 = var4 - var6;
         double var10 = 0.0439822971502571;
         if (var8 <= var10) {
            double var12 = var8 / 0.028;
            return new double[]{var2 * (var6 + 0.028 * Math.sin(var12)), 0.028 * (Math.cos(var12) - 1.0), var2 * Math.sin(var12), Math.cos(var12)};
         } else {
            return new double[]{var2 * 0.25, -0.028 - (var8 - var10), var2, 0.0};
         }
      }
   }

   private static double[] I(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = Math.min(1.0, Math.hypot(var0, var2));
      double[] var14 = S(var0 * var4);
      double[] var15 = S(0.085 + var2 * var4);
      double var16 = var14[2];
      double var18 = var14[3] * var15[3];
      double var20 = var15[2];
      double var22 = Math.sqrt(var16 * var16 + var18 * var18 + var20 * var20);
      var16 /= var22;
      var18 /= var22;
      var20 /= var22;
      double var24 = var6 * Math.max(0.0, 1.0 - var12 * var12) * var14[3] * var15[3];
      double var26 = 0.0315 + var8 + var24 + var10;
      return new double[]{var14[0] + var16 * var26, var14[1] + var15[1] + var18 * var26, var15[0] + var20 * var26, var16, var18, var20, var12};
   }

   private static double G(double var0, double var2, double var4) {
      return 0.66 + 0.3 * Math.max(0.0, var2) + 0.14 * Math.max(0.0, var0 * -0.5 + var4 * -0.3 + var2 * 0.6);
   }

   private static cs.z H(double var0, double var2, double var4, double var6, double var8, double var10) {
      double[] var12 = I(var2 * Math.cos(var0), var2 * Math.sin(var0), var4, var6, var8, 0.0);
      return new cs.z(var12[0], var12[1], var12[2], G(var12[3], var12[4], var12[5]) * var10);
   }

   private static List<cs.v> u(double var0, double var2, double var4) {
      ArrayList var6 = new ArrayList(816);
      double var7 = 0.1308996938995747;

      for (int var9 = 0; var9 < 12; var9++) {
         double var10 = var9 / 12.0;
         double var12 = (var9 + 1) / 12.0;

         for (int var14 = 0; var14 < 48; var14++) {
            var6.add(
               new cs.v(
                  H(var14 * var7, var10, var0, var2, var4, T(var14, var9)),
                  H(var14 * var7, var12, var0, var2, var4, T(var14, var9 + 1)),
                  H((var14 + 1) * var7, var12, var0, var2, var4, T(var14 + 1, var9 + 1)),
                  H((var14 + 1) * var7, var10, var0, var2, var4, T(var14 + 1, var9)),
                  false,
                  false
               )
            );
         }
      }

      double var18 = 0.0315 + var4 - 0.001;

      for (int var11 = 0; var11 < 48; var11++) {
         cs.z var20 = H(var11 * var7, 1.0, var0, var2, var4, 0.7);
         cs.z var13 = H((var11 + 1) * var7, 1.0, var0, var2, var4, 0.7);
         var6.add(new cs.v(var20, B(var20, var11 * var7, var0, var2, var4, var18), B(var13, (var11 + 1) * var7, var0, var2, var4, var18), var13, false, false));
      }

      s(var6, var0, var2, var4);

      for (int var19 = 0; var19 < 96; var19++) {
         double var21 = var19 * var7 / 2.0;
         double var22 = (var19 + 1) * var7 / 2.0;
         double var16 = var19 % 2 == 0 ? 1.06 : 0.88;
         var6.add(
            new cs.v(
               g(var21, 0.875, var0, var2, var4, var16),
               g(var21, 0.965, var0, var2, var4, var16),
               g(var22, 0.965, var0, var2, var4, var16),
               g(var22, 0.875, var0, var2, var4, var16),
               false,
               true
            )
         );
      }

      return List.copyOf(var6);
   }

   private static double T(int var0, int var1) {
      double var2 = Math.floorMod(var0, 6) == 0 && var1 > 0 ? 0.78 : 1.0;
      return var2 * (var1 % 2 == 0 ? 1.02 : 0.97);
   }

   private static cs.z B(cs.z var0, double var1, double var3, double var5, double var7, double var9) {
      double[] var11 = I(Math.cos(var1), Math.sin(var1), var3, var5, var7, -var9);
      return new cs.z(var11[0], var11[1], var11[2], var0.light * 0.75);
   }

   private static cs.z g(double var0, double var2, double var4, double var6, double var8, double var10) {
      double[] var12 = I(var2 * Math.cos(var0), var2 * Math.sin(var0), var4, var6, var8, 0.0035);
      return new cs.z(var12[0], var12[1], var12[2], G(var12[3], var12[4], var12[5]) * var10);
   }

   private static void s(List<cs.v> var0, double var1, double var3, double var5) {
      double var7 = 0.58;
      double var9 = 0.04;

      for (int var11 = 0; var11 < 2; var11++) {
         for (int var12 = 0; var12 < 3; var12++) {
            double var13 = (var11 + var12 * 2) * Math.PI / 3.0 + (Math.PI / 2);
            double var15 = var13 + (Math.PI * 2.0 / 3.0);
            double var17 = var7 * Math.cos(var13);
            double var19 = var7 * Math.sin(var13);
            double var21 = var7 * Math.cos(var15);
            double var23 = var7 * Math.sin(var15);
            double var25 = Math.hypot(var21 - var17, var23 - var19);
            double var27 = -(var23 - var19) / var25 * var9;
            double var29 = (var21 - var17) / var25 * var9;
            double var31 = (var21 - var17) / var25 * var9 * 0.6;
            double var33 = (var23 - var19) / var25 * var9 * 0.6;
            var17 -= var31;
            var19 -= var33;
            var21 += var31;
            var23 += var33;

            for (int var35 = 0; var35 < 10; var35++) {
               double var36 = var35 / 10.0;
               double var38 = (var35 + 1) / 10.0;
               double var40 = var17 + (var21 - var17) * var36;
               double var42 = var19 + (var23 - var19) * var36;
               double var44 = var17 + (var21 - var17) * var38;
               double var46 = var19 + (var23 - var19) * var38;
               double var48 = 0.0045 + var11 * 0.0015;
               var0.add(
                  new cs.v(
                     t(var40 + var27, var42 + var29, var1, var3, var5, var48),
                     t(var44 + var27, var46 + var29, var1, var3, var5, var48),
                     t(var44 - var27, var46 - var29, var1, var3, var5, var48),
                     t(var40 - var27, var42 - var29, var1, var3, var5, var48),
                     true,
                     false
                  )
               );
            }
         }
      }
   }

   private static cs.z t(double var0, double var2, double var4, double var6, double var8, double var10) {
      double[] var12 = I(var0, var2, var4, var6, var8, var10);
      return new cs.z(var12[0], var12[1], var12[2], G(var12[3], var12[4], var12[5]) * 1.1);
   }

   public static void n(d var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      List var13 = e(var2, var4, var6);
      int var14 = uh.q(var8, var9, 0.72);
      var0.blend(gl.c.K);
      var0.solid((var8 >>> 24) / 255.0 >= 0.995);

      try {
         for (cs.v var16 : var13) {
            if (!var16.hem || var10) {
               int var17 = var16.symbol ? var9 : (var16.hem ? var14 : var8);
               Q(var0, var1, var16.a, var17, var11);
               Q(var0, var1, var16.b, var17, var11);
               Q(var0, var1, var16.c, var17, var11);
               Q(var0, var1, var16.d, var17, var11);
            }
         }
      } finally {
         var0.solid(false);
      }
   }

   private static void Q(d var0, Matrix4f var1, cs.z var2, int var3, double var4) {
      var0.modelVertex(var1, (float)var2.x, (float)(-0.5 - var2.rise), (float)var2.z, E(var3, var2.light * var4));
   }

   static int E(int var0, double var1) {
      return var0 & 0xFF000000 | r(var0 >> 16, var1) << 16 | r(var0 >> 8, var1) << 8 | r(var0, var1);
   }

   private static int r(int var0, double var1) {
      return Math.min((int)((var0 & 0xFF) * var1), 255);
   }

   public record v(cs.z a, cs.z b, cs.z c, cs.z d, boolean symbol, boolean hem) {
   }

   private record y(double radius, double height, double offset, List<cs.v> quads) {
   }

   public record z(double x, double rise, double z, double light) {
   }
}
