package com.larpclient;

import com.larpclient.module.HalloweenModule;
import java.nio.ByteBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public final class o7 extends oq {
   private static int Z = -15260879;

   public o7() {
      super("larp/after_dark");
   }

   public static void p(int var0) {
      Z = var0;
   }

   @Override
   protected void advance(e_ var1) {
      this.a = this.a + var1.R;
   }

   @Override
   protected void write(e_ var1, ByteBuffer var2) {
      Camera var3 = Minecraft.getInstance().gameRenderer.mainCamera();
      u8 var4 = HalloweenModule.X().s();
      s(var2, (float)this.a, var1.F.width, var1.F.height, 0.0F);
      K(var2, Z);
      s(var2, (float)var4.w, (float)var4.B, var4.z() > 12 ? 4.0F : 3.0F, 0.0F);
      s(var2, (float)Math.toRadians(var3.yRot()), (float)Math.toRadians(var3.xRot()), var3.getFov(), 0.0F);
   }
}
