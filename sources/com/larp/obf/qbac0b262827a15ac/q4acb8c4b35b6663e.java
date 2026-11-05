package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q4acb8c4b35b6663e {
   @Accessor("leftPos")
   int larpLeftPos();

   @Accessor("topPos")
   int larpTopPos();

   @Accessor("imageWidth")
   int larpImageWidth();

   @Accessor("imageHeight")
   int larpImageHeight();
}
