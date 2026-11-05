package com.larpclient;

import java.nio.ByteBuffer;

public final class u0 extends ux {
   public u0() {
      super("caustics");
   }

   @Override
   protected void write(lb var1, ByteBuffer var2) {
      this.K(var1, var2);
      K(var2, var1.o.S(), var1.o.W(), var1.o.K(), 0.0F);
   }
}
