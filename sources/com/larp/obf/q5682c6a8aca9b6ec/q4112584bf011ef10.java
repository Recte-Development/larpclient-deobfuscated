package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.r7;
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
public abstract class q4112584bf011ef10 {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void z(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.y();
         if (var6 != null && var3 == 1) {
            var6.N(r7.g(var4));
         }

         if (WorldMenuModule.S(var3, var4)) {
            var5.cancel();
         }
      }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void t(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (var1 == Minecraft.getInstance().getWindow().handle() && WorldMenuModule.q(var3)) {
         var4.cancel();
      }
   }
}
