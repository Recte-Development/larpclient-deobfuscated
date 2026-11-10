package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q4ad210cb713c6dfc;

import com.larpclient.WalkStyleModule;
import com.larpclient.lf;
import com.larpclient.zm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class qb6fcfa4b4182d6a0 implements lf {
   @Unique
   private zm J;

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.J != null) {
         this.J.G();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, AvatarRenderState var2) {
      if (this.J == null) {
         this.J = new zm((PlayerModel)this);
      }

      this.J.F(var1, var2);
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void O(AvatarRenderState var1, CallbackInfo var2) {
      if (this.J != null) {
         this.J.G();
      }
   }

   @Inject(
      method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
      at = @At("RETURN")
   )
   private void e(AvatarRenderState var1, HumanoidArm var2, PoseStack var3, CallbackInfo var4) {
      if (this.J != null) {
         this.J.b(var2, var3);
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void D(AvatarRenderState var1, CallbackInfo var2) {
      WalkStyleModule var3 = WalkStyleModule.Z();
      if (var3 != null) {
         PlayerModel var4 = (PlayerModel)this;
         float[] var5 = var3.w(var4, var1);
         if (var5 != null) {
            if (this.J == null) {
               this.J = new zm(var4);
            }

            this.J.F(var5, var1);
         }
      }
   }
}
