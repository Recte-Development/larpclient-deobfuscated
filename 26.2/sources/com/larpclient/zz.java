package com.larpclient;

import java.nio.ByteBuffer;

public final class zz extends z5 {
   public zz() {
      super("caustics");
   }

   @Override
   protected void write(wj var1, ByteBuffer var2) {
      this.m(var1, var2);
      B(var2, var1.n.m(), var1.n.L(), var1.n.Q(), 0.0F);
   }
}
