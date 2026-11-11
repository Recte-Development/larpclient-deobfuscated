package com.larpclient.mixin;

import com.larpclient.module.movement.BoatFlyModule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin {
   @Shadow
   private float deltaRotation;

   @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfo var1) {
      if (BoatFlyModule.controlBoat((AbstractBoat)this)) {
         this.deltaRotation = 0.0F;
         var1.cancel();
      }
   }
}
