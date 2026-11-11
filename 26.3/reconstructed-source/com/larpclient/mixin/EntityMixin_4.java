package com.larpclient.mixin;

import com.larpclient.module.render.RemovalsModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin_4 {
   @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
   private void E(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)this).level().isClientSide() && RemovalsModule.isRemovalEnabled("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
