package com.larpclient;

public final class mg {
   private final int[] N = new int[36];
   private final boolean[] E = new boolean[36];
   private long x = -1L;
   private long P = -1L;
   private long X;
   private int[] m;

   public mg() {
      int var1 = 0;

      while (var1 < 36) {
         this.N[var1] = var1++;
      }
   }

   public int[] W(long var1, long var3) {
      if (this.m != null && var1 == this.x && var3 == this.P) {
         return this.m;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.Z(var5, var6);
      this.x = var1;
      this.P = var3;
      this.m = this.l(var6);
      this.X++;
      return this.m;
   }

   public long p() {
      return this.X;
   }

   public void O(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.N, 0, 36);
      this.m = null;
   }

   public void Z(boolean[] var1, boolean[] var2) {
      this.m = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.N[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.N[var4] = -1;
               this.E[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.N[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.N[var9];
                  if (var6 >= 0 && var1[var6] && !this.E[var9]) {
                     this.N[var7] = var6;
                     this.N[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.N[var8] < 0 ? var8 : this.o();
               if (var10 < 0) {
                  break;
               }

               this.N[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int o() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.N[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] l(boolean[] var1) {
      int[] var2 = (int[])this.N.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void F(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.m = null;
         int var3 = this.N[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.N[var5] == var2) {
               this.N[var5] = var3;
               this.E[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.N[var1] = var2;
         this.E[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.o();
            if (var6 >= 0) {
               this.N[var6] = var3;
               this.E[var6] = (boolean)0;
            }
         }
      }
   }

   public void B(int var1, int var2) {
      this.m = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.N[var3] == var1) {
            this.N[var3] = var2;
         } else if (this.N[var3] == var2) {
            this.N[var3] = var1;
         }
      }
   }
}
