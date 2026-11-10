package com.larpclient;

import net.minecraft.class_243;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class yt {
   public static final double U = 1.7;
   public static final double k = 0.85;
   public static final double D = 1.85;
   public static final double w = 1.02;
   public static final yt.l G = new yt.l(new class_243(1.0, 0.0, 0.0), new class_243(0.0, 1.0, 0.0), new class_243(0.0, 0.0, 1.0));

   public static double s(double var0) {
      return u8.S((var0 - 0.85) / 1.0);
   }

   public static class_243 h(u8 var0, double var1) {
      double var3 = Math.sin(Math.max(0.0, var1 - 1.85) * 2.4) * 0.045;
      return var0.z.method_1031(0.0, 2.75 + var3, 0.0);
   }

   public static yt.l U(class_243 var0, class_243 var1, class_243 var2, class_243 var3) {
      class_243 var4 = var1.method_1020(var0);
      if (var4.method_1027() < 1.0E-10) {
         var4 = var2.method_1036(var3);
      }

      if (var4.method_1027() < 1.0E-10) {
         var4 = G.normal;
      }

      var4 = var4.method_1029();
      class_243 var5 = var2.method_1020(var4.method_1021(var2.method_1026(var4)));
      if (var5.method_1027() < 1.0E-10) {
         var5 = var3.method_1036(var4);
      }

      if (var5.method_1027() < 1.0E-10) {
         class_243 var6 = Math.abs(var4.field_1351) < 0.9 ? new class_243(0.0, 1.0, 0.0) : new class_243(0.0, 0.0, 1.0);
         var5 = var6.method_1036(var4);
      }

      var5 = var5.method_1029();
      return new yt.l(var5, var4.method_1036(var5).method_1029(), var4);
   }

   public static yt.y q(u8 var0, double var1, yt.l var3, int var4) {
      e6.e var5 = yy.W.get(var4);
      class_243 var6 = h(var0, var1).method_1019(var3.normal.method_1021(var4 < 3 ? 0.035 : -0.035));
      return new yt.y(Y(var6, var3, var5.from()), Y(var6, var3, var5.to()));
   }

   private static class_243 Y(class_243 var0, yt.l var1, class_243 var2) {
      return var0.method_1019(var1.right.method_1021(var2.field_1352 * 1.02)).method_1019(var1.up.method_1021(-var2.field_1350 * 1.02));
   }

   private static Quaternionf B(yt.l var0) {
      class_243 var1 = var0.right.method_1021(-1.0);
      class_243 var2 = var0.up;
      class_243 var3 = var0.normal.method_1021(-1.0);
      return new Quaternionf()
         .setFromNormalized(
            new Matrix3f()
               .setColumn(0, new Vector3f((float)var1.field_1352, (float)var1.field_1351, (float)var1.field_1350))
               .setColumn(1, new Vector3f((float)var2.field_1352, (float)var2.field_1351, (float)var2.field_1350))
               .setColumn(2, new Vector3f((float)var3.field_1352, (float)var3.field_1351, (float)var3.field_1350))
         );
   }

   public static u8.s i(u8 var0, int var1, double var2, yt.l var4) {
      double var5 = s(var2);
      double var7 = 1.7 * u8.S(var2 / 0.85);
      double var9 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var11 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      class_243 var13 = var0.z.method_1031(0.0, 1.05 + var7, 0.0).method_1019(var4.right.method_1021(var9)).method_1019(var4.up.method_1021(var11 - 1.05));
      yt.y var14 = q(var0, var2, var4, var1);
      double var15 = var1 < 2 ? 0.5 : (!var0.D || var1 != 2 && var1 != 3 ? 0.25 : 0.1875);
      double var17 = var1 == 0 ? 0.5 : 0.75;
      double var19 = var1 == 0 ? 0.5 : 0.25;
      double var21 = var1 == 1 ? 0.22 : 0.18;
      class_243 var23 = new class_243(var21 / (var15 * 0.9375), var14.length() / (var17 * 0.9375), 0.14 / (var19 * 0.9375));
      e6.e var24 = yy.W.get(var1);
      class_243 var25 = var24.to().method_1020(var24.from()).method_1029();
      double var26 = Math.atan2(-var25.field_1352, var25.field_1350);
      Quaternionf var28 = B(var4).rotateZ((float)(var26 * var5));
      class_243 var29 = var13.method_35590(var14.center(), var5).method_1019(var4.normal.method_1021(Math.sin(var5 * Math.PI) * 0.18 * (var1 < 3 ? 1 : -1)));
      double var30 = u8.S((4.6 - var2) / 0.75);
      return new u8.s(var29, 0.0, 0.0, 0.0, 1.0, var30, 0.0, false, var28, new class_243(1.0, 1.0, 1.0).method_35590(var23, var5));
   }

   private yt() {
   }

   public record l(class_243 right, class_243 up, class_243 normal) {
   }

   public record y(class_243 from, class_243 to) {
      public class_243 center() {
         return this.from.method_35590(this.to, 0.5);
      }

      public class_243 tangent() {
         return this.to.method_1020(this.from).method_1029();
      }

      public double length() {
         return this.from.method_1022(this.to);
      }
   }
}
