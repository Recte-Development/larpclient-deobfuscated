package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.j5;
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
public abstract class qd88916eebaa154da {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
      require = 1,
      allow = 1
   )
   private void s(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.Q();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive() && !j5.n(var5)) {
         var4.s(var5, var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"), require = 1, allow = 1)
   private void C(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.Q();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive()) {
         var4.Q(var5, var2);
      }
   }
}
