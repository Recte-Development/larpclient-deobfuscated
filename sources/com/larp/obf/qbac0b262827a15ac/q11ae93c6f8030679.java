package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.BoatFlyModule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class q11ae93c6f8030679 {
   @Shadow
   private float deltaRotation;

   @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
   private void X(CallbackInfo var1) {
      if (BoatFlyModule.P((AbstractBoat)this)) {
         this.deltaRotation = 0.0F;
         var1.cancel();
      }
   }
}
