package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.BacktrackModule;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/class_11980$class_11981")
public abstract class qc86f69e0e3a21b5a {
   @Shadow
   @Final
   private class_2547 comp_4821;
   @Shadow
   @Final
   private class_2596<?> comp_4822;

   @Shadow
   public abstract void method_74450();

   @Inject(method = "method_74450", at = @At("HEAD"), cancellable = true)
   private void w(CallbackInfo var1) {
      if (!BacktrackModule.m()) {
         if (BacktrackModule.r(this.comp_4822, this.comp_4821, this::method_74450)) {
            var1.cancel();
         }
      }
   }
}
