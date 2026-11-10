package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.fr;
import com.larpclient.sd;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractWidget.class)
public abstract class q4a73ab6af91c4cd6 {
   @ModifyArgs(
      method = "extractRenderState",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;containsPointInScissor(II)Z")
   )
   private void t(Args var1) {
      Screen var2 = Minecraft.getInstance().gui.screen();
      if (var2 instanceof sd var3 && var3.larpDrawingMenuPopup()) {
         fr.z var4 = sd.R(var2);
         var1.set(0, (int)Math.round(var4.screenX(((Integer)var1.get(0)).intValue(), var2.width / 2.0)));
         var1.set(1, (int)Math.round(var4.screenY(((Integer)var1.get(1)).intValue(), var2.height / 2.0)));
      }
   }
}
