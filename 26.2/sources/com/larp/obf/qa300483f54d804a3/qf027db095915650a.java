package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FakePlayerModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class qf027db095915650a {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"),
      require = 1,
      allow = 1
   )
   private void i(Player var1, Entity var2, CallbackInfo var3) {
      if (var1 == Minecraft.getInstance().player) {
         FakePlayerModule.n(var2);
      }
   }
}
