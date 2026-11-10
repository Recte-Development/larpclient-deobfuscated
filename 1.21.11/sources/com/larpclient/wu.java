package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_310;

public final class wu {
   private static class_1703 T;
   private static Object d;
   private static Object f;
   private static List<wu.l> j = List.of();
   private static List<class_1799> x = List.of();
   private static class_1799 P = class_1799.field_8037;
   private static class_1799 e = class_1799.field_8037;
   private static Runnable V;
   private static Runnable n;
   private static String q;
   private static int H;
   private static int l;
   private static boolean M;

   private wu() {
   }

   public static boolean e() {
      return T != null;
   }

   public static boolean C() {
      return M;
   }

   public static void q(Runnable var0) {
      if (!e() || M) {
         boolean var1 = M;
         M = (boolean)1;

         try {
            var0.run();
         } finally {
            M = var1;
         }
      }
   }

   public static int X(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<wu.l> F(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new wu.l(X(var0), 0, class_1713.field_7790));
         if (var2 == var3) {
            var4.add(new wu.l(X(var1), 0, class_1713.field_7790));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new wu.l(X(var1), 1, class_1713.field_7790));
            }

            var4.add(new wu.l(X(var0), 0, class_1713.field_7790));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean D(String var0, List<wu.l> var1, Runnable var2, Runnable var3) {
      class_310 var4 = class_310.method_1551();
      return var4.field_1724 != null && o(var4.field_1724.field_7498, var0, var1, var2, var3);
   }

   public static boolean o(class_1703 var0, String var1, List<wu.l> var2, Runnable var3, Runnable var4) {
      return x(var0, var1, var2, class_1799.field_8037, var3, var4);
   }

   public static boolean x(class_1703 var0, String var1, List<wu.l> var2, class_1799 var3, Runnable var4, Runnable var5) {
      class_310 var6 = class_310.method_1551();
      if (!e()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.field_1724 != null
         && var6.field_1761 != null
         && var6.field_1724.field_7512 == var0
         && var0.method_34255().method_7960()) {
         T = var0;
         d = var6.field_1724;
         f = var6.field_1687;
         j = List.copyOf(var2);
         l = 0;
         H = 0;
         V = var4;
         n = var5;
         q = var1;
         e = var3.method_7972();
         M();
         q8.v("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), T.field_7763);
         return true;
      } else {
         return false;
      }
   }

   private static void M() {
      x = T.field_7761.stream().map(var0 -> var0.method_7677().method_7972()).toList();
      P = T.method_34255().method_7972();
   }

   static boolean M(class_1703 var0, List<class_1799> var1, class_1799 var2) {
      if (var0.field_7761.size() == var1.size() && class_1799.method_7973(var0.method_34255(), var2)) {
         for (int var3 = 0; var3 < var1.size(); var3++) {
            if (!class_1799.method_7973(var0.method_7611(var3).method_7677(), (class_1799)var1.get(var3))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static void n() {
      if (e() && !M(T, x, P)) {
         k("server inventory correction");
      }

      iy.V();
   }

   public static void d() {
      if (e()) {
         class_310 var0 = class_310.method_1551();
         if (var0.field_1724 == d && var0.field_1687 == f && var0.field_1724.field_7512 == T && var0.field_1724.method_5805() && cz.T().J()) {
            if (++l <= 200 && M(T, x, P)) {
               if (H == j.size()) {
                  if (!class_1799.method_7973(T.method_34255(), e)) {
                     k("unexpected cursor at completion");
                  } else {
                     Runnable var6 = V;
                     q8.v("FakeSpawner", "Transaction predicted complete: {} clicks={}", q, H);
                     w();
                     if (var6 != null) {
                        var6.run();
                     }

                     iy.V();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && H < j.size(); var1++) {
                     wu.l var2 = j.get(H++);
                     M = (boolean)1;

                     try {
                        var0.field_1761.method_2906(T.field_7763, var2.slot, var2.button, var2.type, var0.field_1724);
                     } finally {
                        M = (boolean)0;
                     }

                     M();
                     iy.V();
                  }
               }
            } else {
               k(l > 200 ? "timeout" : "inventory changed");
            }
         } else {
            k("owner or container changed");
         }
      }
   }

   public static void k(String var0) {
      if (e()) {
         class_310 var1 = class_310.method_1551();
         Runnable var2 = n;
         int var3 = var1.field_1724 == d && var1.field_1687 == f && var1.field_1724.field_7512 == T ? 1 : 0;
         q8.v("FakeSpawner", "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}", q, H, j.size(), var0, Boolean.valueOf((boolean)var3));
         w();
         if (var3 != 0) {
            var1.field_1724.method_7346();
         }

         if (var2 != null) {
            var2.run();
         }

         iy.V();
      }
   }

   private static void w() {
      T = null;
      f = null;
      d = null;
      j = List.of();
      x = List.of();
      P = e = class_1799.field_8037;
      n = null;
      V = null;
      q = null;
      l = 0;
      H = 0;
   }

   public record l(int slot, int button, class_1713 type) {
   }
}
