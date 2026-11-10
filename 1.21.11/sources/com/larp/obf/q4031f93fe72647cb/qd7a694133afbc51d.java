package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.BoatFlyModule;
import net.minecraft.class_10255;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_10255.class)
public abstract class qd7a694133afbc51d {
   @Shadow
   private float field_54456;

   @Inject(method = "method_64482", at = @At("HEAD"), cancellable = true)
   private void O(CallbackInfo var1) {
      if (BoatFlyModule.r((class_10255)this)) {
         this.field_54456 = 0.0F;
         var1.cancel();
      }
   }
}
