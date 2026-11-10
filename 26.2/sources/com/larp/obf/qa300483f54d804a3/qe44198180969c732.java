package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class qe44198180969c732 {
   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void C(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (RemovalsModule.n("bossBar")) {
         var2.cancel();
      }
   }
}
