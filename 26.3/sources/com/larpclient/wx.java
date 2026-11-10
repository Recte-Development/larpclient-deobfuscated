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

public final class wx {
   public static final int Z = 39;
   public static final int O = 19;
   public static final int K = 9;
   public static final int I = 12;
   public static final int f = 14;
   public static final int k = 132;
   public static final String g = "Modules";
   public static final String i = "list";
   private static final Map<String, Float> s = new HashMap<>();
   private static final Map<String, Integer> N = new HashMap<>();
   private static Object h;
   private static long y;
   private static wx.z A;
   private static List<n7.p> d;
   private static int c;

   private wx() {
   }

   public static void j() {
      s.clear();
      N.clear();
      h = null;
      y = 0L;
      A = null;
      d = null;
   }

   public static wx.z b(List<n7.p> var0, boolean var1, int var2) {
      ArrayList var3 = new ArrayList();
      byte var4 = 0;
      if (var1) {
         for (n7.p var6 : var0) {
            var3.add(new wx.x(var6.name(), var6.width(), 1.0F));
         }
      } else {
         ClientLevel var16 = Minecraft.getInstance().level;
         if (h != var16) {
            j();
            h = var16;
         }

         long var18 = System.nanoTime();
         double var8 = y == 0L ? 0.016666666666666666 : Math.max(0L, var18 - y) / 1.0E9;
         y = var18;
         if (A != null && var0 == d && var2 == c) {
            return A;
         }

         HashSet var10 = new HashSet();

         for (n7.p var12 : var0) {
            var10.add(var12.name());
            N.put(var12.name(), var12.width());
         }

         Iterator var19 = s.entrySet().iterator();

         while (var19.hasNext()) {
            Entry var20 = (Entry)var19.next();
            if (!var10.contains(var20.getKey())) {
               var4 = 1;
               float var13 = i5.U((Float)var20.getValue(), false, var8);
               if (var13 <= 0.05F) {
                  var19.remove();
                  N.remove(var20.getKey());
               } else {
                  var20.setValue(var13);
               }
            }
         }

         for (String var23 : var10) {
            float var14 = s.getOrDefault(var23, 0.0F);
            float var15 = i5.U(var14, true, var8);
            var4 |= var14 != var15 ? 1 : 0;
            s.put(var23, var15);
         }

         for (Entry var24 : s.entrySet()) {
            var3.add(new wx.x((String)var24.getKey(), N.getOrDefault(var24.getKey(), 0), (Float)var24.getValue()));
         }
      }

      var3.sort(Comparator.comparing(wx.x::name, String.CASE_INSENSITIVE_ORDER).thenComparing(wx.x::name));
      wx.z var17 = Y(var3, var0.size(), var2, Z(var0.size()));
      if (!var1) {
         A = var4 != 0 ? null : var17;
         d = var0;
         c = var2;
      }

      return var17;
   }

   public static int Z(int var0) {
      return 12 + i5.k("Modules") + 12 + i5.k(Integer.toString(var0)) + 6 + 14 + 12;
   }

   public static wx.z Y(List<wx.x> var0, int var1, int var2, int var3) {
      int var4 = Math.max(1, var2);
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var0.size(); var6 += var4) {
         var5.add(List.copyOf(var0.subList(var6, Math.min(var0.size(), var6 + var4))));
      }

      int var12 = var0.stream().mapToInt(wx.x::width).max().orElse(0);
      int var7 = Math.max(132, Math.max(var3, 24 + var5.size() * var12 + Math.max(0, var5.size() - 1) * 14));
      double var8 = 0.0;

      for (List var11 : var5) {
         var8 = Math.max(var8, var11.stream().mapToDouble(var0x -> 19.0F * var0x.alpha()).sum());
      }

      return new wx.z(List.copyOf(var5), var1, var12, var7, (int)Math.ceil(39.0 + var8 + 9.0));
   }

   public static int s(int var0, double var1) {
      return Math.max(1, (int)((var0 - 24) / Math.max(0.1, var1) - 39.0 - 9.0) / 19);
   }

   public static void B(GuiGraphicsExtractor var0, wx.z var1, int var2, int var3) {
      nv.J(var0, var2, var3, var1.width(), var1.height(), "Modules", "list", true);
      String var4 = Integer.toString(var1.count());
      i5.j(var0, var4, var2 + var1.width() - 26 - 6 - i5.k(var4), n8.b(var3, 32.0, 13.0), -7697772);
      int var5 = var1.columns().size() == 1 ? var1.width() - 24 : var1.columnWidth();

      for (int var6 = 0; var6 < var1.columns().size(); var6++) {
         int var7 = var2 + 12 + var6 * (var1.columnWidth() + 14);
         double var8 = var3 + 39;

         for (wx.x var11 : var1.columns().get(var6)) {
            double var12 = 19.0F * var11.alpha();
            int var14 = (int)Math.floor(var8);
            int var15 = (int)Math.ceil(var8 + var12);
            var0.enableScissor(var7, var14, var7 + var5, var15);

            try {
               i5.j(var0, i5.I(var11.name(), var5, 13.0), var7, n8.b(var8, var12, 13.0), vv.O(-1250068, var11.alpha()));
            } finally {
               var0.disableScissor();
            }

            var8 += var12;
         }
      }
   }

   public record x(String name, int width, float alpha) {
   }

   public record z(List<List<wx.x>> columns, int count, int columnWidth, int width, int height) {
      public boolean empty() {
         return this.columns.isEmpty();
      }
   }
}
