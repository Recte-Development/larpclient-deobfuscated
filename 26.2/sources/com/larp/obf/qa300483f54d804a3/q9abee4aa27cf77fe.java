package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ho;
import com.larpclient.kv;
import com.larpclient.mh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class q9abee4aa27cf77fe {
   @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
   private void A(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && mh.j()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         kv.d(var1, var3.getX(), var3.getY(), var3.getWidth(), var3.getHeight(), ho.dy);
         var2.cancel();
      }
   }

   @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
   private void L(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (Minecraft.getInstance().level == null && mh.j()) {
         AbstractSelectionList var3 = (AbstractSelectionList)this;
         int var4 = ho.c;
         kv.d(var1, var3.getX(), var3.getY() - 1, var3.getWidth(), 1, var4);
         kv.d(var1, var3.getX(), var3.getBottom(), var3.getWidth(), 1, var4);
         var2.cancel();
      }
   }
}
