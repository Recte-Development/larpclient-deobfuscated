package com.larpclient;

import com.larpclient.module.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class uw extends ux {
   private final SkyShaderModule t;

   public uw(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.t = var2;
   }

   @Override
   protected void advance(lb var1) {
      this.E = this.E + var1.N * this.t.J();
   }

   @Override
   protected void write(lb var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      K(var2, (float)this.E, var1.x.width, var1.x.height, 0.0F);
      int var4 = this.t.B();
      K(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.t.G());
      K(var2, 1.0F, this.t.f(), this.t.N(), this.t.n());
      K(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
