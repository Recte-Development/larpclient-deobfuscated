package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class q5 {
   private int[] e;
   private int[] v;
   private final Map<Integer, ArrayDeque<Integer>> p = new HashMap<>();

   public void b() {
      this.e = null;
      this.v = null;
      this.p.clear();
   }

   public int h(int var1, int var2) {
      ArrayDeque var3 = this.p.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean d(int var1, int var2) {
      if (this.e != null && this.e[var1] > 0 && this.e[var1] == this.e[var2] && this.v[var1] != this.v[var2]) {
         int var3 = this.v[var1];
         this.v[var1] = this.v[var2];
         this.v[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean E() {
      return this.e != null;
   }

   public int g(int var1) {
      return this.v[var1];
   }

   public void O(int[] var1, int[] var2) {
      if (this.e == null) {
         this.e = (int[])var1.clone();
         this.v = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.e[var3] && this.e[var3] > 0) {
               this.p.computeIfAbsent(this.e[var3], var0 -> new ArrayDeque<>()).add(this.v[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.e[var5]) {
               ArrayDeque var4 = this.p.get(var1[var5]);
               this.v[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.e[var5] = var1[var5];
            }
         }
      }
   }
}
