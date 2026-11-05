package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class q10974f07b267887b {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void n(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)this;
      Minecraft var7 = Minecraft.getInstance();
      if (rr.u() != rr.f.i && var6 == var7.player) {
         rr.H(var1, var3);
         var5.cancel();
      }
   }
}
