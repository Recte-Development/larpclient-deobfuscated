package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.HitBoxesModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class q4ab07bba83fc9bc6 {
   @Inject(method = "getPickRadius", at = @At("RETURN"), cancellable = true)
   private void h(CallbackInfoReturnable<Float> var1) {
      float var2 = HitBoxesModule.y((Entity)this);
      if (var2 > 0.0F) {
         var1.setReturnValue(var1.getReturnValueF() + var2);
      }
   }
}
