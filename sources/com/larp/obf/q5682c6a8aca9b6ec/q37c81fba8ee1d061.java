package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class q37c81fba8ee1d061 {
   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void k(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RemovalsModule.s("bossBar")) {
         var2.cancel();
      }
   }
}
