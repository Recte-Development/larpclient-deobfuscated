package com.larpclient.render.sky;

import com.larpclient.module.render.CustomSkyModule;
import com.mojang.blaze3d.pipeline.RenderTarget;

public final class SkyRenderContext {
   public final SkyRenderer state = new SkyRenderer();
   public RenderTarget target;
   public CustomSkyModule skyModule;
   public double partialTick;
}
