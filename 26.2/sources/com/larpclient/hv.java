package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;

public final class hv {
   public static final int d = 39;
   public static final int F = 19;
   public static final int o = 9;
   public static final int R = 12;
   public static final int M = 14;
   public static final int P = 132;
   public static final String E = "Modules";
   public static final String l = "list";
   private static final Map<String, Float> Z = new HashMap<>();
   private static final Map<String, Integer> J = new HashMap<>();
   private static Object V;
   private static long B;
   private static hv.h s;
   private static List<k8.l> b;
   private static int n;

   private hv() {
   }

   public static void p() {
      Z.clear();
      J.clear();
      V = null;
      B = 0L;
      s = null;
      b = null;
   }

   public static hv.h n(List<k8.l> var0, boolean var1, int var2) {
      ArrayList var3 = new ArrayList();
      byte var4 = 0;
      if (var1) {
         for (k8.l var6 : var0) {
            var3.add(new hv.w(var6.name(), var6.width(), 1.0F));
         }
      } else {
         ClientLevel var16 = Minecraft.getInstance().level;
         if (V != var16) {
            p();
            V = var16;
         }

         long var18 = System.nanoTime();
         double var8 = B == 0L ? 0.016666666666666666 : Math.max(0L, var18 - B) / 1.0E9;
         B = var18;
         if (s != null && var0 == b && var2 == n) {
            return s;
         }

         HashSet var10 = new HashSet();

         for (k8.l var12 : var0) {
            var10.add(var12.name());
            J.put(var12.name(), var12.width());
         }

         Iterator var19 = Z.entrySet().iterator();

         while (var19.hasNext()) {
            Entry var20 = (Entry)var19.next();
            if (!var10.contains(var20.getKey())) {
               var4 = 1;
               float var13 = gw.q((Float)var20.getValue(), false, var8);
               if (var13 <= 0.05F) {
                  var19.remove();
                  J.remove(var20.getKey());
               } else {
                  var20.setValue(var13);
               }
            }
         }

         for (String var23 : var10) {
            float var14 = Z.getOrDefault(var23, 0.0F);
            float var15 = gw.q(var14, true, var8);
            var4 |= var14 != var15 ? 1 : 0;
            Z.put(var23, var15);
         }

         for (Entry var24 : Z.entrySet()) {
            var3.add(new hv.w((String)var24.getKey(), J.getOrDefault(var24.getKey(), 0), (Float)var24.getValue()));
         }
      }

      var3.sort(Comparator.comparing(hv.w::name, String.CASE_INSENSITIVE_ORDER).thenComparing(hv.w::name));
      hv.h var17 = m(var3, var0.size(), var2, L(var0.size()));
      if (!var1) {
         s = var4 != 0 ? null : var17;
         b = var0;
         n = var2;
      }

      return var17;
   }

   public static int L(int var0) {
      return 12 + gw.S("Modules") + 12 + gw.S(Integer.toString(var0)) + 6 + 14 + 12;
   }

   public static hv.h m(List<hv.w> var0, int var1, int var2, int var3) {
      int var4 = Math.max(1, var2);
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var0.size(); var6 += var4) {
         var5.add(List.copyOf(var0.subList(var6, Math.min(var0.size(), var6 + var4))));
      }

      int var12 = var0.stream().mapToInt(hv.w::width).max().orElse(0);
      int var7 = Math.max(132, Math.max(var3, 24 + var5.size() * var12 + Math.max(0, var5.size() - 1) * 14));
      double var8 = 0.0;

      for (List var11 : var5) {
         var8 = Math.max(var8, var11.stream().mapToDouble(var0x -> 19.0F * var0x.alpha()).sum());
      }

      return new hv.h(List.copyOf(var5), var1, var12, var7, (int)Math.ceil(39.0 + var8 + 9.0));
   }

   public static int A(int var0, double var1) {
      return Math.max(1, (int)((var0 - 24) / Math.max(0.1, var1) - 39.0 - 9.0) / 19);
   }

   public static void j(GuiGraphicsExtractor var0, hv.h var1, int var2, int var3) {
      cl.y(var0, var2, var3, var1.width(), var1.height(), "Modules", "list", true);
      String var4 = Integer.toString(var1.count());
      gw.d(var0, var4, var2 + var1.width() - 26 - 6 - gw.S(var4), c2.X(var3, 32.0, 13.0), -7697772);
      int var5 = var1.columns().size() == 1 ? var1.width() - 24 : var1.columnWidth();

      for (int var6 = 0; var6 < var1.columns().size(); var6++) {
         int var7 = var2 + 12 + var6 * (var1.columnWidth() + 14);
         double var8 = var3 + 39;

         for (hv.w var11 : var1.columns().get(var6)) {
            double var12 = 19.0F * var11.alpha();
            int var14 = (int)Math.floor(var8);
            int var15 = (int)Math.ceil(var8 + var12);
            var0.enableScissor(var7, var14, var7 + var5, var15);

            try {
               gw.d(var0, gw.J(var11.name(), var5, 13.0), var7, c2.X(var8, var12, 13.0), uh.F(-1250068, var11.alpha()));
            } finally {
               var0.disableScissor();
            }

            var8 += var12;
         }
      }
   }

   public record h(List<List<hv.w>> columns, int count, int columnWidth, int width, int height) {
      public boolean empty() {
         return this.columns.isEmpty();
      }
   }

   public record w(String name, int width, float alpha) {
   }
}
