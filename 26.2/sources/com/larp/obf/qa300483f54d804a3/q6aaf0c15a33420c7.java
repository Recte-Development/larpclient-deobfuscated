package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.k2;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class q6aaf0c15a33420c7 {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void a(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.R();
         if (var6 != null && var3 == 1 && var6.t(k2.a(var4))) {
            var5.cancel();
         } else {
            if (var6 != null && var3 == 1) {
               var6.q(k2.a(var4));
            }

            if (WorldMenuModule.s(var3, var4)) {
               var5.cancel();
            }
         }
      }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void j(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (var1 == Minecraft.getInstance().getWindow().handle() && WorldMenuModule.o(var3)) {
         var4.cancel();
      }
   }
}
