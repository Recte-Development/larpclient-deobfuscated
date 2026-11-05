package com.larpclient;

public final class xv {
   private final int[] W = new int[36];
   private final boolean[] Q = new boolean[36];
   private long O = -1L;
   private long I = -1L;
   private long j;
   private int[] l;

   public xv() {
      int var1 = 0;

      while (var1 < 36) {
         this.W[var1] = var1++;
      }
   }

   public int[] S(long var1, long var3) {
      if (this.l != null && var1 == this.O && var3 == this.I) {
         return this.l;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.v(var5, var6);
      this.O = var1;
      this.I = var3;
      this.l = this.H(var6);
      this.j++;
      return this.l;
   }

   public long B() {
      return this.j;
   }

   public void X(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.W, 0, 36);
      this.l = null;
   }

   public void v(boolean[] var1, boolean[] var2) {
      this.l = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.W[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.W[var4] = -1;
               this.Q[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.W[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.W[var9];
                  if (var6 >= 0 && var1[var6] && !this.Q[var9]) {
                     this.W[var7] = var6;
                     this.W[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.W[var8] < 0 ? var8 : this.C();
               if (var10 < 0) {
                  break;
               }

               this.W[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int C() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.W[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] H(boolean[] var1) {
      int[] var2 = (int[])this.W.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void F(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.l = null;
         int var3 = this.W[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.W[var5] == var2) {
               this.W[var5] = var3;
               this.Q[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.W[var1] = var2;
         this.Q[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.C();
            if (var6 >= 0) {
               this.W[var6] = var3;
               this.Q[var6] = (boolean)0;
            }
         }
      }
   }

   public void B(int var1, int var2) {
      this.l = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.W[var3] == var1) {
            this.W[var3] = var2;
         } else if (this.W[var3] == var2) {
            this.W[var3] = var1;
         }
      }
   }
}
