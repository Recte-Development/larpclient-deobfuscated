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
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_5223;
import net.minecraft.class_5250;
import net.minecraft.class_5348;

public final class er {
   private static Map<String, String> a = Map.of();
   private static Object B;
   private static long i;
   private static final ReferenceQueue<class_5348> P = new ReferenceQueue<>();
   private static final Set<er.h> s = new HashSet<>();

   public static long M() {
      return i;
   }

   public static void m() {
      class_310 var0 = class_310.method_1551();
      HashMap var1 = new HashMap();
      if (var0.field_1687 != null && he.k()) {
         String var2 = he.x(var0.method_1548().method_44717());
         J(var1, var0.method_1548().method_1676(), var2);
         if (var0.field_1724 != null) {
            J(var1, var0.field_1724.method_7334().name(), var2);
         }
      }

      I(var0.field_1687, var1);

      for (Reference var3 = P.poll(); var3 != null; var3 = P.poll()) {
         s.remove(var3);
      }
   }

   static void I(Object var0, Map<String, String> var1) {
      if (B != var0 || !var1.equals(a)) {
         B = var0;
         a = Map.copyOf(var1);
         i++;
         s.clear();
         q8.v("Appearance", "Display aliases refreshed: {} participants; chat will reflow", a.size());
      }
   }

   private static void J(Map<String, String> var0, String var1, String var2) {
      if (!var2.isEmpty() && !var1.equals(var2)) {
         var0.put(var1.toLowerCase(Locale.ROOT), var2);
      }
   }

   public static class_2561 P(class_2561 var0) {
      return (class_2561)W(var0);
   }

   public static class_5348 W(class_5348 var0) {
      return !a.isEmpty() && he.k() && class_310.method_1551().field_1687 != null ? C(var0) : var0;
   }

   static class_5348 C(class_5348 var0) {
      if (s.contains(new er.h(var0, null))) {
         return var0;
      }

      class_2561 var1 = N(var0, a);
      if (var1 != var0) {
         s.add(new er.h(var1, P));
      }

      return var1;
   }

   public static class_2561 J(UUID var0, String var1, class_2561 var2) {
      String var3 = he.x(var0);
      if (var3.isEmpty()) {
         return var2;
      }

      class_2561 var4 = N(var2, Map.of(var1.toLowerCase(Locale.ROOT), var3));
      if (var4 != var2) {
         s.add(new er.h(var4, P));
      }

      return var4;
   }

   public static class_2561 N(class_5348 var0, Map<String, String> var1) {
      if (var1.isEmpty()) {
         return K(var0);
      }

      String var2 = var0.getString();
      List var3 = null;
      if (var2.indexOf(167) >= 0) {
         var3 = J(var0);
         StringBuilder var4 = new StringBuilder();

         for (er.w var6 : var3) {
            var4.append(var6.text());
         }

         var2 = var4.toString();
      }

      ArrayList var12 = new ArrayList();
      int var13 = 0;

      while (var13 < var2.length()) {
         if (!d(var2.charAt(var13))) {
            var13++;
         } else {
            int var15 = var13;

            while (var13 < var2.length() && d(var2.charAt(var13))) {
               var13++;
            }

            String var7 = (String)var1.get(var2.substring(var15, var13).toLowerCase(Locale.ROOT));
            if (var7 != null && !var7.equals(var2.substring(var15, var13))) {
               var12.add(new er.e(var15, var13, var7));
            }
         }
      }

      if (var12.isEmpty()) {
         return K(var0);
      }

      if (var3 == null) {
         var3 = J(var0);
      }

      class_5250 var14 = class_2561.method_43473();
      int var16 = 0;

      for (er.e var8 : var12) {
         a(var14, var3, var16, var8.start());
         class_2583 var9 = class_2583.field_24360;

         for (er.w var11 : var3) {
            if (var11.start() <= var8.start() && var11.end() > var8.start()) {
               var9 = var11.style();
               break;
            }
         }

         var14.method_10852(class_2561.method_43470(var8.name()).method_10862(var9));
         var16 = var8.end();
      }

      a(var14, var3, var16, var2.length());
      return var14;
   }

   private static List<er.w> J(class_5348 var0) {
      ArrayList var1 = new ArrayList();
      int[] var2 = new int[]{0};
      var0.method_27658((var2x, var3) -> {
         if (var3.indexOf(167) < 0) {
            var1.add(new er.w(var2[0], var2[0] + var3.length(), var2x, var3));
            var2[0] += var3.length();
         } else {
            StringBuilder var4 = new StringBuilder();
            class_2583[] var5 = new class_2583[]{var2x};
            class_5223.method_27479(var3, var2x, (var4x, var5x, var6) -> {
               if (!var5x.equals(var5[0])) {
                  J(var1, var2, var5[0], var4);
                  var5[0] = var5x;
               }

               var4.appendCodePoint(var6);
               return true;
            });
            J(var1, var2, var5[0], var4);
         }

         return Optional.empty();
      }, class_2583.field_24360);
      return var1;
   }

   private static void J(List<er.w> var0, int[] var1, class_2583 var2, StringBuilder var3) {
      if (!var3.isEmpty()) {
         String var4 = var3.toString();
         var0.add(new er.w(var1[0], var1[0] + var4.length(), var2, var4));
         var1[0] += var4.length();
         var3.setLength(0);
      }
   }

   private static class_2561 K(class_5348 var0) {
      if (var0 instanceof class_2561 var2) {
         return var2;
      } else {
         class_5250 var1 = class_2561.method_43473();
         var0.method_27658((var1x, var2x) -> {
            var1.method_10852(class_2561.method_43470(var2x).method_10862(var1x));
            return Optional.empty();
         }, class_2583.field_24360);
         return var1;
      }
   }

   private static void a(class_5250 var0, List<er.w> var1, int var2, int var3) {
      for (er.w var5 : var1) {
         int var6 = Math.max(var2, var5.start());
         int var7 = Math.min(var3, var5.end());
         if (var6 < var7) {
            var0.method_10852(class_2561.method_43470(var5.text().substring(var6 - var5.start(), var7 - var5.start())).method_10862(var5.style()));
         }
      }
   }

   private static boolean d(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z' || var0 >= '0' && var0 <= '9' || var0 == '_';
   }

   private record e(int start, int end, String name) {
   }

   private static final class h extends WeakReference<class_5348> {
      private final int B;

      h(class_5348 var1, ReferenceQueue<class_5348> var2) {
         super(var1, var2);
         this.B = System.identityHashCode(var1);
      }

      @Override
      public int hashCode() {
         return this.B;
      }

      @Override
      public boolean equals(Object var1) {
         return this == var1 || var1 instanceof er.h var2 && this.get() != null && this.get() == var2.get();
      }
   }

   private record w(int start, int end, class_2583 style, String text) {
   }
}
