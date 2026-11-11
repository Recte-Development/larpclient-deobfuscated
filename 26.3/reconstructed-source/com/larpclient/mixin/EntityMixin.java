package com.larpclient.mixin;

import com.larpclient.module.combat.HitBoxesModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
   @Inject(method = "getPickRadius", at = @At("RETURN"), cancellable = true)
   private void W(CallbackInfoReturnable<Float> var1) {
      float var2 = HitBoxesModule.getExpansion((Entity)this);
      if (var2 > 0.0F) {
         var1.setReturnValue(var1.getReturnValueF() + var2);
      }
   }
}
