package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.AimAssistModule;
import com.larpclient.module.WTapModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class q580825d2802bd06f {
   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
   private void V(Entity var1, CallbackInfo var2) {
      WTapModule.f((Player)this, var1);
   }

   @Inject(
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onAttack()V"),
      require = 1,
      allow = 1
   )
   private void r(Entity var1, CallbackInfo var2) {
      WTapModule.U((Player)this, var1);
      AimAssistModule.E((Player)this, var1);
   }

   @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
   private void T(Entity var1, CallbackInfo var2) {
      WTapModule.o((Player)this, var1);
   }
}
