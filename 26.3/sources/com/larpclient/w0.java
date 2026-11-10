package com.larpclient;

import java.nio.ByteBuffer;

public final class w0 extends wm {
   public w0() {
      super("israel_flag");
   }

   @Override
   protected void write(pa var1, ByteBuffer var2) {
      j(var2, (float)this.H, var1.e.T(), var1.e.G() ? 1.0F : 0.0F, 0.0F);
   }
}
