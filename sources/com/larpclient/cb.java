package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class cb {
   public static final float i = 0.72F;
   public static final float B = (float)Math.toRadians(70.0);

   public static float c(z5 var0) {
      return switch (var0) {
         case o, k, f -> 7.5F;
         default -> 8.0F;
      };
   }

   public static float v(z5 var0) {
      return 0.0F;
   }

   public static Quaternionf b(c1 var0) {
      float[] var1 = var0.root().idle()[0];
      float[] var2 = var0.arm().idle()[0];
      Quaternionf var3 = new Quaternionf(var2[3], var2[4], var2[5], var2[6]);
      Quaternionf var4 = new Quaternionf().rotateZ(0.1F);
      Vector3f var5 = new Quaternionf(var1[3], var1[4], var1[5], var1[6]).transform(new Vector3f(0.0F, 1.0F, 0.0F));
      Vector3f var6 = var3.transform(var4.transform(new Vector3f(0.0F, 1.0F, 0.0F)));
      Vector3f var7 = var3.transform(var4.transform(new Vector3f(1.0F, 0.0F, 0.0F)));
      Vector3f var8 = new Vector3f(var6).cross(var5);
      Quaternionf var9 = new Quaternionf();
      if (var8.lengthSquared() > 1.0E-8F) {
         var9.fromAxisAngleRad(var8.normalize(), (float)Math.acos(Math.clamp(var6.dot(var5), -1.0F, 1.0F)) - B);
      }

      var9.transform(var6);
      var9.transform(var7);
      Vector3f var10 = new Vector3f(var5).fma(-var5.dot(var6), var6).normalize();
      if (var7.dot(var10) < 0.0F) {
         var7.negate();
      }

      Quaternionf var11 = new Quaternionf().fromAxisAngleRad(var6, (float)Math.atan2(new Vector3f(var7).cross(var10).dot(var6), var7.dot(var10)));
      return new Quaternionf(var3).conjugate().mul(var11).mul(var9).mul(var3).normalize();
   }

   public static float i(z5 var0) {
      return switch (var0) {
         case o -> -0.03F;
         case k -> -0.07F;
         case f -> -0.09F;
         default -> 0.0F;
      };
   }

   public static Vector3f e(c1 var0, z5 var1, Quaternionf var2, float var3) {
      float[] var4 = var0.root().idle()[0];
      float[] var5 = var0.arm().idle()[0];
      Vector3f var6 = new Quaternionf(var4[3], var4[4], var4[5], var4[6]).transform(new Vector3f(0.0F, i(var1) * var0.scale() * var3, 0.0F));
      return new Quaternionf(var5[3], var5[4], var5[5], var5[6]).mul(var2).conjugate().transform(var6);
   }

   public static void j(PoseStack var0, z5 var1, int var2, boolean var3) {
      double var4 = var3 ? -0.5 : -1.0;
      double var6 = c(var1);
      var0.translate(
         var2 * (5.0 - var4 * Math.cos(0.1) + var6 * Math.sin(0.1)) / 16.0, -(2.0 + var4 * Math.sin(0.1) + var6 * Math.cos(0.1)) / 16.0, -v(var1) / 16.0F
      );
   }

   public static Matrix4f O(Matrix4f var0, z5 var1, int var2, boolean var3) {
      PoseStack var4 = new PoseStack();
      u.v(var4, var0);
      var4.scale(0.72F, 0.72F, 0.72F);
      j(var4, var1, var2, var3);
      var4.translate(var2 * -5 / 16.0F, 0.125F, 0.0F);
      u.v(var4, Axis.ZP.rotation(var2 * 0.1F));
      return new Matrix4f(var4.last().pose());
   }

   private cb() {
   }
}
