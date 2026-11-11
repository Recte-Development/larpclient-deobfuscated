package com.larpclient.hud;

import com.larpclient.LarpClient;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.CasinoGameModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleManager;
import com.larpclient.module.render.HUDModule;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public final class HudPanels {
   public static final int hEADER_HEIGHT = 32;
   public static final int pADDING = 12;
   public static final int fONT_SIZE = 13;
   public static final int bACKGROUND_COLOR = -267645934;
   private static final Map<Module, Float> keybindAlpha = new IdentityHashMap<>();
   private static final Map<Identifier, Float> effectAlpha = new HashMap<>();
   private static Object lastLevel;
   private static Object lastPlayer;
   private static long lastKeybindsNanos;
   private static long lastPotionsNanos;
   private static final Map<Identifier, HudPanels.Row> lastEffectRows = new HashMap<>();
   public static final int mIN_CARD_WIDTH = 196;
   public static final int mAX_CARD_WIDTH = 300;
   public static final int cARD_HEIGHT = 80;
   private static final int dANGER_COLOR = -41892;
   private static final int dANGER_TEXT_COLOR = -30070;
   static final float bORDER_WIDTH = 1.6F;
   static final float cORNER_RADIUS = 12.0F;
   private static final double sMALL_FONT_SIZE = 11.0;

   private HudPanels() {
   }

   public static void reset() {
      keybindAlpha.clear();
      effectAlpha.clear();
      lastEffectRows.clear();
      lastPlayer = null;
      lastLevel = null;
      lastPotionsNanos = 0L;
      lastKeybindsNanos = 0L;
   }

   private static void resetOnWorldChange() {
      Minecraft var0 = Minecraft.getInstance();
      if (lastLevel != var0.level || lastPlayer != var0.player) {
         reset();
         lastLevel = var0.level;
         lastPlayer = var0.player;
      }
   }

   public static int textWidth(String var0) {
      return textWidthAtSize(var0, 13.0);
   }

   public static int textWidthAtSize(String var0, double var1) {
      return (int)Math.ceil(SdfText.measureWidth(var0, var1));
   }

   public static String truncate(String var0, double var1, double var3) {
      return SdfText.truncate(var0, var1, var3);
   }

   public static int truncate(int var0, int var1) {
      return var0 + var1 + (int)Math.ceil(54.0);
   }

   public static int effectRowWidth(int var0, int var1, int var2) {
      return 26 + (int)Math.ceil(16.799999999999997) + var2 + var0 + 48 + var1;
   }

   public static float smooth(float var0, boolean var1) {
      return smoothByDelta(var0, var1, 0.016666666666666666);
   }

   public static float smoothByDelta(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.pow(0.85, Math.clamp(var2, 0.0, 0.25) * 60.0);
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   private static double deltaSeconds(long var0, long var2) {
      return var0 == 0L ? 0.016666666666666666 : Math.max(0L, var2 - var0) / 1.0E9;
   }

   public static String formatDuration(int var0, boolean var1) {
      int var2 = Math.max(0, var0 / 20);
      return var1 ? "∞" : String.format(Locale.ROOT, "%d:%02d", var2 / 60, var2 % 60);
   }

   private static HudPanels.Panel buildPanel(String var0, List<HudPanels.Row> var1, boolean var2) {
      int var3 = Math.max(168, textWidth(var0) + 28 + 24);
      double var4 = 48.0;

      for (HudPanels.Row var7 : var1) {
         var3 = Math.max(var3, var7.width());
         var4 += (var2 ? 22 : 23) * var7.alpha();
      }

      return new HudPanels.Panel(var0, List.copyOf(var1), var3, (int)Math.ceil(var4), var2);
   }

   public static HudPanels.Panel keybindsPanel(boolean var0) {
      resetOnWorldChange();
      long var1 = System.nanoTime();
      double var3 = deltaSeconds(lastKeybindsNanos, var1);
      if (!var0) {
         lastKeybindsNanos = var1;
      }

      ArrayList var5 = new ArrayList();

      for (Module var7 : LarpClient.getInstance().getModuleManager().getVisibleModules()) {
         HUDModule var8 = HUDModule.getInstance();
         int var9 = var7.getKeyBind() < 0 || !var7.isEnabled() && (var8 == null || !var8.keybindDisplay.isMode("Always")) ? 0 : 1;
         float var10 = var0 ? (var9 != 0 ? 1 : 0) : smoothByDelta(keybindAlpha.getOrDefault(var7, 0.0F), (boolean)var9, var3);
         if (var10 < 0.05F && var9 == 0) {
            if (!var0) {
               keybindAlpha.remove(var7);
            }
         } else {
            if (!var0) {
               keybindAlpha.put(var7, var10);
            }

            if (!(var10 <= 0.05F)) {
               String var11 = var7.getDisplayName();
               String var12 = var7.getKeyBind() < 0 ? "None" : BindCodes.displayName(var7.getKeyBind()).toUpperCase(Locale.ROOT);
               var5.add(new HudPanels.Row(var11, var12, null, truncate(textWidth(var11), textWidth(var12)), var10));
            }
         }
      }

      if (var0 && var5.isEmpty()) {
         var5.add(new HudPanels.Row("Backtrack", "R", null, truncate(textWidth("Backtrack"), textWidth("R")), 1.0F));
         var5.add(new HudPanels.Row("Speed", "V", null, truncate(textWidth("Speed"), textWidth("V")), 1.0F));
      }

      return buildPanel("KeyBinds", var5, true);
   }

   public static HudPanels.Panel blackjackPanel(boolean var0) {
      CasinoGameModule var1 = CasinoGameModule.getActiveGame();
      String var2 = var1 == null ? "Blackjack" : var1.getDisplayName();
      List var3 = var1 == null
         ? List.of(new String[]{"Drawing", "Dealer"}, new String[]{"Player", "18"}, new String[]{"Dealer", "6"}, new String[]{"Dealer plan", "A"})
         : var1.getHudRows();
      ArrayList var4 = new ArrayList();

      for (String[] var6 : var3) {
         var4.add(new HudPanels.Row(var6[0], var6[1], null, truncate(textWidth(var6[0]), textWidth(var6[1])), 1.0F));
      }

      HudPanels.Panel var7 = buildPanel(var2, var4, true);
      return new HudPanels.Panel(var7.title(), var7.rows(), var7.width(), var7.height(), true, "store");
   }

   public static HudPanels.Panel potionsPanel(boolean var0) {
      resetOnWorldChange();
      long var1 = System.nanoTime();
      double var3 = deltaSeconds(lastPotionsNanos, var1);
      if (!var0) {
         lastPotionsNanos = var1;
      }

      Minecraft var5 = Minecraft.getInstance();
      ArrayList var6 = new ArrayList();
      HashSet var7 = new HashSet();
      if (var5.player != null) {
         for (MobEffectInstance var9 : var5.player.getActiveEffects()) {
            Identifier var10 = BuiltInRegistries.MOB_EFFECT.getKey((MobEffect)var9.getEffect().value());
            var7.add(var10);
            float var11 = var0 ? 1.0F : smoothByDelta(effectAlpha.getOrDefault(var10, 0.0F), true, var3);
            if (!var0) {
               effectAlpha.put(var10, var11);
            }

            String var12 = ((MobEffect)var9.getEffect().value()).getDisplayName().getString()
               + (var9.getAmplifier() > 0 ? " " + (var9.getAmplifier() + 1) : "");
            String var13 = formatDuration(var9.getDuration(), var9.isInfiniteDuration());
            Identifier var14 = Identifier.fromNamespaceAndPath(var10.getNamespace(), "mob_effect/" + var10.getPath());
            HudPanels.Row var15 = new HudPanels.Row(var12, var13, var14, effectRowWidth(textWidth(var12), textWidth(var13), textWidth(">")), var11);
            var6.add(var15);
            if (!var0) {
               lastEffectRows.put(var10, var15);
            }
         }
      }

      if (!var0) {
         Iterator var16 = effectAlpha.entrySet().iterator();

         while (var16.hasNext()) {
            Entry var18 = (Entry)var16.next();
            if (!var7.contains(var18.getKey())) {
               float var20 = smoothByDelta((Float)var18.getValue(), false, var3);
               if (var20 <= 0.05F) {
                  var16.remove();
                  lastEffectRows.remove(var18.getKey());
               } else {
                  var18.setValue(var20);
                  HudPanels.Row var21 = lastEffectRows.get(var18.getKey());
                  if (var21 != null) {
                     var6.add(new HudPanels.Row(var21.label(), var21.value(), var21.icon(), var21.width(), var20));
                  }
               }
            }
         }
      }

      if (var0 && var6.isEmpty()) {
         for (String[] var19 : List.of(new String[]{"Speed 2", "0:45", "speed"}, new String[]{"Strength", "0:30", "strength"})) {
            var6.add(
               new HudPanels.Row(
                  var19[0],
                  var19[1],
                  Identifier.withDefaultNamespace("mob_effect/" + var19[2]),
                  effectRowWidth(textWidth(var19[0]), textWidth(var19[1]), textWidth(">")),
                  1.0F
               )
            );
         }
      }

      var6.sort(Comparator.comparingInt(HudPanels.Row::width).reversed());
      return buildPanel("Potions", var6, false);
   }

   public static void formatDuration(GuiGraphicsExtractor var0, String var1, double var2, double var4, int var6) {
      drawText(var0, var1, var2, var4, 13.0, var6);
   }

   public static void drawText(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      SdfText.drawText(var0, var1, var2, var4, var6, var8);
   }

   public static void drawIcon(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      Identifier var6 = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/larp/" + var1 + ".png");
      var0.blit(RenderPipelines.GUI_TEXTURED, var6, var2, var3, 0.0F, 0.0F, var4, var4, 16, 16, 16, 16, AlphaScope.applyAlpha(var5));
   }

   public static void drawIconOrFallback(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      if (!SdfText.drawIcon(var0, var1, var2, var3, var4, var5)) {
         Icons.texture(var0, Icons.get(var1), var2, var3, var4, var5);
      }
   }

   private static int rowHeight(HudPanels.Panel var0) {
      return var0.keys() ? 22 : 23;
   }

   public static List<HudLayout2.Rect> blackjackPanel(HudPanels.Panel var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.add(new HudLayout2.Rect(var1, var2, var0.width(), var0.height()));
      double var5 = var2 + 39;

      for (HudPanels.Row var8 : var0.rows()) {
         int var9 = var0.keys() ? 22 : 23;
         var4.add(new HudLayout2.Rect(var1, (int)Math.floor(var5), var0.width(), (int)Math.ceil(var9 * var8.alpha())));
         var5 += var9 * var8.alpha();
      }

      return var4;
   }

   public static void formatDuration(GuiGraphicsExtractor var0, HudPanels.Panel var1, int var2, int var3, boolean var4) {
      int var5 = var1.icon() != null ? 1 : 0;
      HudPanels2.renderFrame(
         var0, var2, var3, var1.width(), var1.height(), var1.title(), var5 != 0 ? var1.icon() : (var1.keys() ? "keyboard" : "flask-conical"), true
      );
      List var6 = blackjackPanel(var1, var2, var3, var4);

      for (int var7 = 0; var7 < var1.rows().size(); var7++) {
         HudPanels.Row var8 = var1.rows().get(var7);
         HudLayout2.Rect var9 = (HudLayout2.Rect)var6.get(var7 + 1);
         int var10 = var9.top();
         int var11 = var9.height();
         double var12 = var8.alpha();
         var0.enableScissor(var2, var10, var2 + var1.width(), var10 + var11);

         try {
            double var14 = SdfText.centerBaselineY(var10, rowHeight(var1), 13.0) - (rowHeight(var1) - var11) / 2.0;
            int var16 = var2 + 12;
            if (!var1.keys() && var5 == 0) {
               var0.blitSprite(RenderPipelines.GUI_TEXTURED, var8.icon(), var16, var10 + (var11 - 15) / 2, 15, 15, ColorUtil.multiplyAlpha(-1, var12));
               var16 += 22;
            }

            int var17 = textWidth(var8.value());
            int var18 = var17 + 12;
            int var19 = var2 + var1.width() - 12 - var17;
            int var20 = -1250068;
            if (var1.keys() && var5 == 0) {
               ModuleManager var21 = LarpClient.getInstance().getModuleManager();
               boolean var22 = var21.getModules().stream().anyMatch(var1x -> var1x.isEnabled() && var1x.getDisplayName().equals(var8.label()));
               var20 = var22 ? Theme.accent : -1494422292;
               DrawUtil.fillRoundedRect(var0, var19 - 6, var10 + (var11 - 17) / 2, var18, 17.0F, 4.0F, ColorUtil.multiplyAlpha(-1275068416, var12));
            }

            String var26 = truncate(var8.label(), var19 - var16 - 14, 13.0);
            formatDuration(var0, var26, var16, var14, ColorUtil.multiplyAlpha(var20, var12));
            formatDuration(var0, var8.value(), var19, var14, ColorUtil.multiplyAlpha(-4671294, var12));
         } finally {
            var0.disableScissor();
         }
      }
   }

   public static float[][] buildPanel(float var0, float var1, float var2, float var3, float var4) {
      var4 = Math.max(0.0F, Math.min(var4, Math.min(var2, var3) / 2.0F));
      ArrayList var5 = new ArrayList();
      var5.add(new float[]{var0, var1 + var3 / 2.0F});
      float[][] var6 = new float[][]{
         {var0 + var4, var1 + var4, (float) Math.PI},
         {var0 + var2 - var4, var1 + var4, (float) (Math.PI * 3.0 / 2.0)},
         {var0 + var2 - var4, var1 + var3 - var4, 0.0F},
         {var0 + var4, var1 + var3 - var4, (float) (Math.PI / 2)}
      };
      int var7 = Math.max(2, (int)Math.ceil(var4 / 1.5F));

      for (float[] var11 : var6) {
         for (int var12 = 0; var12 <= var7; var12++) {
            double var13 = var11[2] + (Math.PI / 2) * var12 / var7;
            var5.add(new float[]{(float)(var11[0] + Math.cos(var13) * var4), (float)(var11[1] + Math.sin(var13) * var4)});
         }
      }

      var5.add(new float[]{var0, var1 + var3 / 2.0F});
      return var5.toArray(new float[0][]);
   }

   public static float pathLength(float[][] var0) {
      float var1 = 0.0F;

      for (int var2 = 1; var2 < var0.length; var2++) {
         var1 += (float)Math.hypot(var0[var2][0] - var0[var2 - 1][0], var0[var2][1] - var0[var2 - 1][1]);
      }

      return var1;
   }

   private static void resetOnWorldChange(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, int var7) {
      float var8 = 0.8F;
      RoundedShapes.drawRim(var0, var1, var2, var3, var4, 12.0F, 486539263);
      if (!(var5 <= 0.001)) {
         float[][] var9 = buildPanel(var1 + var8, var2 + var8, var3 - var8 * 2.0F, var4 - var8 * 2.0F, 12.0F - var8);
         float var10 = pathLength(var9);
         float var11 = var10 * (float)Math.min(1.0, var5);
         float var12 = 0.0F;
         int var13 = var7 == -41892 ? var7 : ColorUtil.lerpColor(var7, Theme.accentEnd | 0xFF000000, 0.6);

         for (int var14 = 1; var14 < var9.length && var12 < var11; var14++) {
            float[] var15 = var9[var14 - 1];
            float[] var16 = var9[var14];
            float var17 = (float)Math.hypot(var16[0] - var15[0], var16[1] - var15[1]);
            if (!(var17 <= 0.0F)) {
               float var18 = Math.min(1.0F, (var11 - var12) / var17);
               float var19 = var15[0] + (var16[0] - var15[0]) * var18;
               float var20 = var15[1] + (var16[1] - var15[1]) * var18;
               int var21 = ColorUtil.lerpColor(var13, var7, Math.min(1.0F, (var12 + var17 * var18 / 2.0F) / Math.max(1.0F, var11)));
               DrawUtil.drawLine(var0, var15[0], var15[1], var19, var20, 1.6F, var21);
               var12 += var17 * var18;
            }
         }
      }
   }

   public static int cardWidth(String var0, String var1, String var2, String var3, String var4) {
      int var5 = textWidth(var0) + 28 + 24;
      int var6 = textWidth(var1) + textWidth(var2) + 14 + 24;
      int var7 = textWidthAtSize(var3, 11.0) + textWidthAtSize(var4, 11.0) + 14 + 24;
      return Math.clamp(Math.max(var5, Math.max(var6, var7)), 196, 300);
   }

   public static void resetOnWorldChange(
      GuiGraphicsExtractor var0,
      int var1,
      int var2,
      int var3,
      String var4,
      String var5,
      String var6,
      String var7,
      String var8,
      String var9,
      double var10,
      boolean var12
   ) {
      HudPanels2.renderFrame(var0, var1, var2, var3, 80, var4, var5, true);
      int var13 = var1 + 12;
      int var14 = var1 + var3 - 12;
      int var15 = var12 ? -41892 : Theme.accent | 0xFF000000;
      double var16 = SdfText.centerBaselineY(var2 + 37, 18.0, 13.0);
      int var18 = var7.isEmpty() ? 0 : textWidth(var7);
      drawText(var0, truncate(var6, var14 - var13 - var18 - 10, 13.0), var13, var16, 13.0, var12 ? -30070 : -1250068);
      if (!var7.isEmpty()) {
         drawText(var0, var7, var14 - var18, var16, 13.0, var15);
      }

      resetOnWorldChange(var0, var1, var2, var3, 80, var10, var15);
      double var19 = SdfText.drawIcon(var2 + 57, 14.0, 11.0);
      int var21 = var9.isEmpty() ? 0 : textWidthAtSize(var9, 11.0);
      drawText(var0, truncate(var8, var14 - var13 - var21 - (var9.isEmpty() ? 0 : 10), 11.0), var13, var19, 11.0, -4671294);
      if (!var9.isEmpty()) {
         drawText(var0, var9, var14 - var21, var19, 11.0, -7697770);
      }
   }

   public record Panel(String title, List<HudPanels.Row> rows, int width, int height, boolean keys, String icon) {
      public Panel(String var1, List<HudPanels.Row> var2, int var3, int var4, boolean var5) {
         this(var1, var2, var3, var4, var5, null);
      }
   }

   public record Row(String label, String value, Identifier icon, int width, float alpha) {
   }
}
