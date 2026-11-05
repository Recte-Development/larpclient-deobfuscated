package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class q62eb85efb689f1ea {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void A(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)this;
      Minecraft var7 = Minecraft.getInstance();
      if (tq.l() != tq.y.A && var6 == var7.player) {
         tq.Q(var1, var3);
         var5.cancel();
      }
   }
}
