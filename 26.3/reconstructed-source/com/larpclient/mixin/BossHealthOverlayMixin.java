package com.larpclient.mixin;

import com.larpclient.module.render.RemovalsModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void N(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RemovalsModule.isRemovalEnabled("bossBar")) {
         var2.cancel();
      }
   }
}
