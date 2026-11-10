package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.RealisticWaterModule;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4603.class)
public abstract class qecd0beef6d7bdfd4 {
   @Inject(method = "method_23069", at = @At("HEAD"), cancellable = true)
   private static void K(CallbackInfo var0) {
      if (RealisticWaterModule.d()) {
         var0.cancel();
      }
   }
}
