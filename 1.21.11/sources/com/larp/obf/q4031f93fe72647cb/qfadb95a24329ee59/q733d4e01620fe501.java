package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.module.at;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = class_465.class, priority = 500)
public abstract class q733d4e01620fe501 {
   @Shadow
   protected class_1735 field_2787;
   @Shadow
   @Final
   protected class_1703 field_2797;

   @Inject(method = "method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V", at = @At("HEAD"), cancellable = true)
   private void S(class_1735 var1, int var2, int var3, class_1713 var4, CallbackInfo var5) {
      if (at.o(var1, this.field_2797)) {
         var5.cancel();
      }
   }

   @Inject(method = "method_2385", at = @At("TAIL"))
   private void U(class_332 var1, class_1735 var2, int var3, int var4, CallbackInfo var5) {
      int var6 = at.e(var2, var2 == this.field_2787, this.field_2797);
      if (var6 != 0) {
         var1.method_25294(var2.field_7873, var2.field_7872, var2.field_7873 + 16, var2.field_7872 + 16, var6);
      }
   }
}
