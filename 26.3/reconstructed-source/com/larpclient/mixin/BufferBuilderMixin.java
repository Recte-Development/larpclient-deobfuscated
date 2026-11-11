package com.larpclient.mixin;

import com.larpclient.render.world.BlockRenderTags;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderMixin {
   @ModifyVariable(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int X(int var1, float var2, float var3, float var4) {
      return BlockRenderTags.tagVertexColor(var1, var2, var3, var4);
   }
}
