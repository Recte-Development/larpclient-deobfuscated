package com.larpclient.module;

import com.larpclient.p4;

public final class x extends r<Integer> {
   private boolean w = (boolean)1;

   public x(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public x t() {
      this.w = (boolean)0;
      return this;
   }

   public boolean u() {
      return this.w;
   }

   public void b(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (p4.h(this.getValue())) {
         return p4.Y(this.getValue());
      } else {
         return this.getValue() < 0 ? "Unbound" : "Key " + this.getValue();
      }
   }
}
