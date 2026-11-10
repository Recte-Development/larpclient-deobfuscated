package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.renderer.SectionOcclusionGraph.Node;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Node.class)
public interface q46ef8bacab480735 {
   @Invoker("<init>")
   static Node larpCreateNode(RenderSection var0, Direction var1, int var2) {
      throw new AssertionError("Freecam graph constructor invoker was not applied");
   }
}
