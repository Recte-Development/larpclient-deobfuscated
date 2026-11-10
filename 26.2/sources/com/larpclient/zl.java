package com.larpclient;

import com.larpclient.module.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class zl extends z5 {
   private final SkyShaderModule x;

   public zl(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.x = var2;
   }

   @Override
   protected void advance(wj var1) {
      this.O = this.O + var1.k * this.x.k();
   }

   @Override
   protected void write(wj var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      B(var2, (float)this.O, var1.S.width, var1.S.height, 0.0F);
      int var4 = this.x.n();
      B(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.x.O());
      B(var2, 1.0F, this.x.r(), this.x.W(), this.x.L());
      B(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
