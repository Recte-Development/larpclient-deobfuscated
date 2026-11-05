package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public final class y1 {
   private y1() {
   }

   public static void p(HumanoidModel<?> var0, float[] var1, float var2) {
      Q(var0.head, var1, jc.z.J, var2);
      Q(var0.body, var1, jc.z.L, var2);
      Q(var0.rightArm, var1, jc.z.K, var2);
      Q(var0.leftArm, var1, jc.z.W, var2);
      Q(var0.rightLeg, var1, jc.z.I, var2);
      Q(var0.leftLeg, var1, jc.z.h, var2);
   }

   public static float[] t(float[] var0, float var1) {
      jc.z[] var2 = new jc.z[]{jc.z.K, jc.z.W, jc.z.I, jc.z.h};
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

   public static void A(PoseStack var0, float[] var1, float var2) {
      int var3 = jc.z.v.ordinal() * 10;
      var0.translate(L(var1[var3], var2), L(var1[var3 + 1], var2) + 0.7F, L(var1[var3 + 2], var2));
      fx.b(var0, Axis.ZP.rotation(L(var1[var3 + 5], var2)));
      fx.b(var0, Axis.YP.rotation(L(var1[var3 + 4], var2)));
      fx.b(var0, Axis.XP.rotation(L(var1[var3 + 3], var2)));
      var0.translate(0.0F, -0.7F, 0.0F);
   }

   private static void Q(ModelPart var0, float[] var1, jc.z var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.x = P(var0.x, var1[var4], var3);
      var0.y = P(var0.y, var1[var4 + 1], var3);
      var0.z = P(var0.z, var1[var4 + 2], var3);
      var0.xRot = P(var0.xRot, var1[var4 + 3], var3);
      var0.yRot = P(var0.yRot, var1[var4 + 4], var3);
      var0.zRot = P(var0.zRot, var1[var4 + 5], var3);
   }

   static float P(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float L(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
