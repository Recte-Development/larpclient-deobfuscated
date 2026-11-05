package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.h;
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
public class q6579bdff0700c06e {
   @Inject(method = "attack", at = @At("HEAD"))
   private void O(Player var1, Entity var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.U();
      if (var4 != null && var1 == Minecraft.getInstance().player) {
         var4.G(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.o();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.G(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.J();
      if (var6 != null && var1 == Minecraft.getInstance().player) {
         var6.C(var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"))
   private void Z(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         h.D(var4);
      }
   }
}
