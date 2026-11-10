package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class da {
   private int[] i;
   private int[] K;
   private final Map<Integer, ArrayDeque<Integer>> m = new HashMap<>();

   public void B() {
      this.i = null;
      this.K = null;
      this.m.clear();
   }

   public int j(int var1, int var2) {
      ArrayDeque var3 = this.m.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean y(int var1, int var2) {
      if (this.i != null && this.i[var1] > 0 && this.i[var1] == this.i[var2] && this.K[var1] != this.K[var2]) {
         int var3 = this.K[var1];
         this.K[var1] = this.K[var2];
         this.K[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean t() {
      return this.i != null;
   }

   public int T(int var1) {
      return this.K[var1];
   }

   public void i(int[] var1, int[] var2) {
      if (this.i == null) {
         this.i = (int[])var1.clone();
         this.K = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.i[var3] && this.i[var3] > 0) {
               this.m.computeIfAbsent(this.i[var3], var0 -> new ArrayDeque<>()).add(this.K[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.i[var5]) {
               ArrayDeque var4 = this.m.get(var1[var5]);
               this.K[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.i[var5] = var1[var5];
            }
         }
      }
   }
}
