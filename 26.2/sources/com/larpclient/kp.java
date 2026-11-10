package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public final class kp {
   private kp() {
   }

   public static void y(HumanoidModel<?> var0, float[] var1, float var2) {
      j(var0.head, var1, cx.q.P, var2);
      j(var0.body, var1, cx.q.E, var2);
      j(var0.rightArm, var1, cx.q.Q, var2);
      j(var0.leftArm, var1, cx.q.q, var2);
      j(var0.rightLeg, var1, cx.q.l, var2);
      j(var0.leftLeg, var1, cx.q.A, var2);
   }

   public static float[] M(float[] var0, float var1) {
      cx.q[] var2 = new cx.q[]{cx.q.Q, cx.q.q, cx.q.l, cx.q.A};
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

   public static void o(PoseStack var0, float[] var1, float var2) {
      int var3 = cx.q.m.ordinal() * 10;
      var0.translate(I(var1[var3], var2), I(var1[var3 + 1], var2) + 0.7F, I(var1[var3 + 2], var2));
      uf.q(var0, Axis.ZP.rotation(I(var1[var3 + 5], var2)));
      uf.q(var0, Axis.YP.rotation(I(var1[var3 + 4], var2)));
      uf.q(var0, Axis.XP.rotation(I(var1[var3 + 3], var2)));
      var0.translate(0.0F, -0.7F, 0.0F);
   }

   private static void j(ModelPart var0, float[] var1, cx.q var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.x = W(var0.x, var1[var4], var3);
      var0.y = W(var0.y, var1[var4 + 1], var3);
      var0.z = W(var0.z, var1[var4 + 2], var3);
      var0.xRot = W(var0.xRot, var1[var4 + 3], var3);
      var0.yRot = W(var0.yRot, var1[var4 + 4], var3);
      var0.zRot = W(var0.zRot, var1[var4 + 5], var3);
   }

   static float W(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float I(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
