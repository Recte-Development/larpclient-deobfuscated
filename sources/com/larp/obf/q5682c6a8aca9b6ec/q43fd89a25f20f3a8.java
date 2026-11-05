package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.f8;
import com.larpclient.module.ClickPulseModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.HitSoundsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q43fd89a25f20f3a8 {
   @Inject(method = "attack", at = @At("HEAD"))
   private void G(Player var1, Entity var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.c();
      if (var4 != null && var1 == Minecraft.getInstance().player) {
         var4.I(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.q();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.X(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.J();
      if (var6 != null && var1 == Minecraft.getInstance().player) {
         var6.Y(var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"))
   private void f(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         f8.o(var4);
      }
   }
}
