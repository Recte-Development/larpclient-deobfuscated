package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.qe1de85825286c614;

import com.larpclient.mx;
import com.larpclient.w6;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkSectionsToRender.class)
public abstract class qb463e35c05f5bb0b {
   @Redirect(
      method = "renderGroup",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V")
   )
   private void e(RenderPass var1, RenderPipeline var2) {
      RenderPipeline var3 = w6.n(var2);
      mx.i(var1, var3);
      w6.C(var1, var3);
   }
}
