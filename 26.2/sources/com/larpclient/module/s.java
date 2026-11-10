package com.larpclient.module;

public class s extends q<Boolean> {
   public s(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void c() {
      this.setValue(!this.getValue());
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
