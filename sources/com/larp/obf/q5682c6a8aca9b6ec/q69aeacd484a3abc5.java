package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.CrystalOptimizerModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class q69aeacd484a3abc5 {
   @Inject(method = "attack", at = @At("TAIL"))
   private void G(Player var1, Entity var2, CallbackInfo var3) {
      CrystalOptimizerModule.V(var1, var2);
   }
}
