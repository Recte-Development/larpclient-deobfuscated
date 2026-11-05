package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.r4;
import com.larpclient.rr;
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
public abstract class qbb39d7bc2d468e6a {
   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
   private void O(CallbackInfo var1) {
      LarpClient var2 = LarpClient.y();
      d8.f();
      if (var2 != null && var2.e() != null) {
         var2.e().Z();
         rr.C();
         RodSwapModule var3 = var2.e().y(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            U(var3, var3::D);
         }

         MaceModule var4 = MaceModule.Q();
         if (var4 != null && var4.isRuntimeActive()) {
            U(var4, MaceModule::Q);
         }
      }
   }

   @Unique
   private static void U(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         r4.r("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
