package com.larpclient.render.model;

import com.larpclient.gui.misc.PlayerPreviewAnimator;
import com.larpclient.render.entity.LegTrail;
import com.larpclient.social.cosmetic.AccountCosmeticsRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model.Simple;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class AvatarPoseModel extends Simple {
   private final HumanoidModel<HumanoidRenderState> humanoidModel;
   private final ModelPart head;
   private final ModelPart body;
   private final ModelPart rightArm;
   private final ModelPart leftArm;
   private final ModelPart rightLeg;
   private final ModelPart leftLeg;
   private final AvatarRenderState renderState = new AvatarRenderState();
   private AccountCosmeticsRenderer.CosmeticsSnapshot appearance;
   private float rootRoll;
   private float rootOffsetX;
   private float rootOffsetY;

   public AvatarPoseModel(ModelPart var1) {
      super(var1, RenderTypes::entityTranslucent);
      this.humanoidModel = new HumanoidModel(var1, RenderTypes::entityTranslucent);
      this.head = var1.getChild("head");
      this.body = var1.getChild("body");
      this.rightArm = var1.getChild("right_arm");
      this.leftArm = var1.getChild("left_arm");
      this.rightLeg = var1.getChild("right_leg");
      this.leftLeg = var1.getChild("left_leg");
   }

   public void applyPose(PlayerPreviewAnimator.Pose var1) {
      this.applyBlendedPose(var1, null);
   }

   public void applyBlendedPose(PlayerPreviewAnimator.Pose var1, PlayerPreviewAnimator.LimbPose var2) {
      this.renderState.ageInTicks = var1.ageInTicks();
      this.renderState.walkAnimationPos = var1.walkPosition();
      this.renderState.walkAnimationSpeed = var1.walkSpeed();
      this.renderState.yRot = (float)Math.toDegrees(var1.headYaw());
      this.renderState.xRot = (float)Math.toDegrees(var1.headPitch());
      this.renderState.isCrouching = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      AccountCosmeticsRenderer.applyCapePhysics(this.renderState, var1);
      this.humanoidModel.setupAnim(this.renderState);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         blendLimb(this.rightArm, var2.rightArmX(), var2.rightArmZ(), var3);
         blendLimb(this.leftArm, var2.leftArmX(), var2.leftArmZ(), var3);
         blendLimb(this.rightLeg, var2.rightLegX(), var2.rightLegZ(), var3);
         blendLimb(this.leftLeg, var2.leftLegX(), var2.leftLegZ(), var3);
         this.head.zRot = this.head.zRot + var2.headRoll() * var3;
      }
   }

   private static void blendLimb(ModelPart var0, float var1, float var2, float var3) {
      var0.xRot = var0.xRot + (var1 - var0.xRot) * var3;
      var0.zRot = var0.zRot + (var2 - var0.zRot) * var3;
   }

   public void setRootTransform(float var1, float var2, float var3) {
      this.rootRoll = var1;
      this.rootOffsetX = var2;
      this.rootOffsetY = var3;
   }

   public void applyRootTransform(PoseStack var1) {
      if (this.rootRoll != 0.0F || this.rootOffsetX != 0.0F || this.rootOffsetY != 0.0F) {
         var1.translate(this.rootOffsetX, this.rootOffsetY, 0.0F);
         var1.rotateAround(Axis.ZP.rotation(this.rootRoll), 0.0F, -1.251F, 0.0F);
      }
   }

   AvatarRenderState getRenderState() {
      return this.renderState;
   }

   ModelPart getHead() {
      return this.head;
   }

   ModelPart getBody() {
      return this.body;
   }

   ModelPart getRightLeg() {
      return this.rightLeg;
   }

   ModelPart getLeftLeg() {
      return this.leftLeg;
   }

   void setAppearance(AccountCosmeticsRenderer.CosmeticsSnapshot var1) {
      this.appearance = var1;
      LegTrail var2 = var1 == null ? null : var1.korblox();
      this.head.visible = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.rightLeg.visible = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.leftLeg.visible = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   AccountCosmeticsRenderer.CosmeticsSnapshot applyRootTransform() {
      return this.appearance;
   }
}
