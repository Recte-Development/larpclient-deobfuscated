package com.larpclient.mixin;

import com.larpclient.gui.screen.LarpTitleScreen;
import com.larpclient.gui.theme.Theme;
import com.larpclient.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class AbstractSelectionListMixin {
   @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
   private void z(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && LarpTitleScreen.isMenuEnabled()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         DrawUtil.fillRect(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), Theme.inputBackground);
         var2.cancel();
      }
   }

   @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
   private void M(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && LarpTitleScreen.isMenuEnabled()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         int var4 = Theme.controlBackground;
         DrawUtil.fillRect(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         DrawUtil.fillRect(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
