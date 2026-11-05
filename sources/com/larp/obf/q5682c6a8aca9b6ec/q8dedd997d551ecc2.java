package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.KillFXModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public final class q8dedd997d551ecc2 {
   @Inject(method = "extractEntity", at = @At("RETURN"))
   private void j(Entity var1, float var2, CallbackInfoReturnable<EntityRenderState> var3) {
      KillFXModule var4 = KillFXModule.X();
      if (var4 != null) {
         var4.Z(var1, (EntityRenderState)var3.getReturnValue());
      }
   }

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void v(
      EntityRenderState var1, CameraRenderState var2, double var3, double var5, double var7, PoseStack var9, SubmitNodeCollector var10, CallbackInfo var11
   ) {
      KillFXModule var12 = KillFXModule.X();
      if (var12 != null && var12.B(var1)) {
         var11.cancel();
      }
   }
}
