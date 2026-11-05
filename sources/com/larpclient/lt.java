package com.larpclient;

import java.util.Arrays;

public final class lt {
   private String r = "";
   private long[] v = new long[0];
   private long Z;

   public void p(String var1, long var2, boolean var4) {
      this.Z = var2;
      if (!var4) {
         if (!var1.equals(this.r)) {
            this.v = new long[var1.length()];
         }

         this.r = var1;
         Arrays.fill(this.v, Long.MIN_VALUE);
      } else if (!var1.equals(this.r)) {
         int var5 = 0;

         while (var5 < this.r.length() && var5 < var1.length() && this.r.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.r.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.r.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.r.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.v, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.v, var6, var8, var7, this.r.length() - var6);
         this.r = var1;
         this.v = var8;
      }
   }

   public double m(int var1) {
      return var1 >= 0 && var1 < this.v.length && this.v[var1] != Long.MIN_VALUE ? u7.p.y.m((this.Z - this.v[var1]) / 1.8E8) : 1.0;
   }

   public double O(int var1) {
      return 3.0 * (1.0 - this.m(var1));
   }

   public int X(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.m(var2))) << 24;
   }

   public boolean n() {
      for (int var1 = 0; var1 < this.v.length; var1++) {
         if (this.m(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int u() {
      return this.v.length;
   }

   public static final class k {
      private int G;

      public k(int var1) {
         this.G = var1;
      }

      public int k(int var1) {
         int var2 = this.G;
         this.G = this.G + Character.charCount(var1);
         return var2;
      }
   }
}
