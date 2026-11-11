package com.larpclient.mixin;

import com.larpclient.social.cosmetic.CosmeticOverrides;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void Q(CallbackInfoReturnable<PlayerSkin> var1) {
      var1.setReturnValue(CosmeticOverrides.applySkin(((PlayerInfo)this).getProfile().id(), (PlayerSkin)var1.getReturnValue()));
   }
}
