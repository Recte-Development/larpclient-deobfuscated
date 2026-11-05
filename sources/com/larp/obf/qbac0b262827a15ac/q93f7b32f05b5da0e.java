package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.t7;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class q93f7b32f05b5da0e {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void t(CallbackInfoReturnable<PlayerSkin> var1) {
      var1.setReturnValue(t7.n(((PlayerInfo)this).getProfile().id(), (PlayerSkin)var1.getReturnValue()));
   }
}
