package com.larpclient.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenMixin_3 {
   @Accessor("leftPos")
   int larpLeftPos();

   @Accessor("topPos")
   int larpTopPos();

   @Accessor("imageWidth")
   int larpImageWidth();

   @Accessor("imageHeight")
   int larpImageHeight();
}
