package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HitBoxesModule;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1297.class)
public class qe8456ab0964de789 {
   @Inject(method = "method_5871", at = @At("RETURN"), cancellable = true)
   private void m(CallbackInfoReturnable<Float> var1) {
      float var2 = HitBoxesModule.U((class_1297)this);
      if (var2 > 0.0F) {
         var1.setReturnValue(var1.getReturnValueF() + var2);
      }
   }
}
