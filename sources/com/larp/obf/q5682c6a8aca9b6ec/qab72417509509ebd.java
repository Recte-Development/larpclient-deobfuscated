package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface qab72417509509ebd {
   @Accessor("guiRenderState")
   GuiRenderState larpGuiRenderState();
}
