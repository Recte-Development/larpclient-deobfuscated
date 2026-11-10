package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
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
public abstract class q0b2bdc4d9d39b5c2 {
   @Inject(method = "isEntityVisible", at = @At("HEAD"), cancellable = true)
   private void R(Entity var1, Frustum var2, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (p_.V() && !FreecamModule.Z() && var1 == Minecraft.getInstance().player) {
         var9.setReturnValue(false);
      }
   }

   @Redirect(
      method = "isEntityVisible",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;isSectionCompiledAndVisible(Lnet/minecraft/core/BlockPos;)Z")
   )
   private boolean h(LevelRenderer var1, BlockPos var2) {
      return p_.V() || var1.isSectionCompiledAndVisible(var2);
   }
}
