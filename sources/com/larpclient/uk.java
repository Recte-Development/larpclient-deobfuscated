package com.larpclient;

import com.larpclient.module.HalloweenModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class uk extends ux {
   private static int w = -15260879;

   public uk() {
      super("larp/after_dark");
   }

   public static void d(int var0) {
      w = var0;
   }

   @Override
   protected void advance(lb var1) {
      this.E = this.E + var1.N;
   }

   @Override
   protected void write(lb var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      ol var4 = HalloweenModule.U().t();
      K(var2, (float)this.E, var1.x.width, var1.x.height, 0.0F);
      y(var2, w);
      K(var2, (float)var4.D, (float)var4.j, var4.d() > 12 ? 4.0F : 3.0F, 0.0F);
      K(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
