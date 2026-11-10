package com.larpclient;

import java.util.random.RandomGenerator;

public final class w4 {
   private w4.l h = w4.l.C;
   private int M;
   private int x = 1;

   public w4.l F() {
      return this.h;
   }

   public int l() {
      return this.M;
   }

   public boolean P() {
      return this.M >= this.x;
   }

   public boolean C() {
      return this.M >= this.x + 20;
   }

   public void b() {
      if (this.M < Integer.MAX_VALUE) {
         this.M++;
      }
   }

   public void C(w4.l var1, int var2) {
      this.h = var1;
      this.x = Math.max(0, var2);
      this.M = 0;
   }

   public void k() {
      this.C(w4.l.C, 1);
   }

   public static int k(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int Y(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean t(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean A(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean a(String var0) {
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

   public enum l {
      C,
      a,
      L,
      p,
      D,
      S,
      d,
      T,
      P,
      J;
   }

   public static final class q {
      private long B;
      private int Y;

      public void B(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.Y = var1;
            this.B = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int s(long var1) {
         if (this.Y != 0 && var1 - this.B >= 1000000000L) {
            int var3 = this.Y;
            this.u();
            return var3;
         } else {
            return 0;
         }
      }

      public void u() {
         this.Y = 0;
         this.B = 0L;
      }

      public boolean Y() {
         return this.Y != 0;
      }
   }
}
