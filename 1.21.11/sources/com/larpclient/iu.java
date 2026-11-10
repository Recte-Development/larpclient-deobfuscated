package com.larpclient;

import java.nio.ByteBuffer;

public final class iu extends o {
   public iu() {
      super("caustics");
   }

   @Override
   protected void write(gx var1, ByteBuffer var2) {
      this.J(var1, var2);
      J(var2, var1.w.p(), var1.w.t(), var1.w.B(), 0.0F);
   }
}
