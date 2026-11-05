package com.larpclient;

import java.util.Arrays;

public final class zn {
   private String X = "";
   private long[] a = new long[0];
   private long n;

   public void I(String var1, long var2, boolean var4) {
      this.n = var2;
      if (!var4) {
         if (!var1.equals(this.X)) {
            this.a = new long[var1.length()];
         }

         this.X = var1;
         Arrays.fill(this.a, Long.MIN_VALUE);
      } else if (!var1.equals(this.X)) {
         int var5 = 0;

         while (var5 < this.X.length() && var5 < var1.length() && this.X.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.X.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.X.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.X.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.a, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.a, var6, var8, var7, this.X.length() - var6);
         this.X = var1;
         this.a = var8;
      }
   }

   public double C(int var1) {
      return var1 >= 0 && var1 < this.a.length && this.a[var1] != Long.MIN_VALUE ? ef.q.m.k((this.n - this.a[var1]) / 1.8E8) : 1.0;
   }

   public double z(int var1) {
      return 3.0 * (1.0 - this.C(var1));
   }

   public int V(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.C(var2))) << 24;
   }

   public boolean h() {
      for (int var1 = 0; var1 < this.a.length; var1++) {
         if (this.C(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int l() {
      return this.a.length;
   }

   public static final class g {
      private int X;

      public g(int var1) {
         this.X = var1;
      }

      public int i(int var1) {
         int var2 = this.X;
         this.X = this.X + Character.charCount(var1);
         return var2;
      }
   }
}
