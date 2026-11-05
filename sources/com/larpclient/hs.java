package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class hs<T> {
   private final boolean[] g;
   private final boolean[] w;
   private final boolean[] C;
   private final LinkedHashSet<T> K = new LinkedHashSet<>();

   public hs(int var1) {
      this.g = new boolean[var1];
      this.w = new boolean[var1];
      this.C = new boolean[var1];
   }

   public void Y(int var1) {
      if (var1 > 0 && var1 < this.C.length) {
         this.C[var1] = (boolean)1;
      }
   }

   public void T(int var1) {
      if (var1 > 0 && var1 < this.C.length) {
         this.g[var1] = (boolean)1;
         this.C[var1] = (boolean)0;
      }
   }

   public void x() {
      Arrays.fill(this.C, false);
   }

   public void R() {
      this.K.clear();
      Arrays.fill(this.w, false);
   }

   public void T(T var1) {
      if (var1 != null) {
         this.K.add((T)var1);
      }
   }

   public void I(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.w.length) {
         this.w[var2] = var3;
         if (this.C[var2] || var3 && !this.g[var2]) {
            this.T((T)var1);
         }
      }
   }

   public List<T> g() {
      System.arraycopy(this.w, 0, this.g, 0, this.w.length);
      this.x();
      return List.copyOf(this.K);
   }
}
