package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qc;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class q4f71d6961d27ee56 {
   @WrapOperation(
      method = "extractRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"
      )
   )
   private void X(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5, Operation<Void> var6) {
      if (qc.b(var1)) {
         var6.call(new Object[]{var1, qc.R(), var3, var4, var5});
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
         if (qc.i()) {
            var6.call(new Object[]{var1, qc.R(), var3, var4, var5});
         }
      }
   }
}
