package com.larpclient;

import java.util.random.RandomGenerator;

public final class s7 {
   private s7.i o = s7.i.F;
   private int L;
   private int Q = 1;

   public s7.i u() {
      return this.o;
   }

   public int k() {
      return this.L;
   }

   public boolean C() {
      return this.L >= this.Q;
   }

   public boolean D() {
      return this.L >= this.Q + 20;
   }

   public void x() {
      if (this.L < Integer.MAX_VALUE) {
         this.L++;
      }
   }

   public void o(s7.i var1, int var2) {
      this.o = var1;
      this.Q = Math.max(0, var2);
      this.L = 0;
   }

   public void L() {
      this.o(s7.i.F, 1);
   }

   public static int o(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int F(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean h(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean M(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean G(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean k(String var0) {
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

   public static final class g {
      private long J;
      private int c;

      public void i(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.c = var1;
            this.J = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int r(long var1) {
         if (this.c != 0 && var1 - this.J >= 1000000000L) {
            int var3 = this.c;
            this.W();
            return var3;
         } else {
            return 0;
         }
      }

      public void W() {
         this.c = 0;
         this.J = 0L;
      }

      public boolean H() {
         return this.c != 0;
      }
   }

   public enum i {
      F,
      j,
      N,
      M,
      H,
      Z,
      G,
      d,
      i,
      o;
   }
}
