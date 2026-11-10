package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class q7e2156537944c4d9 {
   @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
   private void t(Player var1, CallbackInfoReturnable<Boolean> var2) {
      if (p_.V() && FreecamModule.Z() && this == Minecraft.getInstance().player && var1 == this) {
         var2.setReturnValue(false);
      }
   }
}
