package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._s;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GlyphRenderState.class)
public abstract class qbfcf3fdafd7c155e {
   @Shadow
   public abstract TextRenderable renderable();

   @ModifyArg(
      method = "textureSetup",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/SamplerCache;getClampToEdge(Lcom/mojang/renderpearl/api/textures/FilterMode;)Lcom/mojang/renderpearl/api/textures/GpuSampler;"
      ),
      index = 0
   )
   private FilterMode D(FilterMode var1) {
      GpuTextureView var2 = this.renderable().textureView();
      return var2 != null && _s.p(var2.texture()) ? FilterMode.LINEAR : var1;
   }
}
