package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.MaceExploitModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class q0a785f03b6cf9360 {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"),
      require = 1,
      allow = 1
   )
   private void t(Player var1, Entity var2, CallbackInfo var3) {
      MaceExploitModule var4 = MaceExploitModule.T();
      if (var1 == Minecraft.getInstance().player && var4 != null && var4.isRuntimeActive()) {
         var4.r(var2);
      }
   }
}
