package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class q1e36164e37e28394 {
   @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
   private void o(Player var1, CallbackInfoReturnable<Boolean> var2) {
      if (rr.o() && FreecamModule.j() && this == Minecraft.getInstance().player && var1 == this) {
         var2.setReturnValue(false);
      }
   }
}
