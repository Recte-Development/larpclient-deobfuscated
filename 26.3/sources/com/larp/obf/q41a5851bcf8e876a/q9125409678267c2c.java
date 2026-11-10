package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient.j2;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q9125409678267c2c {
   @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
   private void Z(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.N();
         if (var6 != null && var4 == 1) {
            var6.m(j2.B(var3));
         }

         if (WorldMenuModule.l(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
