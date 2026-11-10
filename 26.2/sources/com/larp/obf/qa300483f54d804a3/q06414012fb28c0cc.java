package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface q06414012fb28c0cc {
   @Accessor("guiRenderState")
   GuiRenderState larpGuiRenderState();
}
