package com.larpclient;

import java.nio.ByteBuffer;

public final class og extends oq {
   public og() {
      super("israel_flag");
   }

   @Override
   protected void write(e_ var1, ByteBuffer var2) {
      s(var2, (float)this.W, var1.x.d(), var1.x.X() ? 1.0F : 0.0F, 0.0F);
   }
}
