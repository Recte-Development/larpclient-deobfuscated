package com.larpclient;

public final class lz {
   private final int[] Q = new int[36];
   private final boolean[] g = new boolean[36];
   private long l = -1L;
   private long s = -1L;
   private long H;
   private int[] N;

   public lz() {
      int var1 = 0;

      while (var1 < 36) {
         this.Q[var1] = var1++;
      }
   }

   public int[] Y(long var1, long var3) {
      if (this.N != null && var1 == this.l && var3 == this.s) {
         return this.N;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.j(var5, var6);
      this.l = var1;
      this.s = var3;
      this.N = this.L(var6);
      this.H++;
      return this.N;
   }

   public long p() {
      return this.H;
   }

   public void G(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.Q, 0, 36);
      this.N = null;
   }

   public void j(boolean[] var1, boolean[] var2) {
      this.N = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.Q[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.Q[var4] = -1;
               this.g[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.Q[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.Q[var9];
                  if (var6 >= 0 && var1[var6] && !this.g[var9]) {
                     this.Q[var7] = var6;
                     this.Q[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.Q[var8] < 0 ? var8 : this.E();
               if (var10 < 0) {
                  break;
               }

               this.Q[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int E() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.Q[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] L(boolean[] var1) {
      int[] var2 = (int[])this.Q.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void H(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.N = null;
         int var3 = this.Q[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.Q[var5] == var2) {
               this.Q[var5] = var3;
               this.g[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.Q[var1] = var2;
         this.g[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.E();
            if (var6 >= 0) {
               this.Q[var6] = var3;
               this.g[var6] = (boolean)0;
            }
         }
      }
   }

   public void d(int var1, int var2) {
      this.N = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.Q[var3] == var1) {
            this.Q[var3] = var2;
         } else if (this.Q[var3] == var2) {
            this.Q[var3] = var1;
         }
      }
   }
}
