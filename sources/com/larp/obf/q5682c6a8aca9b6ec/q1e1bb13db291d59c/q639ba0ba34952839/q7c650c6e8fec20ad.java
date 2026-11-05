package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q639ba0ba34952839;

import com.larpclient.er;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BufferBuilder.class)
public abstract class q7c650c6e8fec20ad {
   @ModifyVariable(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int f(int var1, float var2, float var3, float var4) {
      return er.I(var1, var2, var3, var4);
   }
}
