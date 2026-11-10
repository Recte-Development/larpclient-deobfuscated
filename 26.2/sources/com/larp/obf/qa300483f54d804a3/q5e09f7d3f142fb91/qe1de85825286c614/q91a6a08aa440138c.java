package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.qe1de85825286c614;

import com.larpclient.zx;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BufferBuilder.class)
public abstract class q91a6a08aa440138c {
   @ModifyVariable(method = "addVertex(FFFIFFIIFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int q(int var1, float var2, float var3, float var4) {
      return zx.K(var1, var2, var3, var4);
   }
}
