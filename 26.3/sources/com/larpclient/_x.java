package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public final class _x {
   private _x() {
   }

   public static void U(HumanoidModel<?> var0, float[] var1, float var2) {
      k(var0.head, var1, bo.i.b, var2);
      k(var0.body, var1, bo.i.L, var2);
      k(var0.rightArm, var1, bo.i.y, var2);
      k(var0.leftArm, var1, bo.i.u, var2);
      k(var0.rightLeg, var1, bo.i.E, var2);
      k(var0.leftLeg, var1, bo.i.Z, var2);
   }

   public static float[] N(float[] var0, float var1) {
      bo.i[] var2 = new bo.i[]{bo.i.y, bo.i.u, bo.i.E, bo.i.Z};
      float[] var3 = null;

      for (int var4 = 0; var4 < var2.length; var4++) {
         float var5 = var0[var2[var4].ordinal() * 10 + 6];
         if (!Float.isNaN(var5)) {
            if (var3 == null) {
               var3 = new float[4];
            }

            var3[var4] = var5 * var1;
         }
      }

      return var3;
   }

   public static void I(PoseStack var0, float[] var1, float var2) {
      int var3 = bo.i.W.ordinal() * 10;
      var0.translate(c(var1[var3], var2), c(var1[var3 + 1], var2) + 0.7F, c(var1[var3 + 2], var2));
      qd.i(var0, Axis.ZP.rotation(c(var1[var3 + 5], var2)));
      qd.i(var0, Axis.YP.rotation(c(var1[var3 + 4], var2)));
      qd.i(var0, Axis.XP.rotation(c(var1[var3 + 3], var2)));
      var0.translate(0.0F, -0.7F, 0.0F);
   }

   private static void k(ModelPart var0, float[] var1, bo.i var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.x = o(var0.x, var1[var4], var3);
      var0.y = o(var0.y, var1[var4 + 1], var3);
      var0.z = o(var0.z, var1[var4 + 2], var3);
      var0.xRot = o(var0.xRot, var1[var4 + 3], var3);
      var0.yRot = o(var0.yRot, var1[var4 + 4], var3);
      var0.zRot = o(var0.zRot, var1[var4 + 5], var3);
   }

   static float o(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float c(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
