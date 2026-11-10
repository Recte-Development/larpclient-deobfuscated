package com.larpclient;

import java.util.List;
import java.util.Locale;

public final class th {
   public static final int P = 5;
   public static final int y = 4;
   public static final int k = 1000;

   public static int S(boolean var0) {
      return var0 ? 4 : 0;
   }

   public static int R(int var0, float var1, float var2, int var3) {
      if (!(var2 <= 0.0F) && var1 > 0.0F) {
         for (int var4 = 0; var4 < 1000; var4++) {
            if (!((var0 + var4 + var3) / var1 < var2)) {
               return var4;
            }
         }

         return 1000;
      } else {
         return 0;
      }
   }

   public static boolean A(th.b var0, boolean var1, th.j var2) {
      return !var1 && !var0.sameItem() && var2 == th.j.E;
   }

   public static int U(th.b var0, boolean var1, th.j var2, int var3, float var4, boolean var5) {
      return !A(var0, var1, var2) ? R(var3, var4, var0.minimum(), S(var5)) : 1 + R(0, var0.delay(), var0.minimum(), var5 ? 5 : 0);
   }

   public static int k(float var0, float var1) {
      return R(0, var0, var1, 5);
   }

   public static th.o T(List<th.b> var0, int var1, th.j var2, int var3, float var4, boolean var5) {
      th.b var6 = null;
      int var7 = 1000;

      for (th.b var9 : var0) {
         int var10 = var9.slot() == var1 ? 1 : 0;
         int var11 = U(var9, (boolean)var10, var2, var3, var4, var5);
         if (var6 == null || a(var9, (boolean)var10, var11, var6, var6.slot() == var1, var7)) {
            var6 = var9;
            var7 = var11;
         }
      }

      return var6 == null ? null : new th.o(var6.slot(), var7, var6.slot() != var1, var6.lunge(), A(var6, var6.slot() == var1, var2));
   }

   private static boolean a(th.b var0, boolean var1, int var2, th.b var3, boolean var4, int var5) {
      if (var2 != var5) {
         return var2 < var5;
      } else if (var1 != var4) {
         return var1;
      } else if (var0.breaks() != var3.breaks()) {
         return !var0.breaks();
      } else if (var0.lunge() != var3.lunge()) {
         return var0.lunge() > var3.lunge();
      } else {
         return var0.durability() != var3.durability() ? var0.durability() > var3.durability() : var0.slot() < var3.slot();
      }
   }

   public static String P(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      if (var3) {
         return "Lunge does not work while riding";
      } else if (var4) {
         return "Lunge does not work while gliding";
      } else if (var2) {
         return "Lunge does not work in water";
      } else {
         return !var1 && var0 < 7 ? "Lunge needs 7 food (you have " + var0 + ")" : null;
      }
   }

   public static String L(int var0) {
      return String.format(Locale.ROOT, "%.2f s", Math.max(0, var0) / 20.0);
   }

   private th() {
   }

   public record b(int slot, float delay, float minimum, int lunge, int durability, boolean breaks, boolean sameItem) {
   }

   public enum j {
      A,
      E;
   }

   public record o(int slot, int ticks, boolean swap, int lunge, boolean resets) {
   }
}
