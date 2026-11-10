package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model.Simple;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class g3 extends Simple {
   private final HumanoidModel<HumanoidRenderState> B;
   private final ModelPart i;
   private final ModelPart t;
   private final ModelPart W;
   private final ModelPart p;
   private final ModelPart d;
   private final ModelPart I;
   private final AvatarRenderState s = new AvatarRenderState();
   private c4.w V;
   private float C;
   private float y;
   private float H;

   public g3(ModelPart var1) {
      super(var1, RenderTypes::entityTranslucent);
      this.B = new HumanoidModel(var1, RenderTypes::entityTranslucent);
      this.i = var1.getChild("head");
      this.t = var1.getChild("body");
      this.W = var1.getChild("right_arm");
      this.p = var1.getChild("left_arm");
      this.d = var1.getChild("right_leg");
      this.I = var1.getChild("left_leg");
   }

   public void V(tx.u var1) {
      this.F(var1, null);
   }

   public void F(tx.u var1, tx.g var2) {
      this.s.ageInTicks = var1.ageInTicks();
      this.s.walkAnimationPos = var1.walkPosition();
      this.s.walkAnimationSpeed = var1.walkSpeed();
      this.s.yRot = (float)Math.toDegrees(var1.headYaw());
      this.s.xRot = (float)Math.toDegrees(var1.headPitch());
      this.s.isCrouching = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      c4.V(this.s, var1);
      this.B.setupAnim(this.s);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         E(this.W, var2.rightArmX(), var2.rightArmZ(), var3);
         E(this.p, var2.leftArmX(), var2.leftArmZ(), var3);
         E(this.d, var2.rightLegX(), var2.rightLegZ(), var3);
         E(this.I, var2.leftLegX(), var2.leftLegZ(), var3);
         this.i.zRot = this.i.zRot + var2.headRoll() * var3;
      }
   }

   private static void E(ModelPart var0, float var1, float var2, float var3) {
      var0.xRot = var0.xRot + (var1 - var0.xRot) * var3;
      var0.zRot = var0.zRot + (var2 - var0.zRot) * var3;
   }

   public void r(float var1, float var2, float var3) {
      this.C = var1;
      this.y = var2;
      this.H = var3;
   }

   public void s(PoseStack var1) {
      if (this.C != 0.0F || this.y != 0.0F || this.H != 0.0F) {
         var1.translate(this.y, this.H, 0.0F);
         var1.rotateAround(Axis.ZP.rotation(this.C), 0.0F, -1.251F, 0.0F);
      }
   }

   AvatarRenderState U() {
      return this.s;
   }

   ModelPart i() {
      return this.i;
   }

   ModelPart J() {
      return this.t;
   }

   ModelPart d() {
      return this.d;
   }

   ModelPart h() {
      return this.I;
   }

   void S(c4.w var1) {
      this.V = var1;
      l8 var2 = var1 == null ? null : var1.korblox();
      this.i.visible = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.d.visible = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.I.visible = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   c4.w b() {
      return this.V;
   }
}
