package com.larpclient;

import java.util.random.RandomGenerator;

public final class k7 {
   private k7.y z = k7.y.K;
   private int k;
   private int I = 1;

   public k7.y R() {
      return this.z;
   }

   public int y() {
      return this.k;
   }

   public boolean o() {
      return this.k >= this.I;
   }

   public boolean p() {
      return this.k >= this.I + 20;
   }

   public void b() {
      if (this.k < Integer.MAX_VALUE) {
         this.k++;
      }
   }

   public void R(k7.y var1, int var2) {
      this.z = var1;
      this.I = Math.max(0, var2);
      this.k = 0;
   }

   public void T() {
      this.R(k7.y.K, 1);
   }

   public static int b(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int u(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean T(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean P(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean k(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean Y(String var0) {
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

   public static final class u {
      private long u;
      private int m;

      public void L(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.m = var1;
            this.u = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int a(long var1) {
         if (this.m != 0 && var1 - this.u >= 1000000000L) {
            int var3 = this.m;
            this.Z();
            return var3;
         } else {
            return 0;
         }
      }

      public void Z() {
         this.m = 0;
         this.u = 0L;
      }

      public boolean Y() {
         return this.m != 0;
      }
   }

   public enum y {
      K,
      g,
      u,
      l,
      q,
      n,
      M,
      V,
      e,
      U;
   }
}
