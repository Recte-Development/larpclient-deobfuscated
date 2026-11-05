package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.CrystalOptimizerModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class q27023645374021c9 {
   @Inject(method = "attack", at = @At("TAIL"))
   private void R(Player var1, Entity var2, CallbackInfo var3) {
      CrystalOptimizerModule.A(var1, var2);
   }
}
