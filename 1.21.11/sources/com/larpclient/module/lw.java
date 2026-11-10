package com.larpclient.module;

import com.larpclient.s5;

public final class lw extends lb<Integer> {
   private boolean H = (boolean)1;

   public lw(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public lw P() {
      this.H = (boolean)0;
      return this;
   }

   public boolean D() {
      return this.H;
   }

   public void J(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (s5.X(this.getValue())) {
         return s5.J(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
