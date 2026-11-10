package com.larpclient;

public final class a8 {
   private final int[] S = new int[36];
   private final boolean[] J = new boolean[36];
   private long u = -1L;
   private long T = -1L;
   private long n;
   private int[] d;

   public a8() {
      int var1 = 0;

      while (var1 < 36) {
         this.S[var1] = var1++;
      }
   }

   public int[] h(long var1, long var3) {
      if (this.d != null && var1 == this.u && var3 == this.T) {
         return this.d;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.m(var5, var6);
      this.u = var1;
      this.T = var3;
      this.d = this.p(var6);
      this.n++;
      return this.d;
   }

   public long X() {
      return this.n;
   }

   public void W(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.S, 0, 36);
      this.d = null;
   }

   public void m(boolean[] var1, boolean[] var2) {
      this.d = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.S[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.S[var4] = -1;
               this.J[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.S[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.S[var9];
                  if (var6 >= 0 && var1[var6] && !this.J[var9]) {
                     this.S[var7] = var6;
                     this.S[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.S[var8] < 0 ? var8 : this.W();
               if (var10 < 0) {
                  break;
               }

               this.S[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int W() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.S[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] p(boolean[] var1) {
      int[] var2 = (int[])this.S.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void T(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.d = null;
         int var3 = this.S[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.S[var5] == var2) {
               this.S[var5] = var3;
               this.J[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.S[var1] = var2;
         this.J[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.W();
            if (var6 >= 0) {
               this.S[var6] = var3;
               this.J[var6] = (boolean)0;
            }
         }
      }
   }

   public void D(int var1, int var2) {
      this.d = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.S[var3] == var1) {
            this.S[var3] = var2;
         } else if (this.S[var3] == var2) {
            this.S[var3] = var1;
         }
      }
   }
}
