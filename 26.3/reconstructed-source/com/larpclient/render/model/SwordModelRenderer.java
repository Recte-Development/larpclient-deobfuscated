package com.larpclient.render.model;

import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.render.entity.FlameHandsRenderer;
import com.larpclient.render.entity.ViewModelTransforms;
import com.larpclient.render.entity.WeaponTrailRenderer;
import com.larpclient.util.data.HandBoundsTracker;
import com.larpclient.util.data.HandPoseSample;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.render.PoseStackUtil;
import com.larpclient.util.render.SpinAnimator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class SwordModelRenderer {
   static final float fOV_SCALE = (float)(Math.tan(Math.toRadians(35.0)) / (0.75 * Math.tan(Math.toRadians(34.0))));
   static final float[][] tINT_PALETTES = new float[][]{
      {0.36F, 0.16F, 0.78F, 1.0F, 0.25F, 0.78F, 0.2F, 0.82F, 1.0F},
      {0.42F, 0.14F, 0.8F, 0.85F, 0.2F, 0.95F, 0.55F, 0.4F, 1.0F},
      {0.62F, 0.07F, 0.25F, 1.0F, 0.42F, 0.55F, 1.0F, 0.7F, 0.3F},
      {0.12F, 0.25F, 0.7F, 0.15F, 0.85F, 1.0F, 0.7F, 0.95F, 1.0F},
      {0.28F, 0.31F, 0.36F, 0.62F, 0.68F, 0.78F, 0.92F, 0.95F, 1.0F}
   };
   private static final float[] gLINT_UV_SCALE = new float[]{1.3F, 1.0F, 1.9F};
   private static final float[] uV_SCROLL_X = new float[]{0.011F, -0.017F, 0.007F};
   private static final float[] uV_SCROLL_Y = new float[]{-0.031F, -0.052F, -0.095F};
   private static final boolean dEBUG = false;

   public static boolean renderFirstPerson(
      AbstractClientPlayer var0, InteractionHand var1, ItemStack var2, float var3, float var4, float var5, PoseStack var6, SubmitNodeCollector var7, int var8
   ) {
      SwordModelsModule var9 = SwordModelsModule.getInstance();
      Minecraft var10 = Minecraft.getInstance();
      if (var9 != null && var9.shouldReplace(var2) && var0 == var10.player && !var0.isScoping()) {
         CustomItemModel var11 = var9.getModel();
         int var12 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         KnifeMesh var13 = SwordModels.getModelForVariant(var11, (boolean)var12);
         if (var13 == null) {
            return false;
         }

         HumanoidArm var14 = var1 == InteractionHand.MAIN_HAND ? var0.getMainArm() : var0.getMainArm().getOpposite();
         int var15 = var14 == HumanoidArm.RIGHT ? 1 : -1;
         if (var11.isAccessory()) {
            return renderWhackAnimation(var0, var1, var14, var15, var2, var3, var4, var5, var6, var7, var8, var9, var13);
         }

         WeaponAnimator var16 = var9.getAnimator(var1);
         if (var0.isUsingItem() || !var10.isWindowActive()) {
            var16.beginHold();
         }

         double var17 = TickTimer.getTickTime() + Math.clamp(var5, 0.0F, 1.0F) * 0.05;
         WeaponAnimator.Frame var19 = var16.update(var9.getStackToken(var1, var2), var11, var17, var3, var9.idleMotion.getValue());
         Matrix4f[] var20 = var9.getPoseBlender(var1).computePoses(var13, var11, var19, var15, var9.modelScale.getValue().floatValue(), var17);
         Vector3f var21 = new Vector3f(
            var15 * var9.handX.getValue().floatValue(), var9.handY.getValue().floatValue() + var19.bob, var9.handZ.getValue().floatValue()
         );
         Vector3f var22 = var20[0].transformPosition(new Vector3f(var15 * var11.offsetX, var11.offsetY, var11.offsetZ));
         var6.pushPose();

         try {
            applyViewModel(var6, var1, var15, new Vector3f(var22).add(var21).mul(fOV_SCALE, fOV_SCALE, 1.0F));
            var6.scale(fOV_SCALE, fOV_SCALE, 1.0F);
            var6.translate(var21.x, var21.y, var21.z);
            if (!var0.isInvisible()) {
               var6.pushPose();

               try {
                  PoseStackUtil.multiply(var6, var20[1]);
                  var6.scale(0.72F, 0.72F, 0.72F);
                  renderArm(var0, var1, var14, var15, var11, var6, var7, var8);
               } finally {
                  var6.popPose();
               }
            }

            PoseStackUtil.multiply(var6, var20[0]);

            for (int var23 = 0; var23 < var13.parts().size(); var23++) {
               KnifeMesh.Part var24 = var13.parts().get(var23);
               var6.pushPose();

               try {
                  PoseStackUtil.multiply(var6, var20[var23 + 2]);
                  HandBoundsTracker.trackVertices(var1, var15 < 0, var6, var24.vertices(), var15);
                  if (var24.name().equals("blade")) {
                     WeaponTrailRenderer.submitModelBlade(var1, var2, var24.vertices(), var15, var6, var3, var19.stage == 3, var7);
                  }

                  if (var24.tint()) {
                     float[] var25 = tINT_PALETTES[var9.resetSlot()];
                     int var26 = LightCoordsUtil.pack(Math.max(LightCoordsUtil.block(var8), 11), LightCoordsUtil.sky(var8));
                     var7.submitCustomGeometry(
                        var6,
                        getRenderType(SwordModels.galaxyLayerTextureId(0), var9.enchantmentGlint.getValue() && var2.hasFoil()),
                        (var6x, var7x) -> emitTintedVertices(var24, var6x, var7x, var26, var15, 0, var25, var17)
                     );

                     for (int var27 = 1; var27 <= 2; var27++) {
                        int var28 = var27;
                        var7.order(1)
                           .submitCustomGeometry(
                              var6,
                              RenderTypes.eyes(SwordModels.galaxyLayerTextureId(var27)),
                              (var7x, var8x) -> emitTintedVertices(var24, var7x, var8x, var26, var15, var28, var25, var17)
                           );
                     }
                  } else {
                     var7.submitCustomGeometry(
                        var6,
                        getRenderType(SwordModels.knifeTextureId(var11), var9.enchantmentGlint.getValue() && var2.hasFoil()),
                        (var3x, var4x) -> emitVertices(var24, var3x, var4x, var8, var15, false)
                     );
                  }
               } finally {
                  var6.popPose();
               }
            }

            return true;
         } finally {
            var6.popPose();
         }
      } else {
         return false;
      }
   }

   private static boolean renderWhackAnimation(
      AbstractClientPlayer var0,
      InteractionHand var1,
      HumanoidArm var2,
      int var3,
      ItemStack var4,
      float var5,
      float var6,
      float var7,
      PoseStack var8,
      SubmitNodeCollector var9,
      int var10,
      SwordModelsModule var11,
      KnifeMesh var12
   ) {
      int var13 = var0.isUsingItem() && var0.getUsedItemHand() == var1 && !SwordModelsModule.isSkinnedSword(var4) ? 1 : 0;
      if (var13 != 0) {
         var11.logUsePose(var1, var4);
      }

      if (var13 == 0 && !var0.isAutoSpinAttack() && SwingHelper.getAttackAnimation(var4).type() == SwingAnimationType.WHACK) {
         var8.pushPose();

         try {
            CustomItemModel var14 = var11.getModel();
            double var15 = TickTimer.getTickTime() + Math.clamp(var7, 0.0F, 1.0F) * 0.05;
            float var17 = 0.0F;
            HandPoseSample var18;
            if (var14 == CustomItemModel.lEDGER_NANO_S) {
               SpinAnimator var19 = var11.getSpinAnimator(var1);
               var19.update(SwingHelper.getSwingHand(var0) == var1, SwingHelper.getSwingProgressMillis(var0), var15);
               var17 = var3 * var19.getAngle(var15);
               int var20 = !SwordModelsModule.isSameWeapon(var4, var0.getItemInHand(var1)) ? 1 : 0;
               var18 = ViewModelTransforms.buildItemPose(
                  var3, var19.latchFade(var11.getStackToken(var1, var4), var6, (boolean)var20), var11.modelScale.getValue().floatValue()
               );
            } else {
               var18 = ViewModelTransforms.buildBlockPose(var3, var5, var6);
            }

            Vector3f var37 = new Vector3f(var3 * var11.handX.getValue().floatValue(), var11.handY.getValue().floatValue(), var11.handZ.getValue().floatValue());
            applyViewModel(var8, var1, var3, var18.weapon().getTranslation(new Vector3f()).add(var37));
            var8.translate(var37.x, var37.y, var37.z);
            if (var14 == CustomItemModel.lUXURY_WATCH) {
               WeaponAnimator var38 = var11.getAnimator(var1);
               if (!Minecraft.getInstance().isWindowActive()) {
                  var38.beginHold();
               }

               InspectAnimationBlender.BlendState var21 = var11.getInspectBlender(var1)
                  .update(var38.update(var11.getStackToken(var1, var4), var14, var15, var5, false), var15);
               var18 = ViewModelTransforms.blendToGuard(var18, var3, var21.phase(), var21.weight());
            }

            if (!var0.isInvisible()) {
               var8.pushPose();

               try {
                  PoseStackUtil.multiply(var8, var18.arm());
                  var8.scale(0.72F, 0.72F, 0.72F);
                  renderArm(var0, var1, var2, var3, var14, var8, var9, var10);
               } finally {
                  var8.popPose();
               }
            }

            PoseStackUtil.multiply(var8, var18.weapon());

            for (KnifeMesh.Part var40 : var12.parts()) {
               var8.pushPose();

               try {
                  if (var40.name().equals("case")) {
                     ViewModelTransforms.scaleAroundPivot(var8, var11.modelScale.getValue().floatValue());
                  }

                  int var22 = var14 != CustomItemModel.lEDGER_NANO_S && !var40.name().equals("case") ? var3 : 1;
                  HandBoundsTracker.trackVertices(var1, var3 < 0, var8, var40.vertices(), var22);
                  if (var17 != 0.0F && var40.name().equals("swivel")) {
                     var8.mulPose(ViewModelTransforms.rotateAroundZ(var40.pivotX(), var40.pivotY(), var40.pivotZ(), var17));
                  }

                  var9.submitCustomGeometry(
                     var8,
                     getRenderType(SwordModels.knifeTextureId(var14), var11.enchantmentGlint.getValue() && var4.hasFoil()),
                     (var3x, var4x) -> emitVertices(var40, var3x, var4x, var10, var22, false)
                  );
               } finally {
                  var8.popPose();
               }
            }

            return true;
         } finally {
            var8.popPose();
         }
      } else {
         return false;
      }
   }

   private static void applyViewModel(PoseStack var0, InteractionHand var1, int var2, Vector3f var3) {
      ViewModelModule var4 = ViewModelModule.getInstance();
      if (var4 != null && var4.isRuntimeActive()) {
         var4.applyTranslation(var0, var1, var2 < 0);
         var0.translate(var3.x, var3.y, var3.z);
         var4.applyRotationAndScale(var0, var1, var2 < 0);
         var0.translate(-var3.x, -var3.y, -var3.z);
      }
   }

   private static void renderArm(
      AbstractClientPlayer var0, InteractionHand var1, HumanoidArm var2, int var3, CustomItemModel var4, PoseStack var5, SubmitNodeCollector var6, int var7
   ) {
      AvatarRenderer var8 = (AvatarRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var0);
      var5.pushPose();

      try {
         int var9 = var0.getSkin().model() == PlayerModelType.SLIM ? 1 : 0;
         KnifeHandTransform.applyPalmTranslation(var5, var4, var3, (boolean)var9);
         Identifier var10 = var0.getSkin().body().texturePath();
         if (var2 == HumanoidArm.RIGHT) {
            var8.renderRightHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
         } else {
            var8.renderLeftHand(var5, var6, var7, var10, var0.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
         }

         PlayerModel var11 = (PlayerModel)var8.getModel();
         HandBoundsTracker.trackModelPart(var1, var3 < 0, var5, var2 == HumanoidArm.RIGHT ? var11.rightArm : var11.leftArm);
         FlameHandsRenderer.renderHand(var2, var5, var6, true);
      } finally {
         var5.popPose();
      }
   }

   private static void emitVertices(KnifeMesh.Part var0, Pose var1, VertexConsumer var2, int var3, int var4, boolean var5) {
      float[] var6 = var0.vertices();

      for (byte var7 = 0; var7 < var6.length; var7 += 32) {
         for (int var8 = 0; var8 < 4; var8++) {
            int var9 = var7 + KnifeMesh.vertexIndex(var4, var8) * 8;
            var2.addVertex(var1, var4 * var6[var9], var6[var9 + 1], var6[var9 + 2])
               .setColor(-1)
               .setUv(var5 ? var4 * var6[var9] : var6[var9 + 3], var5 ? var6[var9 + 1] : var6[var9 + 4])
               .setOverlay(OverlayTexture.NO_OVERLAY)
               .setLight(var3)
               .setNormal(var1, var4 * var6[var9 + 5], var6[var9 + 6], var6[var9 + 7]);
         }
      }
   }

   static void emitTintedVertices(KnifeMesh.Part var0, Pose var1, VertexConsumer var2, int var3, int var4, int var5, float[] var6, double var7) {
      float[] var9 = var0.vertices();
      float[] var10 = var0.finishUv();
      int[] var11 = var0.finishColor();
      float var12 = gLINT_UV_SCALE[var5];
      float var13 = (float)(var7 * uV_SCROLL_X[var5] % 1.0) + (var5 == 1 ? 0.37F : 0.0F);
      float var14 = (float)(var7 * uV_SCROLL_Y[var5] % 1.0);
      float var15 = (float)(var7 % 3600.0);

      for (byte var16 = 0; var16 < var9.length; var16 += 32) {
         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = var16 + KnifeMesh.vertexIndex(var4, var17) * 8;
            float var19 = var10[var18 / 4];
            float var20 = var10[var18 / 4 + 1];
            int var21;
            if (var5 == 0) {
               float var22 = 1.25F * (var11[var18 / 8] & 0xFF) / 255.0F;
               var21 = packColor(1.0F, var6[0] * var22, var6[1] * var22, var6[2] * var22);
            } else if (var5 == 1) {
               float var23 = 0.5F + 0.5F * (float)Math.sin(var20 * 7.0F - var15 * 1.3F);
               var21 = packColor(0.85F, var6[3] + (var6[6] - var6[3]) * var23, var6[4] + (var6[7] - var6[4]) * var23, var6[5] + (var6[8] - var6[5]) * var23);
            } else {
               var21 = packColor(0.75F + 0.25F * (float)Math.sin(var20 * 23.0F + var19 * 11.0F - var15 * 3.1F), 1.0F, 1.0F, 1.0F);
            }

            var2.addVertex(var1, var4 * var9[var18], var9[var18 + 1], var9[var18 + 2])
               .setColor(var21)
               .setUv(var19 * var12 + var13, var20 * var12 + var14)
               .setOverlay(OverlayTexture.NO_OVERLAY)
               .setLight(var3)
               .setNormal(var1, var4 * var9[var18 + 5], var9[var18 + 6], var9[var18 + 7]);
         }
      }
   }

   private static int packColor(float var0, float var1, float var2, float var3) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F) << 24
         | Math.round(Math.clamp(var1, 0.0F, 1.0F) * 255.0F) << 16
         | Math.round(Math.clamp(var2, 0.0F, 1.0F) * 255.0F) << 8
         | Math.round(Math.clamp(var3, 0.0F, 1.0F) * 255.0F);
   }

   private SwordModelRenderer() {
   }

   private static RenderType getRenderType(Identifier var0, boolean var1) {
      return var1 ? RenderTypes.entitySolidGlint(var0) : RenderTypes.entitySolid(var0);
   }

   private static RenderType unsupportedGlint() {
      throw new IllegalStateException("26.3 glint is part of the base pass");
   }
}
