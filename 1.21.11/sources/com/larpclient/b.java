package com.larpclient;

import net.minecraft.class_238;
import net.minecraft.class_243;

public final class b {
   public static b.k K(class_243 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1);
      class_243 var5 = new class_243(-Math.sin(var3), 0.0, Math.cos(var3));
      class_243 var6 = new class_243(var5.field_1352, Math.clamp(-Math.tan(Math.toRadians(var2)), -5.0, 5.0), var5.field_1350);
      return new b.k(var0.method_1019(var5.method_1021(0.3)), var6.method_1021(0.6 / var6.method_1033() + 0.5));
   }

   public static b.m d(class_243 var0, class_243 var1, int var2) {
      double var3 = var1.field_1352 - var0.field_1352;
      double var5 = var1.field_1350 - var0.field_1350;
      double var7 = Math.hypot(var3, var5);
      if (var7 > 0.5 && Double.isFinite(var1.field_1351)) {
         float var9 = (float)Math.toDegrees(Math.atan2(var5, var3)) - 90.0F;
         float var10 = -35.0F;
         float var11 = 35.0F;
         if (j(var0, var9, var10, var7, var1.field_1351, var2) < 0.0) {
            return null;
         }

         if (j(var0, var9, var11, var7, var1.field_1351, var2) > 0.0) {
            return null;
         }

         for (int var12 = 0; var12 < 32; var12++) {
            float var13 = (var10 + var11) / 2.0F;
            if (j(var0, var9, var13, var7, var1.field_1351, var2) > 0.0) {
               var10 = var13;
            } else {
               var11 = var13;
            }
         }

         float var14 = (var10 + var11) / 2.0F;
         int var15 = Z(var0, var9, var14, var7, var2);
         return var15 < 0 ? null : new b.m(var9, var14, var15);
      } else {
         return null;
      }
   }

   static double j(class_243 var0, float var1, float var2, double var3, double var5, int var7) {
      b.k var8 = K(var0, var1, var2);

      for (int var9 = 0; var9 < var7; var9++) {
         class_243 var10 = var8.position();
         class_243 var11 = var8.collisionEnd();
         double var12 = Math.hypot(var10.field_1352 - var0.field_1352, var10.field_1350 - var0.field_1350);
         double var14 = Math.hypot(var11.field_1352 - var0.field_1352, var11.field_1350 - var0.field_1350);
         if (var14 >= var3) {
            double var16 = var14 - var12 < 1.0E-9 ? 1.0 : Math.clamp((var3 - var12) / (var14 - var12), 0.0, 1.0);
            return var10.field_1351 + (var11.field_1351 - var10.field_1351) * var16 - var5;
         }

         var8 = var8.next();
      }

      return Double.NEGATIVE_INFINITY;
   }

   private static int Z(class_243 var0, float var1, float var2, double var3, int var5) {
      b.k var6 = K(var0, var1, var2);

      for (int var7 = 0; var7 < var5; var7++) {
         class_243 var8 = var6.collisionEnd();
         if (Math.hypot(var8.field_1352 - var0.field_1352, var8.field_1350 - var0.field_1350) >= var3) {
            return var7 + 1;
         }

         var6 = var6.next();
      }

      return -1;
   }

   public static double g(class_243 var0, class_243 var1, class_238 var2) {
      return var2.method_1006(var0) ? 0.0 : var2.method_992(var0, var1).<Double>map(var0::method_1025).orElse(Double.POSITIVE_INFINITY);
   }

   public static double z(int var0) {
      return Math.clamp((var0 - 1) / 20.0, 0.0, 0.3);
   }

   public static class_243 K(class_243 var0, int var1) {
      double var2 = var0.method_1033();
      return var0.method_1021((var2 > 0.6 ? 0.6 / var2 : 1.0) * Math.min(var1, 6));
   }

   private b() {
   }

   public record k(class_243 position, class_243 velocity) {
      public class_243 collisionEnd() {
         return this.position.method_1019(this.velocity);
      }

      public b.k next() {
         class_243 var1 = this.velocity.method_1031(0.0, -0.03, 0.0);
         return new b.k(this.position.method_1019(var1), var1.method_1021(0.92));
      }
   }

   public record m(float yaw, float pitch, int ticks) {
   }
}
