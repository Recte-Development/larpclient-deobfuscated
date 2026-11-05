package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface qa1ea7ee4f1c0c80a {
   @Accessor("guiRenderState")
   GuiRenderState larpGuiRenderState();
}
