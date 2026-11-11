package com.larpclient.mixin;

import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.module.combat.WTapModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
   private void U(Entity var1, CallbackInfo var2) {
      WTapModule.onAttack((Player)this, var1);
   }

   @Inject(
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onAttack()V"),
      require = 1,
      allow = 1
   )
   private void u(Entity var1, CallbackInfo var2) {
      WTapModule.onAttackConfirmed((Player)this, var1);
      AimAssistModule.onAttack((Player)this, var1);
   }

   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
   private void K(Entity var1, CallbackInfo var2) {
      WTapModule.onAttackExecuted((Player)this, var1);
   }
}
