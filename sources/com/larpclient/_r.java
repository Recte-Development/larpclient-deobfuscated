package com.larpclient;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

public final class _r {
   private static Map<String, String> U = Map.of();
   private static Object M;
   private static long D;
   private static final ReferenceQueue<FormattedText> C = new ReferenceQueue<>();
   private static final Set<_r.g> k = new HashSet<>();

   public static long x() {
      return D;
   }

   public static void f() {
      Minecraft var0 = Minecraft.getInstance();
      HashMap var1 = new HashMap();
      if (var0.level != null && t7.f()) {
         String var2 = t7.f(var0.getUser().getProfileId());
         m(var1, var0.getUser().getName(), var2);
         if (var0.player != null) {
            m(var1, var0.player.getGameProfile().name(), var2);
         }
      }

      J(var0.level, var1);

      for (Reference var3 = C.poll(); var3 != null; var3 = C.poll()) {
         k.remove(var3);
      }
   }

   static void J(Object var0, Map<String, String> var1) {
      if (M != var0 || !var1.equals(U)) {
         M = var0;
         U = Map.copyOf(var1);
         D++;
         k.clear();
         mt.U("Appearance", "Display aliases refreshed: {} participants; chat will reflow", U.size());
      }
   }

   private static void m(Map<String, String> var0, String var1, String var2) {
      if (!var2.isEmpty() && !var1.equals(var2)) {
         var0.put(var1.toLowerCase(Locale.ROOT), var2);
      }
   }

   public static Component H(Component var0) {
      return (Component)t(var0);
   }

   public static FormattedText t(FormattedText var0) {
      return !U.isEmpty() && t7.f() && Minecraft.getInstance().level != null ? f(var0) : var0;
   }

   static FormattedText f(FormattedText var0) {
      if (k.contains(new _r.g(var0, null))) {
         return var0;
      }

      Component var1 = U(var0, U);
      if (var1 != var0) {
         k.add(new _r.g(var1, C));
      }

      return var1;
   }

   public static Component L(UUID var0, String var1, Component var2) {
      String var3 = t7.f(var0);
      if (var3.isEmpty()) {
         return var2;
      }

      Component var4 = U(var2, Map.of(var1.toLowerCase(Locale.ROOT), var3));
      if (var4 != var2) {
         k.add(new _r.g(var4, C));
      }

      return var4;
   }

   public static Component U(FormattedText var0, Map<String, String> var1) {
      if (var1.isEmpty()) {
         return y(var0);
      }

      String var2 = var0.getString();
      List var3 = null;
      if (var2.indexOf(167) >= 0) {
         var3 = t(var0);
         StringBuilder var4 = new StringBuilder();

         for (_r.b var6 : var3) {
            var4.append(var6.text());
         }

         var2 = var4.toString();
      }

      ArrayList var12 = new ArrayList();
      int var13 = 0;

      while (var13 < var2.length()) {
         if (!R(var2.charAt(var13))) {
            var13++;
         } else {
            int var15 = var13;

            while (var13 < var2.length() && R(var2.charAt(var13))) {
               var13++;
            }

            String var7 = (String)var1.get(var2.substring(var15, var13).toLowerCase(Locale.ROOT));
            if (var7 != null && !var7.equals(var2.substring(var15, var13))) {
               var12.add(new _r.o(var15, var13, var7));
            }
         }
      }

      if (var12.isEmpty()) {
         return y(var0);
      }

      if (var3 == null) {
         var3 = t(var0);
      }

      MutableComponent var14 = Component.empty();
      int var16 = 0;

      for (_r.o var8 : var12) {
         X(var14, var3, var16, var8.start());
         Style var9 = Style.EMPTY;

         for (_r.b var11 : var3) {
            if (var11.start() <= var8.start() && var11.end() > var8.start()) {
               var9 = var11.style();
               break;
            }
         }

         var14.append(Component.literal(var8.name()).setStyle(var9));
         var16 = var8.end();
      }

      X(var14, var3, var16, var2.length());
      return var14;
   }

   private static List<_r.b> t(FormattedText var0) {
      ArrayList var1 = new ArrayList();
      int[] var2 = new int[]{0};
      var0.visit((var2x, var3) -> {
         if (var3.indexOf(167) < 0) {
            var1.add(new _r.b(var2[0], var2[0] + var3.length(), var2x, var3));
            var2[0] += var3.length();
         } else {
            StringBuilder var4 = new StringBuilder();
            Style[] var5 = new Style[]{var2x};
            StringDecomposer.iterateFormatted(var3, var2x, (var4x, var5x, var6) -> {
               if (!var5x.equals(var5[0])) {
                  L(var1, var2, var5[0], var4);
                  var5[0] = var5x;
               }

               var4.appendCodePoint(var6);
               return true;
            });
            L(var1, var2, var5[0], var4);
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var1;
   }

   private static void L(List<_r.b> var0, int[] var1, Style var2, StringBuilder var3) {
      if (!var3.isEmpty()) {
         String var4 = var3.toString();
         var0.add(new _r.b(var1[0], var1[0] + var4.length(), var2, var4));
         var1[0] += var4.length();
         var3.setLength(0);
      }
   }

   private static Component y(FormattedText var0) {
      if (var0 instanceof Component var2) {
         return var2;
      } else {
         MutableComponent var1 = Component.empty();
         var0.visit((var1x, var2x) -> {
            var1.append(Component.literal(var2x).setStyle(var1x));
            return Optional.empty();
         }, Style.EMPTY);
         return var1;
      }
   }

   private static void X(MutableComponent var0, List<_r.b> var1, int var2, int var3) {
      for (_r.b var5 : var1) {
         int var6 = Math.max(var2, var5.start());
         int var7 = Math.min(var3, var5.end());
         if (var6 < var7) {
            var0.append(Component.literal(var5.text().substring(var6 - var5.start(), var7 - var5.start())).setStyle(var5.style()));
         }
      }
   }

   private static boolean R(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '_';
   }

   private record b(int start, int end, Style style, String text) {
   }

   private static final class g extends WeakReference<FormattedText> {
      private final int p;

      g(FormattedText var1, ReferenceQueue<FormattedText> var2) {
         super(var1, var2);
         this.p = System.identityHashCode(var1);
      }

      @Override
      public int hashCode() {
         return this.p;
      }

      @Override
      public boolean equals(Object var1) {
         return this == var1 || var1 instanceof _r.g var2 && this.get() != null && this.get() == var2.get();
      }
   }

   private record o(int start, int end, String name) {
   }
}
