package com.larpclient.module;

public class l3 extends lb<Boolean> {
   public l3(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void h() {
      this.setValue(!this.getValue());
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
