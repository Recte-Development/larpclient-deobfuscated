package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.larpclient.u2;
import net.minecraft.class_10039;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_916;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_916.class)
public abstract class q7e83a946d21fee0c {
   @Redirect(
      method = "method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1542;method_6983()Lnet/minecraft/class_1799;")
   )
   private class_1799 x(class_1542 var1) {
      cz var2 = cz.T();
      u2.X(var1);
      if (!var2.J()) {
         return var1.method_6983();
      } else {
         return var2.Q(var1) ? class_1799.field_8037 : var2.i(var1.method_6983());
      }
   }

   @Inject(method = "method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V", at = @At("TAIL"))
   private void A(class_1542 var1, class_10039 var2, float var3, CallbackInfo var4) {
      cz var5 = cz.T();
      if (var5.J() && var5.D(var1)) {
         var2.field_53333 = true;
         var2.field_55310.method_65605();
         var2.field_55311 = 0;
      }
   }
}
