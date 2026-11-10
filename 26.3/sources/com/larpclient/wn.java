package com.larpclient;

import java.nio.ByteBuffer;

public final class wn extends wm {
   public wn() {
      super("caustics");
   }

   @Override
   protected void write(pa var1, ByteBuffer var2) {
      this.G(var1, var2);
      j(var2, var1.e.x(), var1.e.G(), var1.e.p(), 0.0F);
   }
}
