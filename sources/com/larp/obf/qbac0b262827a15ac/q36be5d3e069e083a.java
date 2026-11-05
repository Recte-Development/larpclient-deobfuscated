package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient.mo;
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
public abstract class q36be5d3e069e083a {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void E(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.F();
         if (var6 != null && var3 == 1) {
            var6.S(mo.O(var4));
         }

         if (WorldMenuModule.O(var3, var4)) {
            var5.cancel();
         }
      }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void Z(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (var1 == Minecraft.getInstance().getWindow().handle() && WorldMenuModule.V(var3)) {
         var4.cancel();
      }
   }
}
