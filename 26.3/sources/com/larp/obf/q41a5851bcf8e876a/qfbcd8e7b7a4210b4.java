package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface qfbcd8e7b7a4210b4 {
   @Accessor("leftPos")
   int larpLeftPos();

   @Accessor("topPos")
   int larpTopPos();

   @Accessor("imageWidth")
   int larpImageWidth();

   @Accessor("imageHeight")
   int larpImageHeight();
}
