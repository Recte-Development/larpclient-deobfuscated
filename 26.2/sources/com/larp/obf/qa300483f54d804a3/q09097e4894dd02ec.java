package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p2;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class q09097e4894dd02ec {
   @WrapOperation(
      method = "extractRenderState",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"
      )
   )
   private void J(Screen var1, GuiGraphicsExtractor var2, int var3, int var4, float var5, Operation<Void> var6) {
      if (p2.R(var1)) {
         var6.call(new Object[]{var1, p2.n(), var3, var4, var5});
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
         if (p2.I()) {
            var6.call(new Object[]{var1, p2.n(), var3, var4, var5});
         }
      }
   }
}
