package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.RemovalsModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class qb86f97d0131ecea2 {
   @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
   private void W(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)this).level().isClientSide() && RemovalsModule.n("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
