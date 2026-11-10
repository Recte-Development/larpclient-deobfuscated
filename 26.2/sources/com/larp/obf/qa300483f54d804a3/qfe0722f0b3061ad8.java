package com.larp.obf.qa300483f54d804a3;

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
public final class qfe0722f0b3061ad8 {
   @Inject(method = "extractEntity", at = @At("RETURN"))
   private void V(Entity var1, float var2, CallbackInfoReturnable<EntityRenderState> var3) {
      KillFXModule var4 = KillFXModule.t();
      if (var4 != null && var4.F()) {
         var4.I(var1, (EntityRenderState)var3.getReturnValue());
      }
   }

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/client/renderer/state/level/CameraRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void w(
      EntityRenderState var1, CameraRenderState var2, double var3, double var5, double var7, PoseStack var9, SubmitNodeCollector var10, CallbackInfo var11
   ) {
      KillFXModule var12 = KillFXModule.t();
      if (var12 != null && var12.F() && var12.B(var1)) {
         var11.cancel();
      }
   }
}
