package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model.Simple;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class w_ extends Simple {
   private final HumanoidModel<HumanoidRenderState> b;
   private final ModelPart F;
   private final ModelPart o;
   private final ModelPart i;
   private final ModelPart v;
   private final ModelPart l;
   private final ModelPart X;
   private final AvatarRenderState u = new AvatarRenderState();
   private pa.h N;
   private float G;
   private float W;
   private float m;

   public w_(ModelPart var1) {
      super(var1, RenderTypes::entityTranslucent);
      this.b = new HumanoidModel(var1, RenderTypes::entityTranslucent);
      this.F = var1.getChild("head");
      this.o = var1.getChild("body");
      this.i = var1.getChild("right_arm");
      this.v = var1.getChild("left_arm");
      this.l = var1.getChild("right_leg");
      this.X = var1.getChild("left_leg");
   }

   public void J(zt.n var1) {
      this.x(var1, null);
   }

   public void x(zt.n var1, zt.p var2) {
      this.u.ageInTicks = var1.ageInTicks();
      this.u.walkAnimationPos = var1.walkPosition();
      this.u.walkAnimationSpeed = var1.walkSpeed();
      this.u.yRot = (float)Math.toDegrees(var1.headYaw());
      this.u.xRot = (float)Math.toDegrees(var1.headPitch());
      this.u.isCrouching = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      pa.T(this.u, var1);
      this.b.setupAnim(this.u);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         n(this.i, var2.rightArmX(), var2.rightArmZ(), var3);
         n(this.v, var2.leftArmX(), var2.leftArmZ(), var3);
         n(this.l, var2.rightLegX(), var2.rightLegZ(), var3);
         n(this.X, var2.leftLegX(), var2.leftLegZ(), var3);
         this.F.zRot = this.F.zRot + var2.headRoll() * var3;
      }
   }

   private static void n(ModelPart var0, float var1, float var2, float var3) {
      var0.xRot = var0.xRot + (var1 - var0.xRot) * var3;
      var0.zRot = var0.zRot + (var2 - var0.zRot) * var3;
   }

   public void h(float var1, float var2, float var3) {
      this.G = var1;
      this.W = var2;
      this.m = var3;
   }

   public void n(PoseStack var1) {
      if (this.G != 0.0F || this.W != 0.0F || this.m != 0.0F) {
         var1.translate(this.W, this.m, 0.0F);
         var1.rotateAround(Axis.ZP.rotation(this.G), 0.0F, -1.251F, 0.0F);
      }
   }

   AvatarRenderState d() {
      return this.u;
   }

   ModelPart O() {
      return this.F;
   }

   ModelPart r() {
      return this.o;
   }

   ModelPart G() {
      return this.l;
   }

   ModelPart N() {
      return this.X;
   }

   void r(pa.h var1) {
      this.N = var1;
      u3 var2 = var1 == null ? null : var1.korblox();
      this.F.visible = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.l.visible = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.X.visible = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   pa.h y() {
      return this.N;
   }
}
