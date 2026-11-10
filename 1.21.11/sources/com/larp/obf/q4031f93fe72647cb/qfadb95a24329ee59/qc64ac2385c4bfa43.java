package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.i;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class qc64ac2385c4bfa43 {
   @Inject(method = "method_7346", at = @At("HEAD"))
   private void i(CallbackInfo var1) {
      i.e(class_310.method_1551());
   }
}
