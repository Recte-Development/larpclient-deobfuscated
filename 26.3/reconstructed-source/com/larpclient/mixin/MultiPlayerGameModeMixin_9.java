package com.larpclient.mixin;

import com.larpclient.module.misc.HitSoundsModule;
import com.larpclient.module.render.ClickPulseModule;
import com.larpclient.module.render.DamageMarkersModule;
import com.larpclient.render.misc.VisualsRenderer;
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
public class MultiPlayerGameModeMixin_9 {
   @Inject(method = "attack", at = @At("HEAD"))
   private void P(Player var1, Entity var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.getInstance();
      if (var4 != null && var1 == Minecraft.getInstance().player) {
         var4.isFriend(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.getInstance();
      if (var5 != null && var1 == Minecraft.getInstance().player) {
         var5.createSoundInstance(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.getInstance();
      if (var6 != null && var1 == Minecraft.getInstance().player) {
         var6.onAttackEntity(var2);
      }
   }

   @Inject(method = "attack", at = @At("TAIL"))
   private void g(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof LivingEntity var4) {
         VisualsRenderer.onEntityHit(var4);
      }
   }
}
