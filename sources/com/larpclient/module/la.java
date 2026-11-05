package com.larpclient.module;

import com.larpclient.pe;

public final class la extends l6<Integer> {
   private boolean S = (boolean)1;

   public la(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public la p() {
      this.S = (boolean)0;
      return this;
   }

   public boolean b() {
      return this.S;
   }

   public void k(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (pe.o(this.getValue())) {
         return pe.B(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
