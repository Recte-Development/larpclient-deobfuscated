package com.larpclient;

import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class qd {
   private qd() {
   }

   public static class_243 y(class_243 var0, class_243 var1, class_243 var2, class_243 var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : j(var0, var1, var2, var3, var4, Math.sqrt(var0.method_1022(var1)), Math.sqrt(var1.method_1022(var2)), Math.sqrt(var2.method_1022(var3)));
      }
   }

   private static class_243 j(class_243 var0, class_243 var1, class_243 var2, class_243 var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new class_243(
            z(var0.field_1352, var1.field_1352, var2.field_1352, var3.field_1352, var14, var16, var18, var20, var22),
            z(var0.field_1351, var1.field_1351, var2.field_1351, var3.field_1351, var14, var16, var18, var20, var22),
            z(var0.field_1350, var1.field_1350, var2.field_1350, var3.field_1350, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.method_35590(var2, var4);
      }
   }

   private static double z(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = class_3532.method_16436(var8, var0, var2);
      double var20 = class_3532.method_16436(var10, var2, var4);
      double var22 = class_3532.method_16436(var12, var4, var6);
      double var24 = class_3532.method_16436(var14, var18, var20);
      double var26 = class_3532.method_16436(var16, var20, var22);
      return class_3532.method_16436(var10, var24, var26);
   }

   static final class p {
      private class_243 M;
      private class_243 Q;
      private class_243 n;
      private class_243 G;
      private double v;
      private double c;
      private double q;

      void V(class_243 var1, class_243 var2, class_243 var3, class_243 var4) {
         this.M = var1;
         this.Q = var2;
         this.n = var3;
         this.G = var4;
         this.v = Math.sqrt(var1.method_1022(var2));
         this.c = Math.sqrt(var2.method_1022(var3));
         this.q = Math.sqrt(var3.method_1022(var4));
      }

      class_243 K(double var1) {
         if (var1 <= 0.0) {
            return this.Q;
         } else {
            return var1 >= 1.0 ? this.n : qd.j(this.M, this.Q, this.n, this.G, var1, this.v, this.c, this.q);
         }
      }
   }

   public static final class x {
      final class_243[] Q;
      final class_243[] S;
      final double[] g;
      final int[] M;
      int w;
      double C = Double.POSITIVE_INFINITY;

      public x(int var1) {
         this.Q = new class_243[var1];
         this.S = new class_243[var1];
         this.g = new double[var1];
         this.M = new int[var1];
      }

      public void T() {
         this.w = 0;
      }

      public int F() {
         return this.w;
      }

      public void x(class_243 var1, double var2, int var4) {
         if (this.w == this.Q.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         }

         this.Q[this.w] = var1;
         this.g[this.w] = var2;
         this.M[this.w++] = var4;
      }

      public void V() {
         if (this.w >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.w; var3++) {
               var1 += this.Q[var3 - 1].method_1022(this.Q[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.g[0] = 0.0;
               this.M[0] = this.M[0] & 16777215;

               for (int var7 = 1; var7 < this.w; var7++) {
                  var5 += this.Q[var7 - 1].method_1022(this.Q[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = u9.L(var5 / var10);
                  this.g[var7] = this.g[var7] * var8;
                  this.M[var7] = ei.R(this.M[var7], var8);
               }
            }
         }
      }

      public void k(class_243 var1) {
         if (this.w >= 2) {
            class_243 var2 = u(this.Q[0], this.Q[1], var1, new class_243(1.0, 0.0, 0.0));
            this.S[0] = var2.method_1021(this.g[0] * 0.5);

            for (int var3 = 1; var3 < this.w; var3++) {
               class_243 var4 = var3 + 1 < this.w ? u(this.Q[var3], this.Q[var3 + 1], var1, var2) : var2;
               if (var4.method_1026(var2) < 0.0) {
                  var4 = var4.method_1021(-1.0);
               }

               class_243 var5 = var2.method_1019(var4);
               class_243 var6 = var5.method_1027() < 1.0E-12 ? var2 : var5.method_1029();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.method_1026(var4))));
               this.S[var3] = var6.method_1021(this.g[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public class_243 j(int var1) {
         return this.S[var1];
      }

      private static class_243 u(class_243 var0, class_243 var1, class_243 var2, class_243 var3) {
         class_243 var4 = var1.method_1020(var0);
         class_243 var5 = var4.method_1036(var2.method_1020(var0));
         if (var5.method_1027() >= 1.0E-12) {
            return var5.method_1029();
         }

         if (var4.method_1027() < 1.0E-12) {
            return var3;
         }

         class_243 var6 = var4.method_1029();
         class_243 var7 = var3.method_1020(var6.method_1021(var3.method_1026(var6)));
         if (var7.method_1027() >= 1.0E-12) {
            return var7.method_1029();
         }

         class_243 var8 = Math.abs(var6.field_1351) < 0.9 ? new class_243(0.0, 1.0, 0.0) : new class_243(0.0, 0.0, 1.0);
         return var6.method_1036(var8).method_1029();
      }
   }
}
