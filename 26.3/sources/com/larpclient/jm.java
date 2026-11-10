package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class jm<T> {
   private final boolean[] Q;
   private final boolean[] U;
   private final boolean[] F;
   private final LinkedHashSet<T> V = new LinkedHashSet<>();

   public jm(int var1) {
      this.Q = new boolean[var1];
      this.U = new boolean[var1];
      this.F = new boolean[var1];
   }

   public void l(int var1) {
      if (var1 > 0 && var1 < this.F.length) {
         this.F[var1] = (boolean)1;
      }
   }

   public void y(int var1) {
      if (var1 > 0 && var1 < this.F.length) {
         this.Q[var1] = (boolean)1;
         this.F[var1] = (boolean)0;
      }
   }

   public void z() {
      Arrays.fill(this.F, false);
   }

   public void c() {
      this.V.clear();
      Arrays.fill(this.U, false);
   }

   public void k(T var1) {
      if (var1 != null) {
         this.V.add((T)var1);
      }
   }

   public void f(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.U.length) {
         this.U[var2] = var3;
         if (this.F[var2] || var3 && !this.Q[var2]) {
            this.k((T)var1);
         }
      }
   }

   public List<T> a() {
      System.arraycopy(this.U, 0, this.Q, 0, this.U.length);
      this.z();
      return List.copyOf(this.V);
   }
}
