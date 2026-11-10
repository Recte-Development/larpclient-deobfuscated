package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.s2;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.class_11910;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_312.class)
public abstract class q2ea1755269a9ae29 {
   @Inject(method = "method_1601", at = @At("HEAD"), cancellable = true)
   private void g(long var1, class_11910 var3, int var4, CallbackInfo var5) {
      if (var1 == class_310.method_1551().method_22683().method_4490()) {
         LarpClient var6 = LarpClient.b();
         if (var6 != null && var4 == 1) {
            var6.g(s2.E(var3));
         }

         if (WorldMenuModule.W(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
