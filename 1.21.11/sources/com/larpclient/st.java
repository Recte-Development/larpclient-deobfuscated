package com.larpclient;

import net.minecraft.class_243;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class st {
   private final f1 x;
   private final class_243 J;
   private final Quaternionf e;
   private final double Y;
   private final boolean h;
   private String H = "Matte";
   private class_243 U = new class_243(0.0, 0.3, 1.0).method_1029();

   st h(String var1, class_243 var2) {
      this.H = var1;
      this.U = var2;
      return this;
   }

   st(f1 var1, class_243 var2, Quaternionf var3, double var4, boolean var6) {
      this.x = var1;
      this.J = var2;
      this.e = var3;
      this.Y = var4;
      this.h = var6;
   }

   class_243 I(double var1, double var3, double var5) {
      Vector3f var7 = this.e.transform(new Vector3f((float)(var1 * this.Y), (float)(var3 * this.Y), (float)(var5 * this.Y)));
      return this.J.method_1031(var7.x, var7.y, var7.z);
   }

   private void I(double var1, double var3, double var5, int var7) {
      class_243 var8 = this.I(var1, var3, var5);
      this.x.vertex(var8.field_1352, var8.field_1351, var8.field_1350, var7);
   }

   void h(class_243 var1, class_243 var2, class_243 var3, class_243 var4, int var5) {
      class_243 var6 = var2.method_1020(var1).method_1036(var3.method_1020(var1)).method_1029();
      double var7 = this.h ? 1.0 : 0.68 + 0.32 * Math.max(0.0, var6.method_1026(new class_243(-0.35, 0.8, 0.45).method_1029()));
      int var9 = ei.W(var5, var5 & 0xFF000000, 1.0 - var7);
      Vector3f var10 = this.e.transform(new Vector3f((float)var6.field_1352, (float)var6.field_1351, (float)var6.field_1350));
      class_243 var11 = new class_243(var10.x, var10.y, var10.z);
      double var12 = Math.abs(var11.method_1026(this.U));
      if (!this.h) {
         switch (this.H) {
            case "Glass":
               var9 = ei.R(ei.W(var9, -1, 0.18 * Math.pow(1.0 - var12, 3.0)), 0.32 + 0.68 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Crystal":
               var9 = ei.W(var9, ei.D(-1, (var9 >>> 24) / 255.0), 0.35 * Math.pow(1.0 - var12, 4.0));
               break;
            case "Gold":
               var9 = ei.W(
                  var9,
                  ei.D(-4683, (var9 >>> 24) / 255.0),
                  0.65 * Math.pow(Math.max(0.0, var11.method_1026(new class_243(-0.35, 0.8, 0.45).method_1029())), 16.0)
               );
               break;
            case "Obsidian":
               var9 = ei.W(var9, ei.D(-7301451, (var9 >>> 24) / 255.0), 0.3 * Math.pow(var12, 28.0));
               break;
            case "Silk":
               var9 = ei.W(var9, ei.D(-5893, (var9 >>> 24) / 255.0), 0.3 * Math.pow(1.0 - var12, 2.0));
               break;
            case "Energy":
               var9 = ei.W(var5, ei.D(-1, (var5 >>> 24) / 255.0), 0.16 + 0.2 * Math.pow(1.0 - var12, 2.0));
         }
      }

      this.I(var1.field_1352, var1.field_1351, var1.field_1350, var9);
      this.I(var2.field_1352, var2.field_1351, var2.field_1350, var9);
      this.I(var3.field_1352, var3.field_1351, var3.field_1350, var9);
      this.I(var4.field_1352, var4.field_1351, var4.field_1350, var9);
   }

   void K(class_243 var1, class_243 var2, class_243 var3, int var4) {
      this.h(var1, var2, var3, var3, var4);
   }

   void c(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      byte var14 = 10;
      byte var15 = 20;

      for (int var16 = 0; var16 < var14; var16++) {
         for (int var17 = 0; var17 < var15; var17++) {
            double var18 = Math.PI * var16 / var14;
            double var20 = Math.PI * (var16 + 1) / var14;
            double var22 = (Math.PI * 2) * var17 / var15;
            double var24 = (Math.PI * 2) * (var17 + 1) / var15;
            this.h(
               n(var1, var3, var5, var7, var9, var11, var18, var22),
               n(var1, var3, var5, var7, var9, var11, var20, var22),
               n(var1, var3, var5, var7, var9, var11, var20, var24),
               n(var1, var3, var5, var7, var9, var11, var18, var24),
               var13
            );
         }
      }
   }

   private static class_243 n(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      return new class_243(var0 + var6 * Math.sin(var12) * Math.cos(var14), var2 + var8 * Math.cos(var12), var4 + var10 * Math.sin(var12) * Math.sin(var14));
   }

   void p(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      class_243[] var14 = new class_243[8];

      for (int var15 = 0; var15 < 8; var15++) {
         var14[var15] = new class_243(
            var1 + ((var15 & 1) == 0 ? -var7 : var7), var3 + ((var15 & 2) == 0 ? -var9 : var9), var5 + ((var15 & 4) == 0 ? -var11 : var11)
         );
      }

      int[][] var20 = new int[][]{{0, 2, 3, 1}, {4, 5, 7, 6}, {0, 4, 6, 2}, {1, 3, 7, 5}, {0, 1, 5, 4}, {2, 6, 7, 3}};

      for (int[] var19 : var20) {
         this.h(var14[var19[0]], var14[var19[1]], var14[var19[2]], var14[var19[3]], var13);
      }
   }

   void F(double var1, double var3, double var5, double var7, int var9) {
      for (int var10 = 0; var10 < 8; var10++) {
         double var11 = var7 + var10 * Math.PI / 4.0;
         double var13 = var7 + (var10 + 1) * Math.PI / 4.0;
         class_243 var15 = new class_243(Math.cos(var11) * var1, 0.0, Math.sin(var11) * var1);
         class_243 var16 = new class_243(Math.cos(var13) * var1, 0.0, Math.sin(var13) * var1);
         this.K(new class_243(0.0, var3, 0.0), var16, var15, var9);
         this.K(new class_243(0.0, -var5, 0.0), var15, var16, var9);
      }
   }

   void h(double var1, double var3, Quaternionf var5, int var6) {
      for (int var7 = 0; var7 < 48; var7++) {
         double var8 = (Math.PI * 2) * var7 / 48.0;
         double var10 = (Math.PI * 2) * (var7 + 1) / 48.0;
         this.h(P(var1 - var3, var8, var5), P(var1 + var3, var8, var5), P(var1 + var3, var10, var5), P(var1 - var3, var10, var5), var6);
      }
   }

   private static class_243 P(double var0, double var2, Quaternionf var4) {
      Vector3f var5 = var4.transform(new Vector3f((float)(Math.cos(var2) * var0), 0.0F, (float)(Math.sin(var2) * var0)));
      return new class_243(var5.x, var5.y, var5.z);
   }

   void z(class_243 var1, class_243 var2, double var3, double var5, int var7) {
      class_243 var8 = var2.method_1020(var1).method_1029();
      class_243 var9 = var8.method_1036(Math.abs(var8.field_1351) > 0.9 ? new class_243(1.0, 0.0, 0.0) : new class_243(0.0, 1.0, 0.0)).method_1029();
      class_243 var10 = var8.method_1036(var9).method_1029();

      for (int var11 = 0; var11 < 8; var11++) {
         double var12 = var11 * Math.PI / 4.0;
         double var14 = (var11 + 1) * Math.PI / 4.0;
         class_243 var16 = var9.method_1021(Math.cos(var12)).method_1019(var10.method_1021(Math.sin(var12)));
         class_243 var17 = var9.method_1021(Math.cos(var14)).method_1019(var10.method_1021(Math.sin(var14)));
         this.h(
            var1.method_1019(var16.method_1021(var3)),
            var2.method_1019(var16.method_1021(var5)),
            var2.method_1019(var17.method_1021(var5)),
            var1.method_1019(var17.method_1021(var3)),
            var7
         );
      }
   }
}
