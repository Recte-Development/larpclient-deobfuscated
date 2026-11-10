package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.BoatFlyModule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class q0be1f6889fc1cda0 {
   @Shadow
   private float deltaRotation;

   @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
   private void N(CallbackInfo var1) {
      if (BoatFlyModule.Q((AbstractBoat)this)) {
         this.deltaRotation = 0.0F;
         var1.cancel();
      }
   }
}
