package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class pf<T> {
   private final boolean[] N;
   private final boolean[] R;
   private final boolean[] O;
   private final LinkedHashSet<T> d = new LinkedHashSet<>();

   public pf(int var1) {
      this.N = new boolean[var1];
      this.R = new boolean[var1];
      this.O = new boolean[var1];
   }

   public void h(int var1) {
      if (var1 > 0 && var1 < this.O.length) {
         this.O[var1] = (boolean)1;
      }
   }

   public void T(int var1) {
      if (var1 > 0 && var1 < this.O.length) {
         this.N[var1] = (boolean)1;
         this.O[var1] = (boolean)0;
      }
   }

   public void S() {
      Arrays.fill(this.O, false);
   }

   public void i() {
      this.d.clear();
      Arrays.fill(this.R, false);
   }

   public void T(T var1) {
      if (var1 != null) {
         this.d.add((T)var1);
      }
   }

   public void M(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.R.length) {
         this.R[var2] = var3;
         if (this.O[var2] || var3 && !this.N[var2]) {
            this.T((T)var1);
         }
      }
   }

   public List<T> J() {
      System.arraycopy(this.R, 0, this.N, 0, this.R.length);
      this.S();
      return List.copyOf(this.d);
   }
}
