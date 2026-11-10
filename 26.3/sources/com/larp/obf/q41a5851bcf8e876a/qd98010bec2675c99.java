package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._q;
import com.larpclient.f5;
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
public abstract class qd98010bec2675c99 extends AbstractTexture {
   @Inject(method = "<init>", at = @At("RETURN"))
   private void u(Supplier<String> var1, GlyphRenderTypes var2, boolean var3, CallbackInfo var4) {
      String var5 = (String)var1.get();
      if (f5.P(this.texture, var5, var3)) {
         this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         _q.c("Font", "Linear UI glyph sampling selected for {}", var5);
      }
   }
}
