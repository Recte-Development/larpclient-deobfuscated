package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.c7;
import com.larpclient.f;
import com.larpclient.uj;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class qc7ee3aefbb2fde02 {
   @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
   private void F(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && uj.l()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         c7.s(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), f.m);
         var2.cancel();
      }
   }

   @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
   private void G(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && uj.l()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         int var4 = f.I;
         c7.s(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         c7.s(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
