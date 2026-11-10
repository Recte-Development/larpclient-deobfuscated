package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.CrystalOptimizerModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class q454ea29530f91141 {
   @Inject(method = "attack", at = @At("TAIL"))
   private void j(Player var1, Entity var2, CallbackInfo var3) {
      CrystalOptimizerModule.n(var1, var2);
   }
}
