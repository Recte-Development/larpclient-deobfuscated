package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.render.FreecamModule;
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
public abstract class LevelExtractorMixin_2 {
   @Inject(method = "isEntityVisible", at = @At("HEAD"), cancellable = true)
   private void h(Entity var1, Frustum var2, double var3, double var5, double var7, float var9, long var10, CallbackInfoReturnable<Boolean> var12) {
      if (ViewController.isFreecam() && !FreecamModule.shouldShowBody() && var1 == Minecraft.getInstance().player) {
         var12.setReturnValue(false);
      }
   }

   @Redirect(
      method = "isEntityVisible",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;isSectionCompiledAndVisible(Lnet/minecraft/core/BlockPos;J)Z")
   )
   private boolean O(LevelRenderer var1, BlockPos var2, long var3) {
      return ViewController.isFreecam() || var1.isSectionCompiledAndVisible(var2, var3);
   }
}
