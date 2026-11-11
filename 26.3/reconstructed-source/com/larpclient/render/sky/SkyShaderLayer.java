package com.larpclient.render.sky;

import com.larpclient.module.render.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class SkyShaderLayer extends SkyShader {
   private final SkyShaderModule module;

   public SkyShaderLayer(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.module = var2;
   }

   @Override
   protected void advance(SkyRenderContext var1) {
      this.time = this.time + var1.partialTick * this.module.getSpeed();
   }

   @Override
   protected void write(SkyRenderContext var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      putVec4(var2, (float)this.time, var1.target.width, var1.target.height, 0.0F);
      int var4 = this.module.getColor();
      putVec4(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.module.getAlpha());
      putVec4(var2, 1.0F, this.module.getScale(), this.module.getIntensity(), this.module.getLightningRate());
      putVec4(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
