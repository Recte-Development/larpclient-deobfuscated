package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.TotemHitModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class qd7da8a50b4706038 {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
      require = 1,
      allow = 1
   )
   private void E(Player var1, Entity var2, CallbackInfo var3) {
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var4) {
         TotemHitModule.u(var4, var2);
      }
   }
}
