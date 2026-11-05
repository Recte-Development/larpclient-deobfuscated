package com.larpclient;

import java.nio.ByteBuffer;

public final class uh extends ux {
   public uh() {
      super("israel_flag");
   }

   @Override
   protected void write(lb var1, ByteBuffer var2) {
      K(var2, (float)this.y, var1.o.h(), var1.o.A() ? 1.0F : 0.0F, 0.0F);
   }
}
