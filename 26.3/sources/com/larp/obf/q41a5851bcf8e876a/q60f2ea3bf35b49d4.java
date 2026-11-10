package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.CrystalOptimizerModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class q60f2ea3bf35b49d4 {
   @Inject(method = "attack", at = @At("TAIL"))
   private void A(Player var1, Entity var2, CallbackInfo var3) {
      CrystalOptimizerModule.E(var1, var2);
   }
}
