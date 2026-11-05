package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import com.larpclient.WalkStyleModule;
import com.larpclient.pv;
import com.larpclient.xe;
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
public abstract class q8b81559e80e82b6b implements xe {
   @Unique
   private pv v;

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.v != null) {
         this.v.q();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, AvatarRenderState var2) {
      if (this.v == null) {
         this.v = new pv((PlayerModel)this);
      }

      this.v.O(var1, var2);
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void X(AvatarRenderState var1, CallbackInfo var2) {
      if (this.v != null) {
         this.v.q();
      }
   }

   @Inject(
      method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
      at = @At("RETURN")
   )
   private void L(AvatarRenderState var1, HumanoidArm var2, PoseStack var3, CallbackInfo var4) {
      if (this.v != null) {
         this.v.n(var2, var3);
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void Q(AvatarRenderState var1, CallbackInfo var2) {
      WalkStyleModule var3 = WalkStyleModule.f();
      if (var3 != null) {
         PlayerModel var4 = (PlayerModel)this;
         float[] var5 = var3.z(var4, var1);
         if (var5 != null) {
            if (this.v == null) {
               this.v = new pv(var4);
            }

            this.v.O(var5, var1);
         }
      }
   }
}
