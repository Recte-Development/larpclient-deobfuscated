package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q7ab337832e529236 {
   @Accessor("leftPos")
   int larpLeftPos();

   @Accessor("topPos")
   int larpTopPos();

   @Accessor("imageWidth")
   int larpImageWidth();

   @Accessor("imageHeight")
   int larpImageHeight();
}
