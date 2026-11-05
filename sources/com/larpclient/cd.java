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

public final class cd {
   private PlayerModel k;
   private PlayerModel B;
   private int x = -1;

   public void O() {
      this.k = this.B = null;
      this.x = -1;
   }

   public void F() {
      if (this.x != m5.k || this.k == null) {
         EntityModelSet var1 = Minecraft.getInstance().getEntityModels();
         this.k = new PlayerModel(var1.bakeLayer(ModelLayers.PLAYER), false);
         this.B = new PlayerModel(var1.bakeLayer(ModelLayers.PLAYER_SLIM), true);
         this.x = m5.k;
      }
   }

   public void Z(AvatarRenderState var1, ta var2, ta.y[] var3, Vec3 var4, PoseStack var5, xh var6, int var7, boolean var8) {
      if (this.x == m5.k && this.k != null) {
         int var9 = var1.skin.model() == PlayerModelType.SLIM ? 1 : 0;
         PlayerModel var10 = var9 != 0 ? this.B : this.k;

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
            ta.y var13 = var3[var18];
            if (!(var13.alpha() <= 0.001) && !(var13.scale() <= 0.001) && !(var13.dissolve() >= 1.0)) {
               var5.pushPose();

               try {
                  P(var5, var13, var2.v, var18, (boolean)var9, var4);
                  tj.t(var17[var18], var5, var6, var1.skin.body().texturePath(), var7, var13.alpha(), var13.dissolve(), var8);
               } finally {
                  var5.popPose();
               }
            }
         }
      }
   }

   public static void P(PoseStack var0, ta.y var1, double var2, int var4, boolean var5, Vec3 var6) {
      var0.translate(var1.position().x - var6.x, var1.position().y - var6.y, var1.position().z - var6.z);
      if (var1.orientation() != null) {
         u.v(var0, var1.orientation());
      } else {
         u.v(var0, new Quaternionf().rotateY((float)(Math.PI - var2)).rotateXYZ((float)var1.rx(), (float)var1.ry(), (float)var1.rz()));
      }

      float var7 = (float)(0.9375 * var1.scale());
      if (var1.stretch() == null) {
         var0.scale(-var7, -var7, var7);
      } else {
         var0.scale((float)(-var7 * var1.stretch().x), (float)(-var7 * var1.stretch().y), (float)(var7 * var1.stretch().z));
      }

      var0.translate(-ta.Z(var4, var5) / 16.0, -ta.Q[var4][1] / 16.0, 0.0);
   }
}
