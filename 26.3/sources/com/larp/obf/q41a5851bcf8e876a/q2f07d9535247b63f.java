package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient.j2;
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
public abstract class q2f07d9535247b63f {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void y(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.N();
         if (var6 != null && var3 == 1 && var6.Z(j2.P(var4))) {
            var5.cancel();
         } else {
            if (var6 != null && var3 == 1) {
               var6.a(j2.P(var4));
            }

            if (WorldMenuModule.p(var3, var4)) {
               var5.cancel();
            }
         }
      }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void o(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (var1 == Minecraft.getInstance().getWindow().handle() && WorldMenuModule.z(var3)) {
         var4.cancel();
      }
   }
}
