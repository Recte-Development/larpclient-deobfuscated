package com.larpclient;

import java.util.List;
import net.minecraft.class_243;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class ch {
   private static class_243 I(Matrix4fc var0, class_243 var1, int var2) {
      Vector3f var3 = var0.transformPosition(new Vector3f((float)(var2 * var1.field_1352), (float)var1.field_1351, (float)var1.field_1350));
      return new class_243(var3.x, var3.y, var3.z);
   }

   public static ch.r s(float[] var0) {
      double var1 = Double.POSITIVE_INFINITY;
      double var3 = Double.NEGATIVE_INFINITY;

      for (byte var5 = 0; var5 < var0.length; var5 += 8) {
         var1 = Math.min(var1, var0[var5 + 1]);
         var3 = Math.max(var3, var0[var5 + 1]);
      }

      if (Double.isFinite(var1 + var3) && !(var3 - var1 < 0.02)) {
         class_243 var7 = U(var0, var1, var1 + (var3 - var1) * 0.055);
         class_243 var6 = U(var0, var3 - (var3 - var1) * 0.025, var3);
         return new ch.r(var7, var6);
      } else {
         return null;
      }
   }

   private static class_243 U(float[] var0, double var1, double var3) {
      double var5 = 0.0;
      double var7 = 0.0;
      double var9 = 0.0;
      int var11 = 0;

      for (byte var12 = 0; var12 < var0.length; var12 += 8) {
         if (var0[var12 + 1] >= var1 && var0[var12 + 1] <= var3) {
            var5 += var0[var12];
            var7 += var0[var12 + 1];
            var9 += var0[var12 + 2];
            var11++;
         }
      }

      return new class_243(var5 / var11, var7 / var11, var9 / var11);
   }

   public static ch.r y(List<class_243> var0) {
      if (var0.size() < 4) {
         return null;
      }

      class_243 var1 = class_243.field_1353;

      for (class_243 var3 : var0) {
         var1 = var1.method_1019(var3);
      }

      var1 = var1.method_1021(1.0 / var0.size());
      double var24 = 0.0;
      double var4 = 0.0;
      double var6 = 0.0;
      double var8 = 0.0;
      double var10 = 0.0;
      double var12 = 0.0;

      for (class_243 var15 : var0) {
         class_243 var16 = var15.method_1020(var1);
         var24 += var16.field_1352 * var16.field_1352;
         var4 += var16.field_1352 * var16.field_1351;
         var6 += var16.field_1352 * var16.field_1350;
         var8 += var16.field_1351 * var16.field_1351;
         var10 += var16.field_1351 * var16.field_1350;
         var12 += var16.field_1350 * var16.field_1350;
      }

      class_243 var25 = new class_243(0.6, 1.0, 0.05).method_1029();

      for (int var26 = 0; var26 < 12; var26++) {
         class_243 var28 = new class_243(
            var24 * var25.field_1352 + var4 * var25.field_1351 + var6 * var25.field_1350,
            var4 * var25.field_1352 + var8 * var25.field_1351 + var10 * var25.field_1350,
            var6 * var25.field_1352 + var10 * var25.field_1351 + var12 * var25.field_1350
         );
         if (var28.method_1027() < 1.0E-12) {
            return null;
         }

         var25 = var28.method_1029();
      }

      if (var25.field_1351 < 0.0) {
         var25 = var25.method_1021(-1.0);
      }

      double var27 = Double.POSITIVE_INFINITY;
      double var17 = Double.NEGATIVE_INFINITY;

      for (class_243 var20 : var0) {
         double var21 = var20.method_1020(var1).method_1026(var25);
         var27 = Math.min(var27, var21);
         var17 = Math.max(var17, var21);
      }

      if (var17 - var27 < 0.1) {
         return null;
      }

      class_243 var29 = class_243.field_1353;
      int var31 = 0;

      for (class_243 var22 : var0) {
         if (var22.method_1020(var1).method_1026(var25) >= var17 - (var17 - var27) * 0.025) {
            var29 = var29.method_1019(var22);
            var31++;
         }
      }

      var29 = var29.method_1021(1.0 / var31);
      class_243 var33 = var1.method_1019(var25.method_1021(var27 + (var17 - var27) * 0.48));
      return new ch.r(var33, var29);
   }

   private ch() {
   }

   public record r(class_243 root, class_243 tip) {
      public ch.r transform(Matrix4fc var1, int var2) {
         return new ch.r(ch.I(var1, this.root, var2), ch.I(var1, this.tip, var2));
      }

      public double length() {
         return this.root.method_1022(this.tip);
      }
   }
}
