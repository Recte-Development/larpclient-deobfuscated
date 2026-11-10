package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.wp;
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
public class qd6fa4233ba547d98 {
   @Inject(method = "attack", at = @At("HEAD"))
   private void P(Player var1, Entity var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.D();
      if (var4 != null && var1 == Minecraft.getInstance().player) {
         var4.G(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.k();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.P(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.N();
      if (var6 != null && var1 == Minecraft.getInstance().player) {
         var6.R(var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"))
   private void g(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         wp.W(var4);
      }
   }
}
