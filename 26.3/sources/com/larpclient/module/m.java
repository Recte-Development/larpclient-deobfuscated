package com.larpclient.module;

public class m extends r<Boolean> {
   public m(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void q() {
      this.setValue(!this.getValue());
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
