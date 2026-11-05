package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class pm {
   private int[] x;
   private int[] n;
   private final Map<Integer, ArrayDeque<Integer>> J = new HashMap<>();

   public void c() {
      this.x = null;
      this.n = null;
      this.J.clear();
   }

   public int v(int var1, int var2) {
      ArrayDeque var3 = this.J.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean m(int var1, int var2) {
      if (this.x != null && this.x[var1] > 0 && this.x[var1] == this.x[var2] && this.n[var1] != this.n[var2]) {
         int var3 = this.n[var1];
         this.n[var1] = this.n[var2];
         this.n[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean f() {
      return this.x != null;
   }

   public int X(int var1) {
      return this.n[var1];
   }

   public void O(int[] var1, int[] var2) {
      if (this.x == null) {
         this.x = (int[])var1.clone();
         this.n = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.x[var3] && this.x[var3] > 0) {
               this.J.computeIfAbsent(this.x[var3], var0 -> new ArrayDeque<>()).add(this.n[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.x[var5]) {
               ArrayDeque var4 = this.J.get(var1[var5]);
               this.n[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.x[var5] = var1[var5];
            }
         }
      }
   }
}
