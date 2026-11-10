package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_638;

public final class ud {
   public static final int E = 39;
   public static final int p = 19;
   public static final int j = 9;
   public static final int s = 12;
   public static final int u = 14;
   public static final int c = 132;
   public static final String e = "Modules";
   public static final String y = "list";
   private static final Map<String, Float> h = new HashMap<>();
   private static final Map<String, Integer> U = new HashMap<>();
   private static Object W;
   private static long F;
   private static ud.a O;
   private static List<up.d> X;
   private static int N;

   private ud() {
   }

   public static void V() {
      h.clear();
      U.clear();
      W = null;
      F = 0L;
      O = null;
      X = null;
   }

   public static ud.a i(List<up.d> var0, boolean var1, int var2) {
      ArrayList var3 = new ArrayList();
      byte var4 = 0;
      if (var1) {
         for (up.d var6 : var0) {
            var3.add(new ud.l(var6.name(), var6.width(), 1.0F));
         }
      } else {
         class_638 var16 = class_310.method_1551().field_1687;
         if (W != var16) {
            V();
            W = var16;
         }

         long var18 = System.nanoTime();
         double var8 = F == 0L ? 0.016666666666666666 : Math.max(0L, var18 - F) / 1.0E9;
         F = var18;
         if (O != null && var0 == X && var2 == N) {
            return O;
         }

         HashSet var10 = new HashSet();

         for (up.d var12 : var0) {
            var10.add(var12.name());
            U.put(var12.name(), var12.width());
         }

         Iterator var19 = h.entrySet().iterator();

         while (var19.hasNext()) {
            Entry var20 = (Entry)var19.next();
            if (!var10.contains(var20.getKey())) {
               var4 = 1;
               float var13 = w5.P((Float)var20.getValue(), false, var8);
               if (var13 <= 0.05F) {
                  var19.remove();
                  U.remove(var20.getKey());
               } else {
                  var20.setValue(var13);
               }
            }
         }

         for (String var23 : var10) {
            float var14 = h.getOrDefault(var23, 0.0F);
            float var15 = w5.P(var14, true, var8);
            var4 |= var14 != var15 ? 1 : 0;
            h.put(var23, var15);
         }

         for (Entry var24 : h.entrySet()) {
            var3.add(new ud.l((String)var24.getKey(), U.getOrDefault(var24.getKey(), 0), (Float)var24.getValue()));
         }
      }

      var3.sort(Comparator.comparing(ud.l::name, String.CASE_INSENSITIVE_ORDER).thenComparing(ud.l::name));
      ud.a var17 = D(var3, var0.size(), var2, h(var0.size()));
      if (!var1) {
         O = var4 != 0 ? null : var17;
         X = var0;
         N = var2;
      }

      return var17;
   }

   public static int h(int var0) {
      return 12 + w5.I("Modules") + 12 + w5.I(Integer.toString(var0)) + 6 + 14 + 12;
   }

   public static ud.a D(List<ud.l> var0, int var1, int var2, int var3) {
      int var4 = Math.max(1, var2);
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var0.size(); var6 += var4) {
         var5.add(List.copyOf(var0.subList(var6, Math.min(var0.size(), var6 + var4))));
      }

      int var12 = var0.stream().mapToInt(ud.l::width).max().orElse(0);
      int var7 = Math.max(132, Math.max(var3, 24 + var5.size() * var12 + Math.max(0, var5.size() - 1) * 14));
      double var8 = 0.0;

      for (List var11 : var5) {
         var8 = Math.max(var8, var11.stream().mapToDouble(var0x -> 19.0F * var0x.alpha()).sum());
      }

      return new ud.a(List.copyOf(var5), var1, var12, var7, (int)Math.ceil(39.0 + var8 + 9.0));
   }

   public static int Q(int var0, double var1) {
      return Math.max(1, (int)((var0 - 24) / Math.max(0.1, var1) - 39.0 - 9.0) / 19);
   }

   public static void c(class_332 var0, ud.a var1, int var2, int var3) {
      y4.R(var0, var2, var3, var1.width(), var1.height(), "Modules", "list", true);
      String var4 = Integer.toString(var1.count());
      w5.Y(var0, var4, var2 + var1.width() - 26 - 6 - w5.I(var4), q2.e(var3, 32.0, 13.0), -7697772);
      int var5 = var1.columns().size() == 1 ? var1.width() - 24 : var1.columnWidth();

      for (int var6 = 0; var6 < var1.columns().size(); var6++) {
         int var7 = var2 + 12 + var6 * (var1.columnWidth() + 14);
         double var8 = var3 + 39;

         for (ud.l var11 : var1.columns().get(var6)) {
            double var12 = 19.0F * var11.alpha();
            int var14 = (int)Math.floor(var8);
            int var15 = (int)Math.ceil(var8 + var12);
            var0.method_44379(var7, var14, var7 + var5, var15);

            try {
               w5.Y(var0, w5.O(var11.name(), var5, 13.0), var7, q2.e(var8, var12, 13.0), ei.R(-1250068, var11.alpha()));
            } finally {
               var0.method_44380();
            }

            var8 += var12;
         }
      }
   }

   public record a(List<List<ud.l>> columns, int count, int columnWidth, int width, int height) {
      public boolean empty() {
         return this.columns.isEmpty();
      }
   }

   public record l(String name, int width, float alpha) {
   }
}
