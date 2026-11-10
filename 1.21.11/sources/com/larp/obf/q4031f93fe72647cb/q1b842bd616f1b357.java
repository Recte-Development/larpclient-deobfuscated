package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.Feature3DUIModule;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_757.class)
public abstract class q1b842bd616f1b357 {
   @Inject(method = "method_76295", at = @At("HEAD"))
   private void x(CallbackInfo var1) {
      Feature3DUIModule.h();
   }
}
