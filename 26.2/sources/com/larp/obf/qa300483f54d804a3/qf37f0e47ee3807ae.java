package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ci;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class qf37f0e47ee3807ae {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void V(CallbackInfoReturnable<PlayerSkin> var1) {
      var1.setReturnValue(ci.X(((PlayerInfo)this).getProfile().id(), (PlayerSkin)var1.getReturnValue()));
   }
}
