package com.larpclient.module.misc;

import com.larpclient.util.data.BindCodes;

public final class KeybindSetting extends Setting<Integer> {
   private boolean toggleable = (boolean)1;

   public KeybindSetting(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public KeybindSetting disableToggle() {
      this.toggleable = (boolean)0;
      return this;
   }

   public boolean isToggleable() {
      return this.toggleable;
   }

   public void setKeyCode(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (BindCodes.isMouseBind(this.getValue())) {
         return BindCodes.isPressed(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
