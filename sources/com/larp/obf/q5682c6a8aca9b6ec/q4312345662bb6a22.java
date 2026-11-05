package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.BoatFlyModule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class q4312345662bb6a22 {
   @Shadow
   private float deltaRotation;

   @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
   private void d(CallbackInfo var1) {
      if (BoatFlyModule.k((AbstractBoat)this)) {
         this.deltaRotation = 0.0F;
         var1.cancel();
      }
   }
}
