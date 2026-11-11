package com.larpclient.module.misc;

public class BooleanSetting extends Setting<Boolean> {
   public BooleanSetting(String var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void toggle() {
      this.setValue(!this.getValue());
   }

   @Override
   public String getValueAsString() {
      return this.getValue() ? "ON" : "OFF";
   }
}
