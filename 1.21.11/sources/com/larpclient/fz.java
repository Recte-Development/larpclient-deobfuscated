package com.larpclient;

public final class fz {
   private final int[] w = new int[36];
   private final boolean[] e = new boolean[36];
   private long i = -1L;
   private long T = -1L;
   private long h;
   private int[] f;

   public fz() {
      int var1 = 0;

      while (var1 < 36) {
         this.w[var1] = var1++;
      }
   }

   public int[] g(long var1, long var3) {
      if (this.f != null && var1 == this.i && var3 == this.T) {
         return this.f;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.z(var5, var6);
      this.i = var1;
      this.T = var3;
      this.f = this.N(var6);
      this.h++;
      return this.f;
   }

   public long N() {
      return this.h;
   }

   public void X(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.w, 0, 36);
      this.f = null;
   }

   public void z(boolean[] var1, boolean[] var2) {
      this.f = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.w[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.w[var4] = -1;
               this.e[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.w[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.w[var9];
                  if (var6 >= 0 && var1[var6] && !this.e[var9]) {
                     this.w[var7] = var6;
                     this.w[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.w[var8] < 0 ? var8 : this.Z();
               if (var10 < 0) {
                  break;
               }

               this.w[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int Z() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.w[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] N(boolean[] var1) {
      int[] var2 = (int[])this.w.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void s(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.f = null;
         int var3 = this.w[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.w[var5] == var2) {
               this.w[var5] = var3;
               this.e[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.w[var1] = var2;
         this.e[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.Z();
            if (var6 >= 0) {
               this.w[var6] = var3;
               this.e[var6] = (boolean)0;
            }
         }
      }
   }

   public void f(int var1, int var2) {
      this.f = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.w[var3] == var1) {
            this.w[var3] = var2;
         } else if (this.w[var3] == var2) {
            this.w[var3] = var1;
         }
      }
   }
}
