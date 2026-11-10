package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class ba {
   private PlayerModel J;
   private PlayerModel c;
   private int D = -1;

   public void v() {
      this.J = this.c = null;
      this.D = -1;
   }

   public void Q() {
      if (this.D != nl.q || this.J == null) {
         EntityModelSet var1 = Minecraft.getInstance().getEntityModels();
         this.J = new PlayerModel(var1.bakeLayer(ModelLayers.PLAYER), false);
         this.c = new PlayerModel(var1.bakeLayer(ModelLayers.PLAYER_SLIM), true);
         this.D = nl.q;
      }
   }

   public void x(AvatarRenderState var1, n5 var2, n5.p[] var3, Vec3 var4, PoseStack var5, wh var6, int var7, boolean var8) {
      if (this.D == nl.q && this.J != null) {
         int var9 = var1.skin.model() == PlayerModelType.SLIM ? 1 : 0;
         PlayerModel var10 = var9 != 0 ? this.c : this.J;

         for (ModelPart var12 : var10.root().getAllParts()) {
            var12.resetPose();
            var12.visible = (boolean)1;
         }

         var10.hat.visible = var1.showHat;
         var10.jacket.visible = var1.showJacket;
         var10.leftSleeve.visible = var1.showLeftSleeve;
         var10.rightSleeve.visible = var1.showRightSleeve;
         var10.leftPants.visible = var1.showLeftPants;
         var10.rightPants.visible = var1.showRightPants;
         ModelPart[] var17 = new ModelPart[]{var10.head, var10.body, var10.rightArm, var10.leftArm, var10.rightLeg, var10.leftLeg};

         for (int var18 = 0; var18 < var3.length; var18++) {
            n5.p var13 = var3[var18];
            if (!(var13.alpha() <= 0.001) && !(var13.scale() <= 0.001) && !(var13.dissolve() >= 1.0)) {
               var5.pushPose();

               try {
                  I(var5, var13, var2.y, var18, (boolean)var9, var4);
                  w7.V(var17[var18], var5, var6, var1.skin.body().texturePath(), var7, var13.alpha(), var13.dissolve(), var8);
               } finally {
                  var5.popPose();
               }
            }
         }
      }
   }

   public static void I(PoseStack var0, n5.p var1, double var2, int var4, boolean var5, Vec3 var6) {
      var0.translate(var1.position().x - var6.x, var1.position().y - var6.y, var1.position().z - var6.z);
      if (var1.orientation() != null) {
         qd.i(var0, var1.orientation());
      } else {
         qd.i(var0, new Quaternionf().rotateY((float)(Math.PI - var2)).rotateXYZ((float)var1.rx(), (float)var1.ry(), (float)var1.rz()));
      }

      float var7 = (float)(0.9375 * var1.scale());
      if (var1.stretch() == null) {
         var0.scale(-var7, -var7, var7);
      } else {
         var0.scale((float)(-var7 * var1.stretch().x), (float)(-var7 * var1.stretch().y), (float)(var7 * var1.stretch().z));
      }

      var0.translate(-n5.H(var4, var5) / 16.0, -n5.a[var4][1] / 16.0, 0.0);
   }
}
