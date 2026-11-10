package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.BacktrackModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class qe21b54089461a7ff {
   @Inject(method = "attack", at = @At("TAIL"))
   private void q(Player var1, Entity var2, CallbackInfo var3) {
      if (var1 == Minecraft.getInstance().player) {
         BacktrackModule.c(var2);
      }
   }
}
