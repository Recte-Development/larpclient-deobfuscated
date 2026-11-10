package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ij;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class qf18e2f3708a83bbe {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void N(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)this;
      Minecraft var7 = Minecraft.getInstance();
      if (ij.L() != ij.r.X && var6 == var7.player) {
         ij.c(var1, var3);
         var5.cancel();
      }
   }
}
