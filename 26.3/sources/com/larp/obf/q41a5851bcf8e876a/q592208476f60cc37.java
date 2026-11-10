package com.larp.obf.q41a5851bcf8e876a;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkinTextureDownloader.class)
public interface q592208476f60cc37 {
   @Invoker("processLegacySkin")
   static NativeImage larpProcessSkin(NativeImage var0, String var1) {
      throw new AssertionError("Mixin invoker");
   }
}
