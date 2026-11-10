package com.larpclient;

import com.larpclient.module.SkyShaderModule;
import java.nio.ByteBuffer;
import net.minecraft.class_310;
import net.minecraft.class_4184;

public final class a extends o {
   private final SkyShaderModule v;

   public a(String var1, SkyShaderModule var2) {
      super("larp/" + var1);
      this.v = var2;
   }

   @Override
   protected void advance(gx var1) {
      this.y = this.y + var1.t * this.v.B();
   }

   @Override
   protected void write(gx var1, ByteBuffer var2) {
      class_4184 var3 = class_310.method_1551().field_1773.method_19418();
      J(var2, (float)this.y, var1.X.field_1482, var1.X.field_1481, 0.0F);
      int var4 = this.v.r();
      J(var2, (var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, this.v.g());
      J(var2, 1.0F, this.v.z(), this.v.c(), this.v.S());
      J(var2, (float)Math.toRadians(var3.method_19330()), (float)Math.toRadians(var3.method_19329()), b9.U(), 0.0F);
   }
}
