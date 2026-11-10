package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.BoatFlyModule;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public abstract class q72c3800f06f31c0c {
   @Shadow
   private float deltaRotation;

   @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfo var1) {
      if (BoatFlyModule.z((AbstractBoat)this)) {
         this.deltaRotation = 0.0F;
         var1.cancel();
      }
   }
}
