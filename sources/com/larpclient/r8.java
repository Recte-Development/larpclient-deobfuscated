package com.larpclient;

import java.util.random.RandomGenerator;

public final class r8 {
   private r8.v p = r8.v.G;
   private int n;
   private int A = 1;

   public r8.v I() {
      return this.p;
   }

   public int v() {
      return this.n;
   }

   public boolean L() {
      return this.n >= this.A;
   }

   public boolean G() {
      return this.n >= this.A + 20;
   }

   public void H() {
      if (this.n < Integer.MAX_VALUE) {
         this.n++;
      }
   }

   public void U(r8.v var1, int var2) {
      this.p = var1;
      this.A = Math.max(0, var2);
      this.n = 0;
   }

   public void b() {
      this.U(r8.v.G, 1);
   }

   public static int b(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int k(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean w(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean F(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean M(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean r(String var0) {
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

   public static final class i {
      private long S;
      private int e;

      public void H(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.e = var1;
            this.S = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int b(long var1) {
         if (this.e != 0 && var1 - this.S >= 1000000000L) {
            int var3 = this.e;
            this.k();
            return var3;
         } else {
            return 0;
         }
      }

      public void k() {
         this.e = 0;
         this.S = 0L;
      }

      public boolean l() {
         return this.e != 0;
      }
   }

   public enum v {
      G,
      g,
      V,
      e,
      S,
      Z,
      Q,
      K,
      E,
      m;
   }
}
