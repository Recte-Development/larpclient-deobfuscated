package com.larpclient.render.entity;

import com.larpclient.social.emote.EmoteAnimation;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public final class PoseOverride {
   private PoseOverride() {
   }

   public static void applyToModel(HumanoidModel<?> var0, float[] var1, float var2) {
      blendPart(var0.head, var1, EmoteAnimation.Part.HEAD, var2);
      blendPart(var0.body, var1, EmoteAnimation.Part.TORSO, var2);
      blendPart(var0.rightArm, var1, EmoteAnimation.Part.RIGHT_ARM, var2);
      blendPart(var0.leftArm, var1, EmoteAnimation.Part.LEFT_ARM, var2);
      blendPart(var0.rightLeg, var1, EmoteAnimation.Part.RIGHT_LEG, var2);
      blendPart(var0.leftLeg, var1, EmoteAnimation.Part.LEFT_LEG, var2);
   }

   public static float[] getLimbSwings(float[] var0, float var1) {
      EmoteAnimation.Part[] var2 = new EmoteAnimation.Part[]{
         EmoteAnimation.Part.RIGHT_ARM, EmoteAnimation.Part.LEFT_ARM, EmoteAnimation.Part.RIGHT_LEG, EmoteAnimation.Part.LEFT_LEG
      };
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

   public static void applyRootTransform(PoseStack var0, float[] var1, float var2) {
      int var3 = EmoteAnimation.Part.BODY.ordinal() * 10;
      var0.translate(scale(var1[var3], var2), scale(var1[var3 + 1], var2) + 0.7F, scale(var1[var3 + 2], var2));
      PoseStackUtil.rotate(var0, Axis.ZP.rotation(scale(var1[var3 + 5], var2)));
      PoseStackUtil.rotate(var0, Axis.YP.rotation(scale(var1[var3 + 4], var2)));
      PoseStackUtil.rotate(var0, Axis.XP.rotation(scale(var1[var3 + 3], var2)));
      var0.translate(0.0F, -0.7F, 0.0F);
   }

   private static void blendPart(ModelPart var0, float[] var1, EmoteAnimation.Part var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.x = lerp(var0.x, var1[var4], var3);
      var0.y = lerp(var0.y, var1[var4 + 1], var3);
      var0.z = lerp(var0.z, var1[var4 + 2], var3);
      var0.xRot = lerp(var0.xRot, var1[var4 + 3], var3);
      var0.yRot = lerp(var0.yRot, var1[var4 + 4], var3);
      var0.zRot = lerp(var0.zRot, var1[var4 + 5], var3);
   }

   static float lerp(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float scale(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
