package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._d;
import com.larpclient.r4;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.util.function.Supplier;
import net.minecraft.client.gui.font.FontTexture;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontTexture.class)
public abstract class q200c1ca2fc3eeadb extends AbstractTexture {
   @Inject(method = "<init>", at = @At("RETURN"))
   private void p(Supplier<String> var1, GlyphRenderTypes var2, boolean var3, CallbackInfo var4) {
      String var5 = (String)var1.get();
      if (_d.R(this.texture, var5, var3)) {
         this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         r4.g("Font", "Linear UI glyph sampling selected for {}", var5);
      }
   }
}
