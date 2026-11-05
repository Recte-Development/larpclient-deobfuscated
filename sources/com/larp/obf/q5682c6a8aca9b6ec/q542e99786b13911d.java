package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.fr;
import com.larpclient.rf;
import com.larpclient.rv;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class q542e99786b13911d {
   @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
   private void h(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && rf.H()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         fr.k(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), rv.q);
         var2.cancel();
      }
   }

   @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
   private void w(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && rf.H()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         int var4 = rv.f;
         fr.k(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         fr.k(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
