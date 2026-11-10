package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class jj<T> {
   private final boolean[] f;
   private final boolean[] D;
   private final boolean[] q;
   private final LinkedHashSet<T> Q = new LinkedHashSet<>();

   public jj(int var1) {
      this.f = new boolean[var1];
      this.D = new boolean[var1];
      this.q = new boolean[var1];
   }

   public void D(int var1) {
      if (var1 > 0 && var1 < this.q.length) {
         this.q[var1] = (boolean)1;
      }
   }

   public void h(int var1) {
      if (var1 > 0 && var1 < this.q.length) {
         this.f[var1] = (boolean)1;
         this.q[var1] = (boolean)0;
      }
   }

   public void e() {
      Arrays.fill(this.q, false);
   }

   public void G() {
      this.Q.clear();
      Arrays.fill(this.D, false);
   }

   public void s(T var1) {
      if (var1 != null) {
         this.Q.add((T)var1);
      }
   }

   public void x(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.D.length) {
         this.D[var2] = var3;
         if (this.q[var2] || var3 && !this.f[var2]) {
            this.s((T)var1);
         }
      }
   }

   public List<T> Y() {
      System.arraycopy(this.D, 0, this.f, 0, this.D.length);
      this.e();
      return List.copyOf(this.Q);
   }
}
