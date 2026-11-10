package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qg;
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
public abstract class q52fccd3b94de9195 {
   @Inject(
      method = "attack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"),
      require = 1,
      allow = 1
   )
   private void W(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.U();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive() && !qg.Y(var5)) {
         var4.M(var5, var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"), require = 1, allow = 1)
   private void I(Player var1, Entity var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.U();
      if (var1 == Minecraft.getInstance().player && var1 instanceof LocalPlayer var5 && var4 != null && var4.isRuntimeActive()) {
         var4.D(var5, var2);
      }
   }
}
