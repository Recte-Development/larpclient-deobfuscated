package com.larpclient.module;

public class dh extends df<Boolean> {
   public dh(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void T() {
      this.setValue(!this.P());
   }

   @Override
   public String getValueAsString() {
      return this.P() ? "ON" : "OFF";
   }
}
