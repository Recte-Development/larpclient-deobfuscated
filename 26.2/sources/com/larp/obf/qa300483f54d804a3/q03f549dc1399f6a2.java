package com.larp.obf.qa300483f54d804a3;

import com.larpclient.si;
import com.larpclient.module.MaceModule;
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
public abstract class q03f549dc1399f6a2 {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
      require = 1,
      allow = 1
   )
   private void Q(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.W();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive() && !si.o(var5)) {
         var4.Z(var5, var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"), require = 1, allow = 1)
   private void J(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.W();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive()) {
         var4.L(var5, var2);
      }
   }
}
