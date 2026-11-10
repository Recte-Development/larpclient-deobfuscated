package com.larpclient;

import com.larpclient.module.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class w3 extends wm {
   private final SkyShaderModule d;

   public w3(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.d = var2;
   }

   @Override
   protected void advance(pa var1) {
      this.K = this.K + var1.J * this.d.x();
   }

   @Override
   protected void write(pa var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      j(var2, (float)this.K, var1.V.width, var1.V.height, 0.0F);
      int var4 = this.d.B();
      j(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.d.T());
      j(var2, 1.0F, this.d.v(), this.d.l(), this.d.C());
      j(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
