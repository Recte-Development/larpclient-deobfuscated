package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.s2;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_309.class)
public abstract class qc76886e8123d24af {
   @Inject(method = "method_1466", at = @At("HEAD"), cancellable = true)
   private void R(long var1, int var3, class_11908 var4, CallbackInfo var5) {
      if (var1 == class_310.method_1551().method_22683().method_4490()) {
         LarpClient var6 = LarpClient.b();
         if (var6 != null && var3 == 1 && var6.z(s2.i(var4))) {
            var5.cancel();
         } else {
            if (var6 != null && var3 == 1) {
               var6.r(s2.i(var4));
            }

            if (WorldMenuModule.G(var3, var4)) {
               var5.cancel();
            }
         }
      }
   }

   @Inject(method = "method_1457", at = @At("HEAD"), cancellable = true)
   private void t(long var1, class_11905 var3, CallbackInfo var4) {
      if (var1 == class_310.method_1551().method_22683().method_4490() && WorldMenuModule.a(var3)) {
         var4.cancel();
      }
   }
}
