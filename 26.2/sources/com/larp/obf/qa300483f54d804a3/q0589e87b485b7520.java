package com.larp.obf.qa300483f54d804a3;

import com.larpclient.g9;
import com.larpclient.h;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.util.function.Supplier;
import net.minecraft.client.gui.font.FontTexture;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontTexture.class)
public abstract class q0589e87b485b7520 extends AbstractTexture {
   @Inject(method = "<init>", at = @At("RETURN"))
   private void v(Supplier<String> var1, GlyphRenderTypes var2, boolean var3, CallbackInfo var4) {
      String var5 = (String)var1.get();
      if (g9.z(this.texture, var5, var3)) {
         this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         h.U("Font", "Linear UI glyph sampling selected for {}", var5);
      }
   }
}
