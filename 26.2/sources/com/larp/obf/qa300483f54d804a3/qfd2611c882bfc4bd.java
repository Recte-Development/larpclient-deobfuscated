package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.h;
import com.larpclient.p_;
import com.larpclient.ta;
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
public abstract class qfd2611c882bfc4bd {
   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
   private void E(CallbackInfo var1) {
      LarpClient var2 = LarpClient.R();
      ta.o();
      if (var2 != null && var2.z() != null) {
         var2.z().u();
         p_.P();
         RodSwapModule var3 = var2.z().p(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            G(var3, var3::T);
         }

         MaceModule var4 = MaceModule.W();
         if (var4 != null && var4.isRuntimeActive()) {
            G(var4, MaceModule::M);
         }
      }
   }

   @Unique
   private static void G(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         h.m("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
