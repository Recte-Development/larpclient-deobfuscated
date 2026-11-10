package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gv;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_10042;
import net.minecraft.class_1921;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_922.class)
public class q7d9f32ef42a6261d {
   @Inject(method = "method_24302", at = @At("HEAD"), cancellable = true)
   private void l(class_10042 var1, boolean var2, boolean var3, boolean var4, CallbackInfoReturnable<class_1921> var5) {
      SelfVisualsModule var6 = SelfVisualsModule.o();
      if (var6 != null && var6.isEnabled() && !gv.a() && SelfVisualsModule.Q(var1)) {
         var5.setReturnValue(null);
      }
   }
}
