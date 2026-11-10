package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q6780db403bf6e5d8;

import com.larpclient.hj;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BufferBuilder.class)
public abstract class qe1179dc47a32f111 {
   @ModifyVariable(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int X(int var1, float var2, float var3, float var4) {
      return hj.m(var1, var2, var3, var4);
   }
}
