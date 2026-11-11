package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.util.misc.KeyCodes;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
   @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
   private void y(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      if (var1 == Minecraft.getInstance().getWindow().handle()) {
         LarpClient var6 = LarpClient.getInstance();
         if (var6 != null && var3 == 1 && var6.handleLegitModeKey(KeyCodes.keyOf(var4))) {
            var5.cancel();
         } else {
            if (var6 != null && var3 == 1) {
               var6.onMouseBind(KeyCodes.keyOf(var4));
            }

            if (WorldMenuModule.isOverHandle(var3, var4)) {
               var5.cancel();
            }
         }
      }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void o(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (var1 == Minecraft.getInstance().getWindow().handle() && WorldMenuModule.onCharTyped(var3)) {
         var4.cancel();
      }
   }
}
