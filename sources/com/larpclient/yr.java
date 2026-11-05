package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class yr {
   private int[] j;
   private int[] i;
   private final Map<Integer, ArrayDeque<Integer>> h = new HashMap<>();

   public void A() {
      this.j = null;
      this.i = null;
      this.h.clear();
   }

   public int I(int var1, int var2) {
      ArrayDeque var3 = this.h.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean f(int var1, int var2) {
      if (this.j != null && this.j[var1] > 0 && this.j[var1] == this.j[var2] && this.i[var1] != this.i[var2]) {
         int var3 = this.i[var1];
         this.i[var1] = this.i[var2];
         this.i[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean d() {
      return this.j != null;
   }

   public int b(int var1) {
      return this.i[var1];
   }

   public void n(int[] var1, int[] var2) {
      if (this.j == null) {
         this.j = (int[])var1.clone();
         this.i = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.j[var3] && this.j[var3] > 0) {
               this.h.computeIfAbsent(this.j[var3], var0 -> new ArrayDeque<>()).add(this.i[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.j[var5]) {
               ArrayDeque var4 = this.h.get(var1[var5]);
               this.i[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.j[var5] = var1[var5];
            }
         }
      }
   }
}
