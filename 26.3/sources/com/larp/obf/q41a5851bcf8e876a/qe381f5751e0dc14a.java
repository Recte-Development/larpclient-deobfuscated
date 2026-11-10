package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.c;
import com.larpclient.de;
import com.larpclient.h5;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class qe381f5751e0dc14a {
   @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
   private void z(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && de.B()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         h5.N(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), c.I);
         var2.cancel();
      }
   }

   @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
   private void M(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && de.B()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         int var4 = c.n;
         h5.N(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         h5.N(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
