package com.larpclient.render.overlay;

import com.mojang.renderpearl.api.textures.GpuTextureView;
import org.jspecify.annotations.Nullable;

public final class OverlayTextures {
   public static @Nullable GpuTextureView colorTexture;
   public static @Nullable GpuTextureView depthTexture;

   private OverlayTextures() {
   }
}
