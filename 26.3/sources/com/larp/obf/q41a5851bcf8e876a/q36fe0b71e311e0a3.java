package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.p3;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class q36fe0b71e311e0a3 {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void Q(CallbackInfoReturnable<PlayerSkin> var1) {
      var1.setReturnValue(p3.n(((PlayerInfo)this).getProfile().id(), (PlayerSkin)var1.getReturnValue()));
   }
}
