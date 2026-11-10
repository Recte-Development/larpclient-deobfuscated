package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.he;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_640.class)
public class qd4800577dd39551f {
   @Inject(method = "method_52810", at = @At("RETURN"), cancellable = true)
   private void Z(CallbackInfoReturnable<class_8685> var1) {
      var1.setReturnValue(he.a(((class_640)this).method_2966().id(), (class_8685)var1.getReturnValue()));
   }
}
