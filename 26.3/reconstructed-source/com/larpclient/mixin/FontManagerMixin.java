package com.larpclient.mixin;

import com.larpclient.render.overlay.FontRenderer;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontManager.class)
public class FontManagerMixin {
   @Inject(method = {"apply", "updateOptions"}, at = @At("TAIL"))
   private void f(CallbackInfo var1) {
      FontRenderer.invalidateCache();
   }
}
