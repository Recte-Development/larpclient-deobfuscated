package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model.Simple;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class mn extends Simple {
   private final HumanoidModel<HumanoidRenderState> C;
   private final ModelPart j;
   private final ModelPart R;
   private final ModelPart m;
   private final ModelPart x;
   private final ModelPart r;
   private final ModelPart H;
   private final AvatarRenderState y = new AvatarRenderState();
   private e8.b K;
   private float O;
   private float X;
   private float z;

   public mn(ModelPart var1) {
      super(var1, RenderTypes::entityTranslucent);
      this.C = new HumanoidModel(var1, RenderTypes::entityTranslucent);
      this.j = var1.getChild("head");
      this.R = var1.getChild("body");
      this.m = var1.getChild("right_arm");
      this.x = var1.getChild("left_arm");
      this.r = var1.getChild("right_leg");
      this.H = var1.getChild("left_leg");
   }

   public void P(og.i var1) {
      this.o(var1, null);
   }

   public void o(og.i var1, og.a var2) {
      this.y.ageInTicks = var1.ageInTicks();
      this.y.walkAnimationPos = var1.walkPosition();
      this.y.walkAnimationSpeed = var1.walkSpeed();
      this.y.yRot = (float)Math.toDegrees(var1.headYaw());
      this.y.xRot = (float)Math.toDegrees(var1.headPitch());
      this.y.isCrouching = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      e8.d(this.y, var1);
      this.C.setupAnim(this.y);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         d(this.m, var2.rightArmX(), var2.rightArmZ(), var3);
         d(this.x, var2.leftArmX(), var2.leftArmZ(), var3);
         d(this.r, var2.rightLegX(), var2.rightLegZ(), var3);
         d(this.H, var2.leftLegX(), var2.leftLegZ(), var3);
         this.j.zRot = this.j.zRot + var2.headRoll() * var3;
      }
   }

   private static void d(ModelPart var0, float var1, float var2, float var3) {
      var0.xRot = var0.xRot + (var1 - var0.xRot) * var3;
      var0.zRot = var0.zRot + (var2 - var0.zRot) * var3;
   }

   public void y(float var1, float var2, float var3) {
      this.O = var1;
      this.X = var2;
      this.z = var3;
   }

   public void s(PoseStack var1) {
      if (this.O != 0.0F || this.X != 0.0F || this.z != 0.0F) {
         var1.translate(this.X, this.z, 0.0F);
         var1.rotateAround(Axis.ZP.rotation(this.O), 0.0F, -1.251F, 0.0F);
      }
   }

   AvatarRenderState K() {
      return this.y;
   }

   ModelPart a() {
      return this.j;
   }

   ModelPart z() {
      return this.R;
   }

   ModelPart I() {
      return this.r;
   }

   ModelPart t() {
      return this.H;
   }

   void L(e8.b var1) {
      this.K = var1;
      fd var2 = var1 == null ? null : var1.korblox();
      this.j.visible = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.r.visible = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.H.visible = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   e8.b s() {
      return this.K;
   }
}
