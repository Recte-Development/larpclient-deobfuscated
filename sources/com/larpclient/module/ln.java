package com.larpclient.module;

public class ln extends l6<Boolean> {
   public ln(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void S() {
      this.setValue(!this.getValue());
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
