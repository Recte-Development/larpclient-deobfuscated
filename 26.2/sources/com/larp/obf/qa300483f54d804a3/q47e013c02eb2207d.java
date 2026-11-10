package com.larp.obf.qa300483f54d804a3;

import com.larpclient.g9;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GlyphRenderState.class)
public abstract class q47e013c02eb2207d {
   @Shadow
   public abstract TextRenderable renderable();

   @ModifyArg(
      method = "textureSetup",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/SamplerCache;getClampToEdge(Lcom/mojang/blaze3d/textures/FilterMode;)Lcom/mojang/blaze3d/textures/GpuSampler;"
      ),
      index = 0
   )
   private FilterMode q(FilterMode var1) {
      GpuTextureView var2 = this.renderable().textureView();
      return var2 != null && g9.E(var2.texture()) ? FilterMode.LINEAR : var1;
   }
}
