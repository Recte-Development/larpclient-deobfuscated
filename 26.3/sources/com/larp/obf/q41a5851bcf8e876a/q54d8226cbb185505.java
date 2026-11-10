package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.ShieldBreakerModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MultiPlayerGameMode.class, priority = 900)
public abstract class q54d8226cbb185505 {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
      require = 1,
      allow = 1
   )
   private void V(Player var1, Entity var2, CallbackInfo var3) {
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var4) {
         ShieldBreakerModule.Y(var4, var2);
      }
   }
}
