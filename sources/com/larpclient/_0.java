package com.larpclient;

public final class _0 {
   public static _0.f e(int var0, boolean var1, int var2, boolean var3, boolean var4, int var5, boolean var6) {
      if (var0 < 0 || !var1 || var2 == 40) {
         return null;
      } else if (!var3) {
         return new _0.f(var0, 40);
      } else if (var4 && var5 >= 1 && var5 <= 9 && !var6) {
         int var7 = var5 - 1;
         return var2 == var7 ? null : new _0.f(var0, var7);
      } else {
         return null;
      }
   }

   public static boolean j(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return !var0 && !var1 && !var2 && !var3 && !var4 && !var5 && !var6;
   }

   private _0() {
   }

   public record f(int menuSlot, int button) {
   }
}
