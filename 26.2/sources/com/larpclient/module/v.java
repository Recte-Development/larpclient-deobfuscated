package com.larpclient.module;

import com.larpclient.bc;

public final class v extends q<Integer> {
   private boolean y = (boolean)1;

   public v(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public v Q() {
      this.y = (boolean)0;
      return this;
   }

   public boolean s() {
      return this.y;
   }

   public void u(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (bc.Q(this.getValue())) {
         return bc.E(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
