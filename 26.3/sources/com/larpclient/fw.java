package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model.Simple;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class fw extends Simple {
   private final HumanoidModel<HumanoidRenderState> b;
   private final ModelPart s;
   private final ModelPart n;
   private final ModelPart I;
   private final ModelPart B;
   private final ModelPart F;
   private final ModelPart z;
   private final AvatarRenderState C = new AvatarRenderState();
   private m.a k;
   private float J;
   private float u;
   private float M;

   public fw(ModelPart var1) {
      super(var1, RenderTypes::entityTranslucent);
      this.b = new HumanoidModel(var1, RenderTypes::entityTranslucent);
      this.s = var1.getChild("head");
      this.n = var1.getChild("body");
      this.I = var1.getChild("right_arm");
      this.B = var1.getChild("left_arm");
      this.F = var1.getChild("right_leg");
      this.z = var1.getChild("left_leg");
   }

   public void m(o5.j var1) {
      this.H(var1, null);
   }

   public void H(o5.j var1, o5.n var2) {
      this.C.ageInTicks = var1.ageInTicks();
      this.C.walkAnimationPos = var1.walkPosition();
      this.C.walkAnimationSpeed = var1.walkSpeed();
      this.C.yRot = (float)Math.toDegrees(var1.headYaw());
      this.C.xRot = (float)Math.toDegrees(var1.headPitch());
      this.C.isCrouching = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      m.I(this.C, var1);
      this.b.setupAnim(this.C);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         L(this.I, var2.rightArmX(), var2.rightArmZ(), var3);
         L(this.B, var2.leftArmX(), var2.leftArmZ(), var3);
         L(this.F, var2.rightLegX(), var2.rightLegZ(), var3);
         L(this.z, var2.leftLegX(), var2.leftLegZ(), var3);
         this.s.zRot = this.s.zRot + var2.headRoll() * var3;
      }
   }

   private static void L(ModelPart var0, float var1, float var2, float var3) {
      var0.xRot = var0.xRot + (var1 - var0.xRot) * var3;
      var0.zRot = var0.zRot + (var2 - var0.zRot) * var3;
   }

   public void j(float var1, float var2, float var3) {
      this.J = var1;
      this.u = var2;
      this.M = var3;
   }

   public void q(PoseStack var1) {
      if (this.J != 0.0F || this.u != 0.0F || this.M != 0.0F) {
         var1.translate(this.u, this.M, 0.0F);
         var1.rotateAround(Axis.ZP.rotation(this.J), 0.0F, -1.251F, 0.0F);
      }
   }

   AvatarRenderState a() {
      return this.C;
   }

   ModelPart E() {
      return this.s;
   }

   ModelPart G() {
      return this.n;
   }

   ModelPart b() {
      return this.F;
   }

   ModelPart g() {
      return this.z;
   }

   void K(m.a var1) {
      this.k = var1;
      a1 var2 = var1 == null ? null : var1.korblox();
      this.s.visible = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.F.visible = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.z.visible = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   m.a q() {
      return this.k;
   }
}
