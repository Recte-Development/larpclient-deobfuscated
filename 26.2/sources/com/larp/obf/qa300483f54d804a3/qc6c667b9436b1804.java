package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qc6c667b9436b1804 {
   @Inject(method = {"crit", "magicCrit"}, at = @At("HEAD"), cancellable = true)
   private void e(Entity var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.N();
      if (var3 != null && var3.E()) {
         var2.cancel();
      }
   }
}
