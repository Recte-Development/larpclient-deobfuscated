package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class _e {
   private static final int O = 32;
   private static final int e = 6;
   private static final int w = 12;
   public static final List<_e.p> N = D();
   public static final int J = 16;

   private static _e.y Q(double var0, double var2, double var4) {
      return new _e.y(var0, Math.sqrt(Math.max(0.0, 1.0 - var0 * var0 - var2 * var2)) + var4, var2);
   }

   private static _e.y D(double var0, int var2, double var3) {
      double var5 = var2 * Math.PI * 2.0 / 32.0;
      return Q(var0 * Math.cos(var5), var0 * Math.sin(var5), var3);
   }

   private static List<_e.p> D() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < 6; var1++) {
         for (int var2 = 0; var2 < 32; var2++) {
            double var3 = var1 / 6.0;
            double var5 = (var1 + 1) / 6.0;
            var0.add(new _e.p(D(var3, var2, 0.0), D(var5, var2, 0.0), D(var5, var2 + 1, 0.0), D(var3, var2 + 1, 0.0), false, false));
         }
      }

      for (int var34 = 0; var34 < 2; var34++) {
         for (int var36 = 0; var36 < 3; var36++) {
            double var37 = (var34 + var36 * 2) * Math.PI / 3.0 - (Math.PI / 2);
            double var38 = var37 + (Math.PI * 2.0 / 3.0);
            double var7 = 0.57 * Math.cos(var37);
            double var9 = 0.57 * Math.sin(var37);
            double var11 = 0.57 * Math.cos(var38);
            double var13 = 0.57 * Math.sin(var38);
            double var15 = Math.hypot(var11 - var7, var13 - var9);
            double var17 = -(var13 - var9) / var15 * 0.018;
            double var19 = (var11 - var7) / var15 * 0.018;

            for (int var21 = 0; var21 < 12; var21++) {
               double var22 = var21 / 12.0;
               double var24 = (var21 + 1) / 12.0;
               double var26 = var7 + (var11 - var7) * var22;
               double var28 = var9 + (var13 - var9) * var22;
               double var30 = var7 + (var11 - var7) * var24;
               double var32 = var9 + (var13 - var9) * var24;
               var0.add(
                  new _e.p(
                     Q(var26 + var17, var28 + var19, 0.025),
                     Q(var30 + var17, var32 + var19, 0.025),
                     Q(var30 - var17, var32 - var19, 0.025),
                     Q(var26 - var17, var28 - var19, 0.025),
                     true,
                     false
                  )
               );
            }
         }
      }

      for (int var35 = 0; var35 < 32; var35++) {
         var0.add(new _e.p(D(0.982, var35, 0.015), D(1.0, var35, 0.015), D(1.0, var35 + 1, 0.015), D(0.982, var35 + 1, 0.015), false, true));
      }

      return List.copyOf(var0);
   }

   public static void u(_c var0, Matrix4f var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
      for (_e.p var12 : N) {
         if (!var12.hem || var10) {
            int var13 = var12.symbol ? var9 : (var12.hem ? oh.j(var8, var9, 0.25) : var8);
            L(var0, var1, var12.a, var2, var4, var6, var13);
            L(var0, var1, var12.b, var2, var4, var6, var13);
            L(var0, var1, var12.c, var2, var4, var6, var13);
            L(var0, var1, var12.d, var2, var4, var6, var13);
         }
      }
   }

   private static void L(_c var0, Matrix4f var1, _e.y var2, double var3, double var5, double var7, int var9) {
      var0.modelVertex(var1, (float)(var2.x * var3), (float)(-0.5 - var7 - var2.rise * var5), (float)(var2.z * var3 + 0.035), var9);
   }

   private _e() {
   }

   public record p(_e.y a, _e.y b, _e.y c, _e.y d, boolean symbol, boolean hem) {
   }

   public record y(double x, double rise, double z) {
   }
}
