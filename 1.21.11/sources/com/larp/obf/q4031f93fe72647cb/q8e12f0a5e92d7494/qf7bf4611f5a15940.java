package com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494;

import com.larpclient.sh;
import net.minecraft.class_11909;
import net.minecraft.class_1735;
import net.minecraft.class_465;
import net.minecraft.class_490;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_465.class)
public abstract class qf7bf4611f5a15940 {
   @Shadow
   protected class_1735 field_2787;

   @Inject(method = "method_25402", at = @At("HEAD"), cancellable = true)
   private void W(class_11909 var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (sh.p() && this instanceof class_490) {
         var3.setReturnValue(true);
         if (this.field_2787 != null && this.field_2787.method_7681()) {
            sh.R(this.field_2787.method_7677());
         }
      }
   }
}
