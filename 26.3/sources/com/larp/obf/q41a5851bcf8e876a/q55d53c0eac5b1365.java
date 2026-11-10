package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class q55d53c0eac5b1365 {
   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void N(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RemovalsModule.G("bossBar")) {
         var2.cancel();
      }
   }
}
