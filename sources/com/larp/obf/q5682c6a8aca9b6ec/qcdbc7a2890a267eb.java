package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.r7;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class qcdbc7a2890a267eb {
   @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
   private void q(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.y();
         if (var6 != null && var4 == 1) {
            var6.b(r7.r(var3));
         }

         if (WorldMenuModule.u(var3, var4)) {
            var5.cancel();
         }
      }
   }
}
