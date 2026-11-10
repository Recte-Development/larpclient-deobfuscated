package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q3f4e251a0dcf56a9;

import com.larpclient.WalkStyleModule;
import com.larpclient.jc;
import com.larpclient.ph;
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
public abstract class q5f6f3807734fd45b implements jc {
   @Unique
   private ph Z;

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.Z != null) {
         this.Z.W();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, AvatarRenderState var2) {
      if (this.Z == null) {
         this.Z = new ph((PlayerModel)this);
      }

      this.Z.b(var1, var2);
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void M(AvatarRenderState var1, CallbackInfo var2) {
      if (this.Z != null) {
         this.Z.W();
      }
   }

   @Inject(
      method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
      at = @At("RETURN")
   )
   private void v(AvatarRenderState var1, HumanoidArm var2, PoseStack var3, CallbackInfo var4) {
      if (this.Z != null) {
         this.Z.n(var2, var3);
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void z(AvatarRenderState var1, CallbackInfo var2) {
      WalkStyleModule var3 = WalkStyleModule.V();
      if (var3 != null) {
         PlayerModel var4 = (PlayerModel)this;
         float[] var5 = var3.b(var4, var1);
         if (var5 != null) {
            if (this.Z == null) {
               this.Z = new ph(var4);
            }

            this.Z.b(var5, var1);
         }
      }
   }
}
