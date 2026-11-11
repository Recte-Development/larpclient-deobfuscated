package com.larpclient.render.sky;

import java.nio.ByteBuffer;

public final class CausticsSkyShader extends SkyShader {
   public CausticsSkyShader() {
      super("caustics");
   }

   @Override
   protected void write(SkyRenderContext var1, ByteBuffer var2) {
      this.writeCommonUniforms(var1, var2);
      putVec4(var2, var1.skyModule.getCausticScale(), var1.skyModule.isHorizonBlend(), var1.skyModule.getInstance(), 0.0F);
   }
}
