package com.larpclient;

import java.nio.ByteBuffer;

public final class i4 extends o {
   public i4() {
      super("israel_flag");
   }

   @Override
   protected void write(gx var1, ByteBuffer var2) {
      J(var2, (float)this.T, var1.w.U(), var1.w.D() ? 1.0F : 0.0F, 0.0F);
   }
}
