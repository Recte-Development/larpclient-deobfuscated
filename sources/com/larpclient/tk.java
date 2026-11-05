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

public final class tk {
   public static final int f = 39;
   public static final int n = 19;
   public static final int U = 9;
   public static final int o = 12;
   public static final int H = 14;
   public static final int v = 132;
   public static final String P = "Modules";
   public static final String W = "list";
   private static final Map<String, Float> t = new HashMap<>();
   private static final Map<String, Integer> K = new HashMap<>();
   private static Object J;
   private static long O;

   private tk() {
   }

   public static void v() {
      t.clear();
      K.clear();
      J = null;
      O = 0L;
   }

   public static tk.z J(List<wo.i> var0, boolean var1, int var2) {
      ArrayList var3 = new ArrayList();
      if (var1) {
         for (wo.i var5 : var0) {
            var3.add(new tk.j(var5.name(), var5.width(), 1.0F));
         }
      } else {
         ClientLevel var13 = Minecraft.getInstance().level;
         if (J != var13) {
            v();
            J = var13;
         }

         long var14 = System.nanoTime();
         double var7 = O == 0L ? 0.016666666666666666 : Math.max(0L, var14 - O) / 1.0E9;
         O = var14;
         HashSet var9 = new HashSet();

         for (wo.i var11 : var0) {
            var9.add(var11.name());
            K.put(var11.name(), var11.width());
         }

         Iterator var15 = t.entrySet().iterator();

         while (var15.hasNext()) {
            Entry var16 = (Entry)var15.next();
            if (!var9.contains(var16.getKey())) {
               float var12 = we.P((Float)var16.getValue(), false, var7);
               if (var12 <= 0.05F) {
                  var15.remove();
                  K.remove(var16.getKey());
               } else {
                  var16.setValue(var12);
               }
            }
         }

         for (String var19 : var9) {
            t.put(var19, we.P(t.getOrDefault(var19, 0.0F), true, var7));
         }

         for (Entry var20 : t.entrySet()) {
            var3.add(new tk.j((String)var20.getKey(), K.getOrDefault(var20.getKey(), 0), (Float)var20.getValue()));
         }
      }

      var3.sort(Comparator.comparing(tk.j::name, String.CASE_INSENSITIVE_ORDER).thenComparing(tk.j::name));
      return D(var3, var0.size(), var2, L(var0.size()));
   }

   public static int L(int var0) {
      return 12 + we.L("Modules") + 12 + we.L(Integer.toString(var0)) + 6 + 14 + 12;
   }

   public static tk.z D(List<tk.j> var0, int var1, int var2, int var3) {
      int var4 = Math.max(1, var2);
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var0.size(); var6 += var4) {
         var5.add(List.copyOf(var0.subList(var6, Math.min(var0.size(), var6 + var4))));
      }

      int var12 = var0.stream().mapToInt(tk.j::width).max().orElse(0);
      int var7 = Math.max(132, Math.max(var3, 24 + var5.size() * var12 + Math.max(0, var5.size() - 1) * 14));
      double var8 = 0.0;

      for (List var11 : var5) {
         var8 = Math.max(var8, var11.stream().mapToDouble(var0x -> 19.0F * var0x.alpha()).sum());
      }

      return new tk.z(List.copyOf(var5), var1, var12, var7, (int)Math.ceil(39.0 + var8 + 9.0));
   }

   public static int u(int var0, double var1) {
      return Math.max(1, (int)((var0 - 24) / Math.max(0.1, var1) - 39.0 - 9.0) / 19);
   }

   public static void L(GuiGraphicsExtractor var0, tk.z var1, int var2, int var3) {
      e0.l(var0, var2, var3, var1.width(), var1.height(), "Modules", "list", true);
      String var4 = Integer.toString(var1.count());
      we.W(var0, var4, var2 + var1.width() - 26 - 6 - we.L(var4), ew.N(var3, 32.0, 13.0), -7697772);
      int var5 = var1.columns().size() == 1 ? var1.width() - 24 : var1.columnWidth();

      for (int var6 = 0; var6 < var1.columns().size(); var6++) {
         int var7 = var2 + 12 + var6 * (var1.columnWidth() + 14);
         double var8 = var3 + 39;

         for (tk.j var11 : var1.columns().get(var6)) {
            double var12 = 19.0F * var11.alpha();
            int var14 = (int)Math.floor(var8);
            int var15 = (int)Math.ceil(var8 + var12);
            var0.enableScissor(var7, var14, var7 + var5, var15);

            try {
               we.W(var0, we.t(var11.name(), var5, 13.0), var7, ew.N(var8, var12, 13.0), _3.U(-1250068, var11.alpha()));
            } finally {
               var0.disableScissor();
            }

            var8 += var12;
         }
      }
   }

   public record j(String name, int width, float alpha) {
   }

   public record z(List<List<tk.j>> columns, int count, int columnWidth, int width, int height) {
      public boolean empty() {
         return this.columns.isEmpty();
      }
   }
}
