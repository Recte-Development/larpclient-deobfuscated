package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qdb1833d4e0bbe2ef {
   @Inject(method = {"crit", "magicCrit"}, at = @At("HEAD"), cancellable = true)
   private void H(Entity var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.q();
      if (var3 != null && var3.isOperational()) {
         var2.cancel();
      }
   }
}
