package com.larp.obf.qbac0b262827a15ac;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkinTextureDownloader.class)
public interface q6b37d34510312db9 {
   @Invoker("processLegacySkin")
   static NativeImage larpProcessSkin(NativeImage var0, String var1) {
      throw new AssertionError("Mixin invoker");
   }
}
