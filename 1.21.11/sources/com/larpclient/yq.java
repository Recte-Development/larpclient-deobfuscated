package com.larpclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.class_1542;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;

public final class yq {
   public static final int e = 200;
   private static final DateTimeFormatter Y = DateTimeFormatter.ofPattern("HH:mm:ss");
   private static final Object T = new Object();
   private static final ArrayDeque<String> u = new ArrayDeque<>();
   private static final Map<class_1542, Boolean> r = new WeakHashMap<>();
   private static long F;

   private yq() {
   }

   public static void d(int var0, int var1, int var2) {
      X("CARD", var0, var1, var2);
   }

   public static void X(String var0, int var1, int var2, int var3) {
      x(var0, var1, var2, var3, false);
   }

   public static void x(String var0, int var1, int var2, int var3, boolean var4) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 0) {
         String var5 = W(var0);
         String var6 = R(k(var0, var1), k(var0, var2), true, var4);
         c("[" + LocalDateTime.now().format(Y) + "] [" + var5 + "] " + var6 + "  (map " + var3 + ")");
      }
   }

   public static void E(String var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         c("[" + LocalDateTime.now().format(Y) + "] [" + W(var0) + "] " + var1.replace('\r', ' ').replace('\n', ' '));
      }
   }

   public static String k(String var0, int var1) {
      return "BLACKJACK".equals(W(var0)) && var1 == 1 ? "Ace" : Integer.toString(var1);
   }

   public static String R(String var0, String var1, boolean var2, boolean var3) {
      if (!var2) {
         return "Unrigged - Real " + var0;
      } else if (var3 && !var0.equals(var1)) {
         return "[visual] " + (var0.equals("Ace") ? "A" : var0) + " -> " + (var1.equals("Ace") ? "A" : var1);
      } else {
         return var0.equals(var1) ? "Real " + var0 : "Real " + var0 + " -> " + (var3 ? "Spoofed " : "Rigged ") + var1;
      }
   }

   public static void M(class_310 var0, hu.q var1, String var2, class_1542 var3, String var4, String var5, boolean var6) {
      if (var0 != null && var0.field_1687 != null && var3.field_6012 <= 20 && !var3.method_31481()) {
         hu var7 = hu.f();
         if (!var7.N()) {
            class_2338 var8 = var7.F(var1);
            if (var8 != null) {
               class_2680 var9 = var0.field_1687.method_8320(var8);
               if (var9.method_26204() instanceof class_2315) {
                  class_2350 var10 = (class_2350)var9.method_11654(class_2315.field_10918);
                  class_243 var11 = class_243.method_24953(var8)
                     .method_1031(var10.method_10148() * 0.7, var10.method_10164() * 0.7, var10.method_10165() * 0.7);
                  if (!(var3.method_5707(var11) > 2.25) && r.put(var3, true) == null) {
                     E(var2, R(var4, var5, var6, true));
                  }
               }
            }
         }
      }
   }

   public static void R(String var0) {
      if (var0 != null && !var0.isBlank()) {
         q8.v("Rig", "{}", var0);
      }
   }

   public static List<String> q() {
      synchronized (T) {
         return new ArrayList<>(u);
      }
   }

   public static long H() {
      synchronized (T) {
         return F;
      }
   }

   public static void Q() {
      synchronized (T) {
         u.clear();
         F++;
      }
   }

   private static String W(String var0) {
      return var0 != null && !var0.isBlank() ? var0.trim().toUpperCase(Locale.ROOT).replace('_', ' ').replaceAll("[^A-Z0-9 /-]", "") : "GAME";
   }

   private static void c(String var0) {
      synchronized (T) {
         u.addLast(var0);

         while (u.size() > 200) {
            u.removeFirst();
         }

         F++;
      }

      q8.G("Rig", "{}", var0);
   }
}
