package com.larp.obf.q5682c6a8aca9b6ec;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkinTextureDownloader.class)
public interface qe213d8ff8a604d6b {
   @Invoker("processLegacySkin")
   static NativeImage larpProcessSkin(NativeImage var0, String var1) {
      throw new AssertionError("Mixin invoker");
   }
}
