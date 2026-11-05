package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.m8;
import com.larpclient.wv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractWidget.class)
public abstract class q9bd72f69699cbc41 {
   @ModifyArgs(
      method = "extractRenderState",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;containsPointInScissor(II)Z")
   )
   private void Z(Args var1) {
      Screen var2 = Minecraft.getInstance().gui.screen();
      if (var2 instanceof wv var3 && var3.larpDrawingMenuPopup()) {
         m8.w var4 = wv.K(var2);
         var1.set(0, (int)Math.round(var4.screenX(((Integer)var1.get(0)).intValue(), var2.width / 2.0)));
         var1.set(1, (int)Math.round(var4.screenY(((Integer)var1.get(1)).intValue(), var2.height / 2.0)));
      }
   }
}
