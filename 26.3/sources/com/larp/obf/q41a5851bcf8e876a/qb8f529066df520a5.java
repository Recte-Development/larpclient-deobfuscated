package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ij;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class qb8f529066df520a5 {
   @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
   private void e(Player var1, CallbackInfoReturnable<Boolean> var2) {
      if (ij.y() && FreecamModule.T() && this == Minecraft.getInstance().player && var1 == this) {
         var2.setReturnValue(false);
      }
   }
}
