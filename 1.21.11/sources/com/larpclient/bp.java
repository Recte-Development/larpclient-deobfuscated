package com.larpclient;

import java.util.Arrays;

public final class bp {
   private String K = "";
   private long[] F = new long[0];
   private long i;

   public void r(String var1, long var2, boolean var4) {
      this.i = var2;
      if (!var4) {
         if (!var1.equals(this.K)) {
            this.F = new long[var1.length()];
         }

         this.K = var1;
         Arrays.fill(this.F, Long.MIN_VALUE);
      } else if (!var1.equals(this.K)) {
         int var5 = 0;

         while (var5 < this.K.length() && var5 < var1.length() && this.K.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.K.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.K.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.K.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.F, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.F, var6, var8, var7, this.K.length() - var6);
         this.K = var1;
         this.F = var8;
      }
   }

   public double W(int var1) {
      return var1 >= 0 && var1 < this.F.length && this.F[var1] != Long.MIN_VALUE ? qb.y.E.f((this.i - this.F[var1]) / 1.8E8) : 1.0;
   }

   public double j(int var1) {
      return 3.0 * (1.0 - this.W(var1));
   }

   public int d(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.W(var2))) << 24;
   }

   public boolean q() {
      for (int var1 = 0; var1 < this.F.length; var1++) {
         if (this.W(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int s() {
      return this.F.length;
   }

   public static final class s {
      private int r;

      public s(int var1) {
         this.r = var1;
      }

      public int t(int var1) {
         int var2 = this.r;
         this.r = this.r + Character.charCount(var1);
         return var2;
      }
   }
}
