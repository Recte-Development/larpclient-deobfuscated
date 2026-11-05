package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qd833b2c6b18c25dc {
   @Inject(method = {"crit", "magicCrit"}, at = @At("HEAD"), cancellable = true)
   private void S(Entity var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.y();
      if (var3 != null && var3.isOperational()) {
         var2.cancel();
      }
   }
}
