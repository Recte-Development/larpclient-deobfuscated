package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelExtractor.class)
public abstract class qd349d6bd2821f54e {
   @Inject(method = "isEntityVisible", at = @At("HEAD"), cancellable = true)
   private void R(Entity var1, Frustum var2, double var3, double var5, double var7, float var9, long var10, CallbackInfoReturnable<Boolean> var12) {
      if (tq.f() && !FreecamModule.p() && var1 == Minecraft.getInstance().player) {
         var12.setReturnValue(false);
      }
   }

   @Redirect(
      method = "isEntityVisible",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;isSectionCompiledAndVisible(Lnet/minecraft/core/BlockPos;J)Z")
   )
   private boolean u(LevelRenderer var1, BlockPos var2, long var3) {
      return tq.f() || var1.isSectionCompiledAndVisible(var2, var3);
   }
}
