package com.larpclient.mixin;

import com.larpclient.gui.misc.MenuPopupHost;
import com.larpclient.util.render.PressAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin {
   @ModifyArgs(
      method = "extractRenderState",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;containsPointInScissor(II)Z")
   )
   private void t(Args var1) {
      Screen var2 = Minecraft.getInstance().gui.screen();
      if (var2 instanceof MenuPopupHost var3 && var3.larpDrawingMenuPopup()) {
         PressAnimation.Transform var4 = MenuPopupHost.getPopupState(var2);
         var1.set(0, (int)Math.round(var4.screenX(((Integer)var1.get(0)).intValue(), var2.width / 2.0)));
         var1.set(1, (int)Math.round(var4.screenY(((Integer)var1.get(1)).intValue(), var2.height / 2.0)));
      }
   }
}
