package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.AimAssistModule;
import com.larpclient.module.WTapModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class qa66b2b15f230dda0 {
   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
   private void Y(Entity var1, CallbackInfo var2) {
      WTapModule.i((Player)this, var1);
   }

   @Inject(
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onAttack()V"),
      require = 1,
      allow = 1
   )
   private void f(Entity var1, CallbackInfo var2) {
      WTapModule.F((Player)this, var1);
      AimAssistModule.J((Player)this, var1);
   }

   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
   private void R(Entity var1, CallbackInfo var2) {
      WTapModule.v((Player)this, var1);
   }
}
