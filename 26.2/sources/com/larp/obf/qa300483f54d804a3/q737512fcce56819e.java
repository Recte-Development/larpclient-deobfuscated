package com.larp.obf.qa300483f54d804a3;

import com.larpclient.kz;
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
public class q737512fcce56819e {
   @Inject(method = "attack", at = @At("HEAD"))
   private void C(Player var1, Entity var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.t();
      if (var4 != null && var1 == Minecraft.getInstance().player) {
         var4.k(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.v();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.V(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.J();
      if (var6 != null && var1 == Minecraft.getInstance().player) {
         var6.w(var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"))
   private void n(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         kz.R(var4);
      }
   }
}
