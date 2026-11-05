package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class cq<T> {
   private final boolean[] u;
   private final boolean[] c;
   private final boolean[] i;
   private final LinkedHashSet<T> V = new LinkedHashSet<>();

   public cq(int var1) {
      this.u = new boolean[var1];
      this.c = new boolean[var1];
      this.i = new boolean[var1];
   }

   public void k(int var1) {
      if (var1 > 0 && var1 < this.i.length) {
         this.i[var1] = (boolean)1;
      }
   }

   public void x(int var1) {
      if (var1 > 0 && var1 < this.i.length) {
         this.u[var1] = (boolean)1;
         this.i[var1] = (boolean)0;
      }
   }

   public void u() {
      Arrays.fill(this.i, false);
   }

   public void F() {
      this.V.clear();
      Arrays.fill(this.c, false);
   }

   public void J(T var1) {
      if (var1 != null) {
         this.V.add((T)var1);
      }
   }

   public void B(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.c.length) {
         this.c[var2] = var3;
         if (this.i[var2] || var3 && !this.u[var2]) {
            this.J((T)var1);
         }
      }
   }

   public List<T> y() {
      System.arraycopy(this.c, 0, this.u, 0, this.c.length);
      this.u();
      return List.copyOf(this.V);
   }
}
