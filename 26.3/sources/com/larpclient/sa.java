package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public final class sa {
   private final int[] C = new int[4];
   private int k;

   public void U() {
      Arrays.fill(this.C, 0);
      this.k = 0;
   }

   public int B(int var1) {
      if (this.k == 0) {
         this.k = var1;
      }

      ArrayList var2 = new ArrayList();
      int var3 = this.C[1] + this.C[2] + this.C[3];

      for (int var4 = 1; var4 <= 3; var4++) {
         if (this.C[var4] < 3 && (var4 == this.k || this.C[var4] < 2 || this.C[this.k] == 3) && (var4 != this.k || this.C[var4] != 2 || var3 >= 4)) {
            for (int var5 = this.C[var4]; var5 < 3; var5++) {
               var2.add(var4);
            }
         }
      }

      if (var2.isEmpty()) {
         return this.k;
      }

      int var6 = (Integer)var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
      this.C[var6]++;
      return var6;
   }

   public static int[] Y(int[] var0) {
      int[] var1 = new int[3];
      int[] var2 = new int[9];
      if (var0.length != 9) {
         throw new IllegalArgumentException("Horse Racing requires nine items.");
      }

      for (int var3 = 0; var3 < 9; var3++) {
         int var4 = var0[var3];
         if (var4 < 0 || var4 > 2) {
            throw new IllegalArgumentException("Use three armor types, three of each.");
         }

         var1[var4]++;
         var2[var3] = var4 + 1;
      }

      for (int var6 : var1) {
         if (var6 != 3) {
            throw new IllegalArgumentException("Use three armor types, three of each.");
         }
      }

      return var2;
   }
}
