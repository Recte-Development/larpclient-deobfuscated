package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RodSwapModule;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_312.class)
public abstract class qd268e163fc603069 {
   @Inject(method = "method_1598(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_61496(I)V"))
   private void m(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwapModule.O();
   }
}
