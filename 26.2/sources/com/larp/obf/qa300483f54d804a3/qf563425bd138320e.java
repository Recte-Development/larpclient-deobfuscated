package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.k2;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class qf563425bd138320e {
   @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
   private void f(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.R();
         if (var6 != null && var4 == 1) {
            var6.u(k2.F(var3));
         }

         if (WorldMenuModule.J(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
