package com.larpclient.mixin;

import com.larpclient.module.render.HitFXModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_7 {
   @Inject(method = {"crit", "magicCrit"}, at = @At("HEAD"), cancellable = true)
   private void C(Entity var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.getInstance();
      if (var3 != null && var3.isOperational()) {
         var2.cancel();
      }
   }
}
