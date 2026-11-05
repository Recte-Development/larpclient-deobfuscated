package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.x2;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class q768db146e6b946f0 {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void i(CallbackInfoReturnable<PlayerSkin> var1) {
      var1.setReturnValue(x2.X(((PlayerInfo)this).getProfile().id(), (PlayerSkin)var1.getReturnValue()));
   }
}
