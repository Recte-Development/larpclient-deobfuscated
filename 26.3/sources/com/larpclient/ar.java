package com.larpclient;

import java.util.Arrays;

public final class ar {
   private String j = "";
   private long[] l = new long[0];
   private long K;

   public void l(String var1, long var2, boolean var4) {
      this.K = var2;
      if (!var4) {
         if (!var1.equals(this.j)) {
            this.l = new long[var1.length()];
         }

         this.j = var1;
         Arrays.fill(this.l, Long.MIN_VALUE);
      } else if (!var1.equals(this.j)) {
         int var5 = 0;

         while (var5 < this.j.length() && var5 < var1.length() && this.j.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.j.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.j.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.j.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.l, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.l, var6, var8, var7, this.j.length() - var6);
         this.j = var1;
         this.l = var8;
      }
   }

   public double N(int var1) {
      return var1 >= 0 && var1 < this.l.length && this.l[var1] != Long.MIN_VALUE ? fq.y.u.q((this.K - this.l[var1]) / 1.8E8) : 1.0;
   }

   public double i(int var1) {
      return 3.0 * (1.0 - this.N(var1));
   }

   public int B(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.N(var2))) << 24;
   }

   public boolean l() {
      for (int var1 = 0; var1 < this.l.length; var1++) {
         if (this.N(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int D() {
      return this.l.length;
   }

   public static final class b {
      private int Z;

      public b(int var1) {
         this.Z = var1;
      }

      public int j(int var1) {
         int var2 = this.Z;
         this.Z = this.Z + Character.charCount(var1);
         return var2;
      }
   }
}
