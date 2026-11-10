package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class jr {
   private int[] A;
   private int[] R;
   private final Map<Integer, ArrayDeque<Integer>> N = new HashMap<>();

   public void j() {
      this.A = null;
      this.R = null;
      this.N.clear();
   }

   public int f(int var1, int var2) {
      ArrayDeque var3 = this.N.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean c(int var1, int var2) {
      if (this.A != null && this.A[var1] > 0 && this.A[var1] == this.A[var2] && this.R[var1] != this.R[var2]) {
         int var3 = this.R[var1];
         this.R[var1] = this.R[var2];
         this.R[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean p() {
      return this.A != null;
   }

   public int p(int var1) {
      return this.R[var1];
   }

   public void P(int[] var1, int[] var2) {
      if (this.A == null) {
         this.A = (int[])var1.clone();
         this.R = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.A[var3] && this.A[var3] > 0) {
               this.N.computeIfAbsent(this.A[var3], var0 -> new ArrayDeque<>()).add(this.R[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.A[var5]) {
               ArrayDeque var4 = this.N.get(var1[var5]);
               this.R[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.A[var5] = var1[var5];
            }
         }
      }
   }
}
