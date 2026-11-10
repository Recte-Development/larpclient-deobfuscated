package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface q08272dc7904e0a01 {
   @Accessor("guiRenderState")
   GuiRenderState larpGuiRenderState();
}
