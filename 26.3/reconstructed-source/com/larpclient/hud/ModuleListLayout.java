package com.larpclient.hud;

import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.data.ModuleListCache;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ModuleListLayout {
   public static final int hEADER_HEIGHT = 39;
   public static final int rOW_HEIGHT = 19;
   public static final int bOTTOM_PADDING = 9;
   public static final int pADDING = 12;
   public static final int cOLUMN_GAP = 14;
   public static final int mIN_WIDTH = 132;
   public static final String tITLE = "Modules";
   public static final String sUBTITLE = "list";
   private static final Map<String, Float> alphas = new HashMap<>();
   private static final Map<String, Integer> widths = new HashMap<>();
   private static Object lastLevel;
   private static long lastNanos;
   private static ModuleListLayout.Layout cachedLayout;
   private static List<ModuleListCache.ModuleEntry> cachedModules;
   private static int cachedRows;

   private ModuleListLayout() {
   }

   public static void reset() {
      alphas.clear();
      widths.clear();
      lastLevel = null;
      lastNanos = 0L;
      cachedLayout = null;
      cachedModules = null;
   }

   public static ModuleListLayout.Layout layout(List<ModuleListCache.ModuleEntry> var0, boolean var1, int var2) {
      ArrayList var3 = new ArrayList();
      byte var4 = 0;
      if (var1) {
         for (ModuleListCache.ModuleEntry var6 : var0) {
            var3.add(new ModuleListLayout.Entry(var6.name(), var6.width(), 1.0F));
         }
      } else {
         ClientLevel var16 = Minecraft.getInstance().level;
         if (lastLevel != var16) {
            reset();
            lastLevel = var16;
         }

         long var18 = System.nanoTime();
         double var8 = lastNanos == 0L ? 0.016666666666666666 : Math.max(0L, var18 - lastNanos) / 1.0E9;
         lastNanos = var18;
         if (cachedLayout != null && var0 == cachedModules && var2 == cachedRows) {
            return cachedLayout;
         }

         HashSet var10 = new HashSet();

         for (ModuleListCache.ModuleEntry var12 : var0) {
            var10.add(var12.name());
            widths.put(var12.name(), var12.width());
         }

         Iterator var19 = alphas.entrySet().iterator();

         while (var19.hasNext()) {
            Map.Entry var20 = (Map.Entry)var19.next();
            if (!var10.contains(var20.getKey())) {
               var4 = 1;
               float var13 = HudPanels.smoothByDelta((Float)var20.getValue(), false, var8);
               if (var13 <= 0.05F) {
                  var19.remove();
                  widths.remove(var20.getKey());
               } else {
                  var20.setValue(var13);
               }
            }
         }

         for (String var23 : var10) {
            float var14 = alphas.getOrDefault(var23, 0.0F);
            float var15 = HudPanels.smoothByDelta(var14, true, var8);
            var4 |= var14 != var15 ? 1 : 0;
            alphas.put(var23, var15);
         }

         for (Map.Entry var24 : alphas.entrySet()) {
            var3.add(new ModuleListLayout.Entry((String)var24.getKey(), widths.getOrDefault(var24.getKey(), 0), (Float)var24.getValue()));
         }
      }

      var3.sort(Comparator.comparing(ModuleListLayout.Entry::name, String.CASE_INSENSITIVE_ORDER).thenComparing(ModuleListLayout.Entry::name));
      ModuleListLayout.Layout var17 = buildLayout(var3, var0.size(), var2, minWidth(var0.size()));
      if (!var1) {
         cachedLayout = var4 != 0 ? null : var17;
         cachedModules = var0;
         cachedRows = var2;
      }

      return var17;
   }

   public static int minWidth(int var0) {
      return 12 + HudPanels.textWidth("Modules") + 12 + HudPanels.textWidth(Integer.toString(var0)) + 6 + 14 + 12;
   }

   public static ModuleListLayout.Layout buildLayout(List<ModuleListLayout.Entry> var0, int var1, int var2, int var3) {
      int var4 = Math.max(1, var2);
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var0.size(); var6 += var4) {
         var5.add(List.copyOf(var0.subList(var6, Math.min(var0.size(), var6 + var4))));
      }

      int var12 = var0.stream().mapToInt(ModuleListLayout.Entry::width).max().orElse(0);
      int var7 = Math.max(132, Math.max(var3, 24 + var5.size() * var12 + Math.max(0, var5.size() - 1) * 14));
      double var8 = 0.0;

      for (List var11 : var5) {
         var8 = Math.max(var8, var11.stream().mapToDouble(var0x -> 19.0F * var0x.alpha()).sum());
      }

      return new ModuleListLayout.Layout(List.copyOf(var5), var1, var12, var7, (int)Math.ceil(39.0 + var8 + 9.0));
   }

   public static int maxRows(int var0, double var1) {
      return Math.max(1, (int)((var0 - 24) / Math.max(0.1, var1) - 39.0 - 9.0) / 19);
   }

   public static void render(GuiGraphicsExtractor var0, ModuleListLayout.Layout var1, int var2, int var3) {
      HudPanels2.renderFrame(var0, var2, var3, var1.width(), var1.height(), "Modules", "list", true);
      String var4 = Integer.toString(var1.count());
      HudPanels.formatDuration(var0, var4, var2 + var1.width() - 26 - 6 - HudPanels.textWidth(var4), SdfText.centerBaselineY(var3, 32.0, 13.0), -7697772);
      int var5 = var1.columns().size() == 1 ? var1.width() - 24 : var1.columnWidth();

      for (int var6 = 0; var6 < var1.columns().size(); var6++) {
         int var7 = var2 + 12 + var6 * (var1.columnWidth() + 14);
         double var8 = var3 + 39;

         for (ModuleListLayout.Entry var11 : var1.columns().get(var6)) {
            double var12 = 19.0F * var11.alpha();
            int var14 = (int)Math.floor(var8);
            int var15 = (int)Math.ceil(var8 + var12);
            var0.enableScissor(var7, var14, var7 + var5, var15);

            try {
               HudPanels.formatDuration(
                  var0,
                  HudPanels.truncate(var11.name(), var5, 13.0),
                  var7,
                  SdfText.centerBaselineY(var8, var12, 13.0),
                  ColorUtil.multiplyAlpha(-1250068, var11.alpha())
               );
            } finally {
               var0.disableScissor();
            }

            var8 += var12;
         }
      }
   }

   public record Entry(String name, int width, float alpha) {
   }

   public record Layout(List<List<ModuleListLayout.Entry>> columns, int count, int columnWidth, int width, int height) {
      public boolean empty() {
         return this.columns.isEmpty();
      }
   }
}
