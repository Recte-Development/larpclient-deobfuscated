package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q91261c560e45f908 {
   @Accessor("leftPos")
   int larpLeftPos();

   @Accessor("topPos")
   int larpTopPos();

   @Accessor("imageWidth")
   int larpImageWidth();

   @Accessor("imageHeight")
   int larpImageHeight();
}
