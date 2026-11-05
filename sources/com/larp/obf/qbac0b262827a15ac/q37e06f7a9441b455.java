package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class q37e06f7a9441b455 {
   @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
   private void N(Player var1, CallbackInfoReturnable<Boolean> var2) {
      if (tq.f() && FreecamModule.p() && this == Minecraft.getInstance().player && var1 == this) {
         var2.setReturnValue(false);
      }
   }
}
