package com.larpclient.hud;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.render.HUDModule;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class KeystrokesHud {
   public static final int kEY_SIZE = 21;
   public static final int kEY_GAP = 4;
   public static final int sPACE_HEIGHT = 13;
   public static final int pADDING_INNER = 4;
   public static final int lABEL_FONT_SIZE = 10;
   public static final int cPS_FONT_SIZE = 7;
   public static final double fADE_IN_SPEED = 34.0;
   public static final double fADE_OUT_SPEED = 12.0;
   public static final long sUPPRESS_WINDOW_NANOS = 90000000L;
   private static final Map<String, Float> keyAnimations = new HashMap<>();
   private static final KeystrokesHud.ClickCounter leftClicks = new KeystrokesHud.ClickCounter();
   private static final KeystrokesHud.ClickCounter rightClicks = new KeystrokesHud.ClickCounter();
   private static Object lastLevel;
   private static long lastFrameNanos;
   private static long lastAttackNanos;
   private static long lastForwardSuppressNanos;

   private KeystrokesHud() {
   }

   public static boolean isEnabled() {
      HUDModule var0 = HUDModule.getInstance();
      return var0 != null && var0.getEffectSettings("keystrokes");
   }

   public static void recordClick(int var0) {
      if (isEnabled() && Minecraft.getInstance().gui.screen() == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? leftClicks : rightClicks).record_(System.nanoTime());
      }
   }

   public static void recordAttack() {
      if (isEnabled()) {
         long var0 = System.nanoTime();
         leftClicks.record_(var0);
         lastAttackNanos = var0;
      }
   }

   public static void recordForwardSuppress() {
      if (isEnabled()) {
         lastForwardSuppressNanos = System.nanoTime();
      }
   }

   public static boolean isRecent(long var0, long var2) {
      return var2 != 0L && var0 - var2 >= 0L && var0 - var2 < 90000000L;
   }

   public static int getCps(boolean var0) {
      return (var0 ? leftClicks : rightClicks).count(System.nanoTime());
   }

   public static int getWidth(HUDModule var0) {
      return getPadding(var0) * 2 + 63 + 8;
   }

   public static int getHeight(HUDModule var0) {
      int var1 = getPadding(var0) * 2 + 42 + 4;
      if (var0.keystrokesMouse.getValue()) {
         var1 += 25;
      }

      if (var0.keystrokesSpace.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int getPadding(HUDModule var0) {
      return var0.keystrokesPadding.getValue().intValue();
   }

   public static float approachAnimation(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   public static void render(GuiGraphicsExtractor var0, HUDModule var1, int var2, int var3, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      long var6 = System.nanoTime();
      double var8 = lastFrameNanos == 0L ? 0.016666666666666666 : (var6 - lastFrameNanos) / 1.0E9;
      lastFrameNanos = var6;
      if (lastLevel != var5.level) {
         keyAnimations.clear();
         leftClicks.clear();
         rightClicks.clear();
         lastForwardSuppressNanos = 0L;
         lastAttackNanos = 0L;
         lastLevel = var5.level;
      }

      int var10 = getWidth(var1);
      int var11 = getHeight(var1);
      DrawUtil.fillRoundedRect(var0, var2, var3, var10, var11, 8.0F, ColorUtil.withAlpha(-16119284, var1.keystrokesBgAlpha.getValue() / 255.0));
      HudRenderer2.drawGlow(var0, var2, var3, var10, var11, 8.0F);
      HudRenderer2.drawShine(var0, var2, var3, var10, var11, 8);
      Options var12 = var5.options;
      boolean var13 = var1.keystrokesArrows.getValue();
      int var14 = var2 + getPadding(var1);
      int var15 = var3 + getPadding(var1);
      byte var16 = 25;
      int var17 = isKeyDown(var12.keyUp, var4) && !isRecent(var6, lastForwardSuppressNanos) ? 1 : 0;
      drawKey(var0, var13 ? "↑" : "W", null, (boolean)var17, var14 + var16, var15, 21, 21, var8);
      var15 += var16;
      drawKey(var0, var13 ? "←" : "A", null, isKeyDown(var12.keyLeft, var4), var14, var15, 21, 21, var8);
      drawKey(var0, var13 ? "↓" : "S", null, isKeyDown(var12.keyDown, var4), var14 + var16, var15, 21, 21, var8);
      drawKey(var0, var13 ? "→" : "D", null, isKeyDown(var12.keyRight, var4), var14 + var16 * 2, var15, 21, 21, var8);
      var15 += var16;
      byte var18 = 71;
      if (var1.keystrokesMouse.getValue()) {
         int var19 = (var18 - 4) / 2;
         boolean var20 = var1.keystrokesCps.getValue();
         int var21 = !isKeyDown(var12.keyAttack, var4) && (var4 || !isRecent(var6, lastAttackNanos)) ? 0 : 1;
         drawKey(var0, "LMB", var20 ? getCps(true) + " CPS" : null, (boolean)var21, var14, var15, var19, 21, var8);
         drawKey(var0, "RMB", var20 ? getCps(false) + " CPS" : null, isKeyDown(var12.keyUse, var4), var14 + var18 - var19, var15, var19, 21, var8);
         var15 += var16;
      }

      if (var1.keystrokesSpace.getValue()) {
         float var24 = animateKey("Space", isKeyDown(var12.keyJump, var4), var8);
         drawKeyBackground(var0, var14, var15, var18, 13, var24);
         int var25 = Math.round(var18 * 0.3F);
         DrawUtil.fillRoundedRect(var0, var14 + (var18 - var25) / 2, var15 + 6 - 1, var25, 2.0F, 1.0F, ColorUtil.lerpColor(-7697772, -1, var24));
      }
   }

   private static boolean isKeyDown(KeyMapping var0, boolean var1) {
      return !var1 && var0.isDown();
   }

   private static float animateKey(String var0, boolean var1, double var2) {
      float var4 = approachAnimation(keyAnimations.getOrDefault(var0, 0.0F), var1, var2);
      keyAnimations.put(var0, var4);
      return var4;
   }

   private static void drawKeyBackground(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, float var5) {
      DrawUtil.fillRoundedRect(var0, var1, var2, var3, var4, 4.0F, ColorUtil.withAlpha(-1, 0.055 * (1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         RoundedShapes.drawGlow(
            var0,
            var1,
            var2,
            var3,
            var4,
            4.0F,
            6.0F * var5,
            ColorUtil.multiplyAlpha(Theme.accent, 0.45 * var5),
            ColorUtil.multiplyAlpha(Theme.accent, 0.45 * var5)
         );
         DrawUtil.fillRoundedRect(var0, var1, var2, var3, var4, 4.0F, ColorUtil.multiplyAlpha(Theme.accent, 0.9 * var5));
      }
   }

   private static void drawKey(GuiGraphicsExtractor var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = animateKey(var1, var3, var8);
      drawKeyBackground(var0, var4, var5, var6, var7, var10);
      int var11 = ColorUtil.lerpColor(Theme.textSecondary, -1, var10);
      double var12 = var4 + var6 / 2.0;
      if (var2 == null) {
         HudPanels.drawText(var0, var1, var12 - HudPanels.textWidthAtSize(var1, 10.0) / 2.0, SdfText.centerBaselineY(var5, var7, 10.0), 10.0, var11);
      } else {
         double var14 = var5 + var7 / 2.0 - 6.7640915F;
         HudPanels.drawText(var0, var1, var12 - HudPanels.textWidthAtSize(var1, 10.0) / 2.0, var14, 10.0, var11);
         HudPanels.drawText(var0, var2, var12 - HudPanels.textWidthAtSize(var2, 7.0) / 2.0, var14 + 10.0, 7.0, ColorUtil.lerpColor(-7697772, -1, var10));
      }
   }

   public static final class ClickCounter {
      public static final long windowNanos = 1000000000L;
      private final ArrayDeque<Long> timestamps = new ArrayDeque<>();

      public void record_(long var1) {
         this.timestamps.addLast(var1);
         this.prune(var1);
      }

      public int count(long var1) {
         this.prune(var1);
         return this.timestamps.size();
      }

      public void clear() {
         this.timestamps.clear();
      }

      private void prune(long var1) {
         while (!this.timestamps.isEmpty() && var1 - this.timestamps.peekFirst() > 1000000000L) {
            this.timestamps.removeFirst();
         }
      }
   }
}
