package com.larp.obf.qa300483f54d804a3;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkinTextureDownloader.class)
public interface q177213fd6a234142 {
   @Invoker("processLegacySkin")
   static NativeImage larpProcessSkin(NativeImage var0, String var1) {
      throw new AssertionError("Mixin invoker");
   }
}
