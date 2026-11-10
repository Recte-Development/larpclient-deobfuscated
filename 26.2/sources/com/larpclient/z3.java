package com.larpclient;

import java.util.Arrays;

public final class z3 {
   private String T = "";
   private long[] E = new long[0];
   private long k;

   public void t(String var1, long var2, boolean var4) {
      this.k = var2;
      if (!var4) {
         if (!var1.equals(this.T)) {
            this.E = new long[var1.length()];
         }

         this.T = var1;
         Arrays.fill(this.E, Long.MIN_VALUE);
      } else if (!var1.equals(this.T)) {
         int var5 = 0;

         while (var5 < this.T.length() && var5 < var1.length() && this.T.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.T.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.T.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.T.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.E, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.E, var6, var8, var7, this.T.length() - var6);
         this.T = var1;
         this.E = var8;
      }
   }

   public double Y(int var1) {
      return var1 >= 0 && var1 < this.E.length && this.E[var1] != Long.MIN_VALUE ? jp.m.S.s((this.k - this.E[var1]) / 1.8E8) : 1.0;
   }

   public double A(int var1) {
      return 3.0 * (1.0 - this.Y(var1));
   }

   public int B(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.Y(var2))) << 24;
   }

   public boolean h() {
      for (int var1 = 0; var1 < this.E.length; var1++) {
         if (this.Y(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int v() {
      return this.E.length;
   }

   public static final class k {
      private int t;

      public k(int var1) {
         this.t = var1;
      }

      public int P(int var1) {
         int var2 = this.t;
         this.t = this.t + Character.charCount(var1);
         return var2;
      }
   }
}
