package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_332;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_337.class)
public class qf0a472bc02cc40bc {
   @Inject(method = "method_1796", at = @At("HEAD"), cancellable = true)
   private void d(class_332 var1, CallbackInfo var2) {
      if (RemovalsModule.o("bossBar")) {
         var2.cancel();
      }
   }
}
