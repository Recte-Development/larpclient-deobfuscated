package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.f4;
import net.minecraft.class_1531;
import net.minecraft.class_1799;
import net.minecraft.class_877;
import net.minecraft.class_9998;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_877.class)
public class q9e882564542211e6 {
   @Inject(method = "method_62375(Lnet/minecraft/class_1531;Lnet/minecraft/class_9998;F)V", at = @At("TAIL"))
   private void i(class_1531 var1, class_9998 var2, float var3, CallbackInfo var4) {
      class_1799[] var5 = f4.Y.D(var1);
      if (var5 != null) {
         var2.field_55309 = var5[1];
         var2.field_53418 = var5[2];
         var2.field_53419 = var5[3];
         var2.field_53420 = var5[4];
      }
   }
}
