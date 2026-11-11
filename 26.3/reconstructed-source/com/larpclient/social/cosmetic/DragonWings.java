package com.larpclient.social.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class DragonWings {
   public static final Identifier dRAGON_TEXTURE = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
   public static final float mODEL_SCALE = 0.15F;
   public static final float wING_OFFSET_X = 2.25F;
   public static final float wING_OFFSET_Y = 3.4F;
   public static final float wING_OFFSET_Y_ARMORED = 4.2F;
   public static final int fLAP_PERIOD_TICKS = 48;
   public static final float mAX_PITCH = 50.0F;
   public static final float mAX_YAW = 64.0F;

   public static float wingOffsetY(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float wingOffsetZ(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float flapSpeed(float var0, float var1, float var2, float var3) {
      float var4 = clamp01(var0);
      float var5 = clamp01(var1);
      float var6 = clamp01(var2);
      float var7 = clamp01(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return flapSpeed(var8, 0.09817477F, var7);
   }

   public static DragonWings.WingAngles computePose(float var0, float var1, float var2, float var3, float var4) {
      float var5 = clamp01(var1);
      float var6 = clamp01(var2);
      float var7 = clamp01(var3);
      float var8 = clamp01(var4);
      float var9 = flapSpeed(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = flapSpeed(var9, -85.0F, var8);
      var10 = flapSpeed(var10, -8.0F, var8);
      var11 = flapSpeed(var11, -4.0F, var8);
      var12 = flapSpeed(var12, -5.0F, var8);
      var13 = flapSpeed(var13, 0.1F, var8);
      return new DragonWings.WingAngles(
         toRadians(var9) - (float)Math.cos(var0) * 0.2F * var13,
         toRadians(var10) + (float)Math.sin(var0 - 0.6F) * 0.24F * var13,
         toRadians(var11) - (float)(Math.sin(var0) + 0.125) * 0.8F * var13,
         toRadians(var12) - (float)(Math.sin(var0 + 2.0F) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float flapSpeed(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float phaseOffset(int var0) {
      double var1 = var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static RenderType renderType(int var0) {
      return var0 >>> 24 == 255 ? RenderTypes.entityCutout(dRAGON_TEXTURE) : RenderTypes.entityTranslucent(dRAGON_TEXTURE);
   }

   private static float clamp01(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float flapSpeed(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float toRadians(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private DragonWings() {
   }

   public record WingAngles(float wingX, float wingY, float wingZ, float tipZ) {
   }

   public static final class WingModel {
      private final ModelPart root;
      private final ModelPart innerBone;
      private final ModelPart outerBone;
      private final ModelPart membrane;

      public WingModel(ModelPart var1) {
         ModelPart var2 = var1.getChild("body");
         this.root = var2.getChild("left_wing");
         this.innerBone = this.root.getChild("left_wing_tip");
         this.outerBone = var2.getChild("right_wing");
         this.membrane = this.outerBone.getChild("right_wing_tip");
      }

      public static DragonWings.WingModel createLayer() {
         return new DragonWings.WingModel(EnderDragonModel.createBodyLayer().bakeRoot());
      }

      public ModelPart setupAnim(int var1) {
         return var1 > 0 ? this.root : this.outerBone;
      }

      public void render(DragonWings.WingAngles var1) {
         this.root.setPos(0.0F, 0.0F, 0.0F);
         this.outerBone.setPos(0.0F, 0.0F, 0.0F);
         this.root.setRotation(var1.wingX(), var1.wingY(), var1.wingZ());
         this.outerBone.setRotation(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.innerBone.setRotation(0.0F, 0.0F, var1.tipZ());
         this.membrane.setRotation(0.0F, 0.0F, -var1.tipZ());
      }

      public static void resetPose(PoseStack var0, int var1, float var2, boolean var3) {
         var0.translate(var1 * 2.25F / 16.0F, DragonWings.wingOffsetY(var3) / 16.0F, DragonWings.wingOffsetZ(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.scale(var4, var4, var4);
      }

      public void getRoot(PoseStack var1, VertexConsumer var2, float var3, boolean var4, int var5, int var6) {
         for (byte var7 = -1; var7 <= 1; var7 += 2) {
            var1.pushPose();

            try {
               resetPose(var1, var7, var3, var4);
               this.setupAnim(var7).render(var1, var2, var5, OverlayTexture.NO_OVERLAY, var6);
            } finally {
               var1.popPose();
            }
         }
      }
   }
}
