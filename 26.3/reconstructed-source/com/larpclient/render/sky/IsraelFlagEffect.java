package com.larpclient.render.sky;

import java.nio.ByteBuffer;

public final class IsraelFlagEffect extends SkyShader {
   public IsraelFlagEffect() {
      super("israel_flag");
   }

   @Override
   protected void write(SkyRenderContext var1, ByteBuffer var2) {
      putVec4(var2, (float)this.rotation, var1.skyModule.getExposure(), var1.skyModule.isHorizonBlend() ? 1.0F : 0.0F, 0.0F);
   }
}
