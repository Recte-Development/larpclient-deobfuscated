package com.larpclient.module;

import com.larpclient.cc;

public final class dv extends df<Integer> {
   private boolean d = (boolean)1;

   public dv(String var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public dv b() {
      this.d = (boolean)0;
      return this;
   }

   public boolean s() {
      return this.d;
   }

   public void T(Integer var1) {
      if (var1 >= -1 && var1 <= 348) {
         super.setValue(var1);
      } else {
         throw new IllegalArgumentException("Invalid key code");
      }
   }

   @Override
   public String getValueAsString() {
      if (cc.h(this.P())) {
         return cc.U(this.P());
      } else {
         return this.P() < 0 ? "Unbound" : "Key " + this.P();
      }
   }
}
