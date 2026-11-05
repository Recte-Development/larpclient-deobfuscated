package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q60d951b25740ad52;

import com.larpclient.WalkStyleModule;
import com.larpclient._b;
import com.larpclient.cs;
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
public abstract class qce03845eac255664 implements _b {
   @Unique
   private cs v;

   @Unique
   @Override
   public void larpRestoreWalkRig() {
      if (this.v != null) {
         this.v.i();
      }
   }

   @Unique
   @Override
   public void larpApplyJoints(float[] var1, AvatarRenderState var2) {
      if (this.v == null) {
         this.v = new cs((PlayerModel)this);
      }

      this.v.o(var1, var2);
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void h(AvatarRenderState var1, CallbackInfo var2) {
      if (this.v != null) {
         this.v.i();
      }
   }

   @Inject(
      method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
      at = @At("RETURN")
   )
   private void A(AvatarRenderState var1, HumanoidArm var2, PoseStack var3, CallbackInfo var4) {
      if (this.v != null) {
         this.v.Y(var2, var3);
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void x(AvatarRenderState var1, CallbackInfo var2) {
      WalkStyleModule var3 = WalkStyleModule.q();
      if (var3 != null) {
         PlayerModel var4 = (PlayerModel)this;
         float[] var5 = var3.h(var4, var1);
         if (var5 != null) {
            if (this.v == null) {
               this.v = new cs(var4);
            }

            this.v.o(var5, var1);
         }
      }
   }
}
