package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient.mo;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class qe5bead3451e6d0bf {
   @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
   private void x(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.F();
         if (var6 != null && var4 == 1) {
            var6.B(mo.i(var3));
         }

         if (WorldMenuModule.N(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
