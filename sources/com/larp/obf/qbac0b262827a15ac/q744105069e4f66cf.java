package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient._m;
import com.larpclient.mt;
import com.larpclient.tq;
import com.larpclient.module.MaceModule;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwapModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class q744105069e4f66cf {
   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
   private void z(CallbackInfo var1) {
      LarpClient var2 = LarpClient.F();
      _m.o();
      if (var2 != null && var2.x() != null) {
         var2.x().P();
         tq.x();
         RodSwapModule var3 = var2.x().V(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            C(var3, var3::O);
         }

         MaceModule var4 = MaceModule.c();
         if (var4 != null && var4.isRuntimeActive()) {
            C(var4, MaceModule::z);
         }
      }
   }

   @Unique
   private static void C(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         mt.P("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
