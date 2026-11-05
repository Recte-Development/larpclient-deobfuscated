package com.larpclient;

import java.nio.ByteBuffer;

public final class o9 extends oq {
   public o9() {
      super("caustics");
   }

   @Override
   protected void write(e_ var1, ByteBuffer var2) {
      this.g(var1, var2);
      s(var2, var1.x.R(), var1.x.I(), var1.x.i(), 0.0F);
   }
}
