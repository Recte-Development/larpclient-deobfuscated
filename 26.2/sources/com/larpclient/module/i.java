package com.larpclient.module;

public final class i extends q<Integer> {
   public i(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   @Override
   public String getValueAsString() {
      return String.format("#%08X", this.getValue());
   }
}
