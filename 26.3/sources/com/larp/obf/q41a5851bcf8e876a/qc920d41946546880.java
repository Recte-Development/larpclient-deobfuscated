package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ij;
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
public abstract class qc920d41946546880 {
   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
   private void F(CallbackInfo var1) {
      LarpClient var2 = LarpClient.N();
      _p.e();
      if (var2 != null && var2.j() != null) {
         var2.j().E();
         ij.S();
         RodSwapModule var3 = var2.j().y(RodSwapModule.class);
         if (var3 != null && var3.isRuntimeActive()) {
            U(var3, var3::N);
         }

         MaceModule var4 = MaceModule.U();
         if (var4 != null && var4.isRuntimeActive()) {
            U(var4, MaceModule::h);
         }
      }
   }

   @Unique
   private static void U(Module var0, Runnable var1) {
      try {
         var1.run();
      } catch (RuntimeException var3) {
         _q.o("Combat", "Pre-input tick failed; releasing " + var0.getName(), var3);
         var0.disable();
      }
   }
}
