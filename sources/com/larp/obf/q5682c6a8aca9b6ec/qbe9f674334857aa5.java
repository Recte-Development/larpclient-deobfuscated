package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.AimAssistModule;
import com.larpclient.module.WTapModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class qbe9f674334857aa5 {
   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
   private void i(Entity var1, CallbackInfo var2) {
      WTapModule.q((Player)this, var1);
   }

   @Inject(
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onAttack()V"),
      require = 1,
      allow = 1
   )
   private void u(Entity var1, CallbackInfo var2) {
      WTapModule.u((Player)this, var1);
      AimAssistModule.F((Player)this, var1);
   }

   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
   private void q(Entity var1, CallbackInfo var2) {
      WTapModule.G((Player)this, var1);
   }
}
