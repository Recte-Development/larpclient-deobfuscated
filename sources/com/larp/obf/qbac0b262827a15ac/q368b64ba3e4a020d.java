package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class q368b64ba3e4a020d {
   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void l(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RemovalsModule.r("bossBar")) {
         var2.cancel();
      }
   }
}
