package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qd17a773d676c1f77 {
   @Inject(method = {"crit", "magicCrit"}, at = @At("HEAD"), cancellable = true)
   private void C(Entity var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.U();
      if (var3 != null && var3.A()) {
         var2.cancel();
      }
   }
}
