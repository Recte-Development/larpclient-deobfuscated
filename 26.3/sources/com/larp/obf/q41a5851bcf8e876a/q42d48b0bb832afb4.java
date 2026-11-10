package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.AimAssistModule;
import com.larpclient.module.WTapModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class q42d48b0bb832afb4 {
   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
   private void U(Entity var1, CallbackInfo var2) {
      WTapModule.E((Player)this, var1);
   }

   @Inject(
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onAttack()V"),
      require = 1,
      allow = 1
   )
   private void u(Entity var1, CallbackInfo var2) {
      WTapModule.y((Player)this, var1);
      AimAssistModule.N((Player)this, var1);
   }

   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
   private void K(Entity var1, CallbackInfo var2) {
      WTapModule.r((Player)this, var1);
   }
}
