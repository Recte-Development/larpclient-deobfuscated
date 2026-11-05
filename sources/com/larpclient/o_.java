package com.larpclient;

import com.larpclient.module.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class o_ extends oq {
   private final SkyShaderModule B;

   public o_(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.B = var2;
   }

   @Override
   protected void advance(e_ var1) {
      this.a = this.a + var1.R * this.B.V();
   }

   @Override
   protected void write(e_ var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      s(var2, (float)this.a, var1.F.width, var1.F.height, 0.0F);
      int var4 = this.B.i();
      s(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.B.w());
      s(var2, 1.0F, this.B.g(), this.B.q(), this.B.K());
      s(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
