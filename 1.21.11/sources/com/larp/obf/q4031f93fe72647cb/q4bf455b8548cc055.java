package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.cr;
import com.larpclient.q8;
import com.larpclient.wp;
import com.larpclient.module.MaceModule;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwapModule;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_310.class)
public abstract class q4bf455b8548cc055 {
   @Inject(method = "method_1574", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_329;method_39191(Z)V"))
   private void a(CallbackInfo var1) {
      LarpClient var2 = LarpClient.b();
      cr.E();
      if (var2 != null && var2.G() != null) {
         var2.G().Z();
         wp.N();
         RodSwapModule var3 = var2.G().b(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            o(var3, var3::L);
         }

         MaceModule var4 = MaceModule.c();
         if (var4 != null && var4.isRuntimeActive()) {
            o(var4, MaceModule::E);
         }
      }
   }

   @Unique
   private static void o(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         q8.u("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
