package com.larpclient;

import java.util.random.RandomGenerator;

public final class uk {
   private uk.r o = uk.r.s;
   private int p;
   private int j = 1;

   public uk.r o() {
      return this.o;
   }

   public int c() {
      return this.p;
   }

   public boolean X() {
      return this.p >= this.j;
   }

   public boolean P() {
      return this.p >= this.j + 20;
   }

   public void L() {
      if (this.p < Integer.MAX_VALUE) {
         this.p++;
      }
   }

   public void I(uk.r var1, int var2) {
      this.o = var1;
      this.j = Math.max(0, var2);
      this.p = 0;
   }

   public void S() {
      this.I(uk.r.s, 1);
   }

   public static int C(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int j(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean t(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean J(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean w(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean v(String var0) {
      if (var0 != null) {
         switch (var0) {
            case "air":
            case "lime_stained_glass_pane":
            case "green_stained_glass_pane":
            case "red_stained_glass_pane":
            case "black_stained_glass_pane":
            case "gray_stained_glass_pane":
               break;
            default:
               return true;
         }
      }

      return false;
   }

   public enum r {
      s,
      u,
      L,
      h,
      X,
      o,
      A,
      b,
      K,
      c;
   }

   public static final class t {
      private long Z;
      private int o;

      public void S(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.o = var1;
            this.Z = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int k(long var1) {
         if (this.o != 0 && var1 - this.Z >= 1000000000L) {
            int var3 = this.o;
            this.K();
            return var3;
         } else {
            return 0;
         }
      }

      public void K() {
         this.o = 0;
         this.Z = 0L;
      }

      public boolean v() {
         return this.o != 0;
      }
   }
}
