package com.larpclient.hud;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.render.HUDModule;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.data.SpeedHistory;
import com.larpclient.util.math.MonotoneSpline;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.resources.Identifier;

public final class HudRenderer2 {
   public static final int labelHeight = 17;
   public static final int speedCardWidth = 172;
   public static final int speedCardHeight = 72;
   public static final int headerSize = 42;
   public static final int gap = 6;
   public static final int speedHeaderHeight = 30;
   private static final int headerRadius = 9;
   private static final int padding = 12;
   private static final int segmentSpacing = 14;
   private static final int largeTextSize = 16;
   private static final int shineHalfWidth = 11;
   public static final int speedBodyColor = -603321844;
   public static final int expandHeight = 22;
   private static final String prefix = "XYZ";
   private static final double expandHeightDouble = 22.0;
   private static final double unusedScale = 4.0;
   private static final double cornerRadius = 6.0;
   public static final double glowAlpha = 0.09;
   public static final double borderAlpha = 0.12;
   public static final int infoRowOffset = 30;
   public static final int horizontalPadding = 12;
   public static final int graphWidth = 160;
   public static final int graphTop = 38;
   public static final int graphBaseline = 63;
   public static final double minGraphScale = 6.0;
   private static double graphScale = 6.0;
   private static long lastScaleNanos;

   private HudRenderer2() {
   }

   public static int panelColor(int var0) {
      return var0 + 12;
   }

   public static int panelColor() {
      return -267645934;
   }

   public static int accentColor() {
      return Theme.accent;
   }

   public static void drawTagPill(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, boolean var5) {
      int var6 = panelColor(var2);
      int var7 = var3 - var6;
      DrawUtil.fillRect(var0, var7, var4, var6, 17, panelColor());
      drawGradientStrip(var0, var7, var4, var6, 17, var5);
      drawShine(var0, var7, var4, var6, 17, 0);
      DrawUtil.fillGradient(var0, var5 ? var3 - 2 : var7, var4, 2, 17, Theme.accent, ColorUtil.lerpColor(Theme.accent, -1, 0.45));
      HudPanels.formatDuration(var0, var1, var7 + (var5 ? 5 : 7), SdfText.centerBaselineY(var4, 17.0, 13.0), Theme.accent);
   }

   private static List<HudRenderer2.Segment> buildSegments(String var0, String var1, String var2, String var3) {
      HUDModule var4 = HUDModule.getInstance();
      ArrayList var5 = new ArrayList();
      if (var4 == null || var4.watermarkUser.getValue()) {
         var5.add(new HudRenderer2.Segment(var0, HudRenderer2.SegmentKind.PLAYER));
      }

      if (var4 == null || var4.watermarkIp.getValue()) {
         var5.add(new HudRenderer2.Segment(var1, HudRenderer2.SegmentKind.SERVER));
      }

      if (var4 == null || var4.watermarkFps.getValue()) {
         var5.add(new HudRenderer2.Segment(var2, HudRenderer2.SegmentKind.FPS));
      }

      if (!var3.isEmpty() && (var4 == null || var4.watermarkTime.getValue())) {
         var5.add(new HudRenderer2.Segment(var3, HudRenderer2.SegmentKind.CLOCK));
      }

      return var5;
   }

   public static int computeWidth(int... var0) {
      return var0.length == 0 ? 42 : 72 + Arrays.stream(var0).sum() + (var0.length - 1) * 14;
   }

   public static int measureHud(String var0, String var1, String var2, String var3) {
      return computeWidth(buildSegments(var0, var1, var2, var3).stream().mapToInt(HudRenderer2.Segment::width).toArray());
   }

   public static int headerHeight(double var0) {
      return 42 + (int)Math.round(22.0 * Math.clamp(var0, 0.0, 1.0));
   }

   public static int drawTagPill(String var0) {
      return 24 + HudPanels.textWidthAtSize("XYZ " + var0, 13.0);
   }

   public static int animatedWidth(int var0, String var1, double var2) {
      int var4 = 48 + drawTagPill(var1);
      return var0 + (int)Math.round(Math.max(0, var4 - var0) * Math.clamp(var2, 0.0, 1.0));
   }

   public static HudLayout2.Rect drawHud(int var0, int var1, int var2, boolean var3, String var4) {
      return infoRect(var0, var1, var2, var3, drawTagPill(var4));
   }

   public static HudLayout2.Rect infoRect(int var0, int var1, int var2, boolean var3, int var4) {
      int var5 = var3 ? var0 : var0 + 42 + 6;
      int var6 = var2 - 42 - 6;
      int var7 = Math.min(var6, var4);
      return new HudLayout2.Rect(var3 ? var5 + var6 - var7 : var5, var1 + 6 + 30, var7, 22);
   }

   public static boolean[] cornerFlags(int var0, int var1, boolean var2) {
      int var3 = var0 > var1 ? 1 : 0;
      int var4 = var1 > var0 ? 1 : 0;
      int var5 = var0 <= 0 ? 1 : 0;
      return new boolean[]{
         false, false, var2 ? var3 == 0 : true, var2 ? true : var3 == 0, var5 == 0 && (!var2 || var4 == 0), var5 == 0 && (var2 || var4 == 0), false, false
      };
   }

   private static void drawSplitPanel(
      GuiGraphicsExtractor var0,
      int var1,
      float var2,
      int var3,
      float var4,
      int var5,
      int var6,
      int var7,
      boolean var8,
      boolean var9,
      boolean var10,
      boolean var11
   ) {
      if (var3 > 0 && !(var4 <= 0.0F)) {
         float var12 = var0.pose().m00();
         float var13 = var0.pose().m20();
         int var14 = (int)Math.floor(var13 + var12 * var1);
         int var15 = (int)Math.ceil(var13 + var12 * (var1 + var3));
         int var16 = Math.round(var13 + var12 * (var1 + var3 / 2.0F));
         drawClippedPanel(var0, var14, var16, var5, var6, var1, var2, var3, var4, var7, var8, var10);
         drawClippedPanel(var0, var16, var15, var5, var6, var1, var2, var3, var4, var7, var9, var11);
      }
   }

   public static float drawShine(float var0, float var1, float var2) {
      return var1 <= 0.0F ? var0 : (Math.round(var2 + var1 * var0) - var2) / var1;
   }

   private static void drawClippedPanel(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, float var6, int var7, float var8, int var9, boolean var10, boolean var11
   ) {
      if (var2 > var1 && var4 > var3) {
         int var12 = var10 ? var9 : 0;
         int var13 = var11 ? var9 : 0;
         var0.pose().pushMatrix();

         try {
            var0.pose().identity();
            var0.enableScissor(var1, var3, var2, var4);
         } finally {
            var0.pose().popMatrix();
         }

         try {
            DrawUtil.fillRoundedRect(var0, var5, var6 - var12, var7, var8 + var12 + var13, var9, panelColor());
            HUDModule var14 = HUDModule.getInstance();
            if (var14 == null || var14.getMusicSettings()) {
               int var15 = ColorUtil.lerpColor(Theme.accent, Theme.accentEnd, 0.5);
               RoundedShapes.fillRoundedGradientHorizontal(
                  var0,
                  var5,
                  var6 - var12,
                  var7,
                  var8 + var12 + var13,
                  var9,
                  ColorUtil.withAlpha(Theme.accent, 0.09 * Theme.accentSaturation()),
                  ColorUtil.withAlpha(var15, 0.0)
               );
            }
         } finally {
            var0.disableScissor();
         }
      }
   }

   public static void drawHud(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7) {
      drawHudWithInfo(var0, var1, var2, var3, var4, var5, var6, var7, "", 0.0);
   }

   public static void drawHudWithInfo(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7, String var8, double var9
   ) {
      HUDModule var11 = HUDModule.getInstance();
      int var12 = var11 != null && var11.watermarkLogoSide.isMode("Right") ? 1 : 0;
      var9 = Math.clamp(var9, 0.0, 1.0);
      double var13 = System.nanoTime() / 1.0E9;
      int var15 = headerHeight(var9);
      int var16 = var12 != 0 ? var1 + var3 - 42 : var1;
      DrawUtil.fillRoundedRect(var0, var16, var2, 42.0F, var15, 9.0F, panelColor());
      drawGlow(var0, var16, var2, 42.0F, var15, 9.0F);
      drawShine(var0, var16, var2, 42, var15, 9);
      int var17 = var2 + (var15 - 32) / 2;
      if (!SdfText.drawIcon(var0, "logo", var16 + 5, var17, 32.0, -1)) {
         DrawUtil.blitTexture(var0, Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png"), var16 + 5, var17, 32, 32, 64, 64);
      }

      List var18 = buildSegments(var4, var5, var6, var7);
      int var19 = var9 > 0.01 && !var8.isEmpty() ? 1 : 0;
      if (!var18.isEmpty() || var19 != 0) {
         int var20 = var12 != 0 ? var1 : var1 + 42 + 6;
         int var21 = var3 - 42 - 6;
         int var22 = var2 + 6;
         int var23 = var15 - 12;
         int var24 = var20 + var21;
         int var25 = var20;
         if (var19 == 0) {
            DrawUtil.fillRoundedRect(var0, var20, var22, var21, var23, 6.0F, panelColor());
            drawGlow(var0, var20, var22, var21, var23, 6.0F);
            drawShine(var0, var20, var22, var21, var23, 6);
         } else {
            int var26 = var18.isEmpty() ? 0 : Math.min(var21, measureHud(var4, var5, var6, var7) - 42 - 6);
            int var27 = Math.min(var21, drawTagPill(var8));
            int var28 = var22 + 30;
            int var29 = var23 - 30;
            var25 = var12 != 0 ? var24 - var26 : var20;
            int var30 = var12 != 0 ? var24 - var27 : var20;
            boolean[] var31 = cornerFlags(var26, var27, (boolean)var12);
            float var32 = var0.pose().m11();
            float var33 = var0.pose().m21();
            float var34 = drawShine(var28, var32, var33);
            int var35 = Math.round(var33 + var32 * var34);
            int var36 = var22 + var23;
            drawSplitPanel(var0, var25, var22, var26, var34 - var22, (int)Math.floor(var33 + var32 * var22), var35, 6, var31[0], var31[1], var31[2], var31[3]);
            drawSplitPanel(var0, var30, var34, var27, var36 - var34, var35, (int)Math.ceil(var33 + var32 * var36), 6, var31[4], var31[5], var31[6], var31[7]);
            if (var26 > 0) {
               drawShine(var0, var25, var22, var26, 30, 6);
            }

            drawShine(var0, var30, var28, var27, var29, 6);
            int var37 = Math.min(var26, var27);
            if (var37 > 24) {
               RoundedShapes.fillRounded(
                  var0, (var12 != 0 ? var24 - var37 : var20) + 12, var34, var37 - 24, 1.0F, 0.5F, ColorUtil.multiplyAlpha(675036230, var9)
               );
            }

            var0.enableScissor(var30, var28, var30 + var27, var28 + var29);

            try {
               double var38 = SdfText.centerBaselineY(var28, 22.0, 13.0);
               HudPanels.drawText(var0, "XYZ", var30 + 12, var38, 13.0, ColorUtil.multiplyAlpha(Theme.accent, var9));
               HudPanels.drawText(var0, " " + var8, var30 + 12 + HudPanels.textWidthAtSize("XYZ", 13.0), var38, 13.0, ColorUtil.multiplyAlpha(-1, var9));
            } finally {
               var0.disableScissor();
            }
         }

         int var44 = var25 + 12;
         double var45 = SdfText.drawIcon(var22, 30.0, 13.0);

         for (HudRenderer2.Segment var47 : var18) {
            int var48 = var44;
            switch (var47.kind()) {
               case PLAYER:
                  if (Minecraft.getInstance().player != null) {
                     PlayerFaceExtractor.extractRenderState(var0, Minecraft.getInstance().player.getSkin(), var44, var22 + 7, 16);
                  }

                  var44 += 22;
                  break;
               case FPS:
                  drawEqualizer(var0, var44, var22 + 8, var13);
                  var44 += 17;
            }

            if (var47.kind() == HudRenderer2.SegmentKind.CLOCK) {
               drawSegmentLabel(var0, var47.label(), var44, var45, var13);
            } else {
               HudPanels.drawText(var0, var47.label(), var44, var45, 13.0, -1);
            }

            var44 = var48 + var47.width() + 14;
         }
      }
   }

   private static void drawEqualizer(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      double[] var5 = new double[]{5.2, 7.1, 4.3};
      double[] var6 = new double[]{0.0, 1.7, 3.1};

      for (int var7 = 0; var7 < 3; var7++) {
         int var8 = 4 + (int)Math.round(10.0 * (0.5 + 0.5 * Math.sin(var3 * var5[var7] + var6[var7])));
         RoundedShapes.fillRounded(var0, var1 + var7 * 4, var2 + 14 - var8, 3.0F, var8, 1.0F, Theme.accent);
      }
   }

   private static void drawSegmentLabel(GuiGraphicsExtractor var0, String var1, int var2, double var3, double var5) {
      int var7 = var1.indexOf(58);
      if (var7 < 0) {
         HudPanels.drawText(var0, var1, var2, var3, 13.0, -1);
      } else {
         String var8 = var1.substring(0, var7 + 1);
         double var9 = 0.55 + 0.45 * Math.cos(var5 * Math.PI * 2.0);
         HudPanels.drawText(var0, var1.substring(0, var7), var2, var3, 13.0, -1);
         HudPanels.drawText(var0, ":", var2 + HudPanels.textWidthAtSize(var1.substring(0, var7), 13.0), var3, 13.0, ColorUtil.multiplyAlpha(-1, var9));
         HudPanels.drawText(var0, var1.substring(var7 + 1), var2 + HudPanels.textWidthAtSize(var8, 13.0), var3, 13.0, -1);
      }
   }

   public static void drawShine(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      drawShineAlpha(var0, var1, var2, var3, var4, var5, 1.0);
   }

   public static void drawShineAlpha(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      HUDModule var8 = HUDModule.getInstance();
      if ((var8 == null || var8.getWidgetToggles()) && !(var6 <= 0.01)) {
         double var9 = System.nanoTime() / 1.0E9 % 6.0 / 4.0;
         float var11 = var0.pose().m00();
         if (!(var9 >= 1.0) && !(var11 <= 0.0F)) {
            double var12 = -22.0 + var9 * (var0.guiWidth() + 44.0);
            double var14 = (var12 - var0.pose().m20()) / var11;
            double var16 = 11.0 / var11;
            int var18 = var1 + var5;
            int var19 = var1 + var3 - var5;
            if (!(var14 + var16 <= var18) && !(var14 - var16 >= var19)) {
               int var20 = (int)Math.round(var14 - var16);
               int var21 = Math.max(1, (int)Math.round(var16));
               int var22 = ColorUtil.multiplyAlpha(251658239, var6);
               var0.enableScissor(var18, var2, var19, var2 + var4);

               try {
                  RoundedShapes.fillRoundedGradientHorizontal(var0, var20, var2, var21, var4, 0.0F, var22 & 16777215, var22);
                  RoundedShapes.fillRoundedGradientHorizontal(var0, var20 + var21, var2, var21, var4, 0.0F, var22, var22 & 16777215);
               } finally {
                  var0.disableScissor();
               }
            }
         }
      }
   }

   public static void drawGlow(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5) {
      HUDModule var6 = HUDModule.getInstance();
      if ((var6 == null || var6.getMusicSettings()) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         double var7 = Theme.accentSaturation();
         int var9 = Theme.accent;
         int var10 = ColorUtil.lerpColor(Theme.accent, Theme.accentEnd, 0.5);
         RoundedShapes.fillRoundedGradientHorizontal(
            var0, var1, var2, var3, var4, var5, ColorUtil.withAlpha(var9, 0.09 * var7), ColorUtil.withAlpha(var10, 0.0)
         );
         int var11 = ColorUtil.withAlpha(var9, 0.12 * var7);
         RoundedShapes.drawRimGradient(var0, var1, var2, var3, var4, var5, var11, var11);
      }
   }

   public static void drawGradientStrip(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, boolean var5) {
      HUDModule var6 = HUDModule.getInstance();
      if ((var6 == null || var6.getMusicSettings()) && var3 > 0 && var4 > 0) {
         int var7 = ColorUtil.withAlpha(Theme.accent, 0.09 * Theme.accentSaturation());
         int var8 = var7 & 16777215;
         RoundedShapes.fillRoundedGradientHorizontal(var0, var1, var2, var3, var4, 0.0F, var5 ? var8 : var7, var5 ? var7 : var8);
      }
   }

   public static int animatedWidth(String var0) {
      return 12 + HudPanels.textWidthAtSize("XYZ " + var0, 15.0);
   }

   public static void drawInfoBox(GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4) {
      DrawUtil.fillRoundedRect(var0, var1, var2, var3, 27.0F, 6.0F, panelColor());
      drawGlow(var0, var1, var2, var3, 27.0F, 6.0F);
      drawShine(var0, var1, var2, var3, 27, 6);
      double var5 = SdfText.centerBaselineY(var2, 27.0, 15.0);
      HudPanels.drawText(var0, "XYZ", var1 + 6, var5, 15.0, Theme.accent);
      HudPanels.drawText(var0, " " + var4, var1 + 6 + HudPanels.textWidthAtSize("XYZ", 15.0), var5, 15.0, -1);
   }

   public static String infoRect(double var0) {
      return String.format(Locale.ROOT, "%.2f bps", var0);
   }

   public static void drawSpeedGraph(GuiGraphicsExtractor var0, int var1, int var2, SpeedHistory var3, boolean var4) {
      HUDModule var5 = HUDModule.getInstance();
      int var6 = var5 != null && !var5.speedCardBackground.getValue() ? 0 : 1;
      int var7 = var6 != 0 ? -267645934 : var5.speedHeader.getValue();
      int var8 = var6 != 0 ? -603321844 : var5.speedBackground.getValue();
      int var9 = var5 == null ? -1841171 : var5.speedLine.getValue();
      var0.enableScissor(var1, var2 + 30, var1 + 172, var2 + 72);
      DrawUtil.fillRoundedRect(var0, var1, var2, 172.0F, 72.0F, 12.0F, var8);
      var0.disableScissor();
      var0.enableScissor(var1, var2, var1 + 172, var2 + 30);
      DrawUtil.fillRoundedRect(var0, var1, var2, 172.0F, 72.0F, 12.0F, var7);
      var0.disableScissor();
      drawGlow(var0, var1, var2, 172.0F, 72.0F, 12.0F);
      drawShine(var0, var1, var2, 172, 72, 12);
      if (var6 != 0) {
         RoundedShapes.fillRounded(var0, var1 + 2, var2 + 30, 168.0F, 1.0F, 0.5F, 675036230);
      }

      HudPanels.drawText(var0, "Speed", var1 + 12, SdfText.centerBaselineY(var2, 30.0, 13.0), 13.0, drawTagPill(var7));
      double var10 = var4 ? 4.3 : var3.latest();
      String var12 = String.format(Locale.ROOT, "%.2f", var10);
      String var13 = " bps";
      double var14 = SdfText.centerBaselineY(var2, 30.0, 13.0);
      double var16 = var14 + 1.6545454263687132;
      int var18 = HudPanels.textWidthAtSize(var13, 10.4);
      int var19 = var1 + 172 - 12;
      HudPanels.drawText(var0, var12, var19 - var18 - HudPanels.textWidthAtSize(var12, 13.0), var14, 13.0, drawTagPill(var7));
      HudPanels.drawText(var0, var13, var19 - var18, var16, 10.4, ColorUtil.multiplyAlpha(drawTagPill(var7), 0.55));
      int var20 = var4 ? 40 : Math.min(40, var3.size());
      double var21 = 0.0;

      for (int var23 = 0; var23 < var20; var23++) {
         var21 = Math.max(var21, sampleSpeed(var3, var4, var23, var20));
      }

      double var45 = drawSplitPanel(var21, var4);
      int var25 = var1 + 12;
      short var26 = 148;
      int var27 = var2 + 63;
      int var28 = Math.round(graphY(var2, var45 / 2.0, var45));

      for (byte var29 = 0; var29 < var26; var29 += 6) {
         DrawUtil.fillRect(var0, var25 + var29, var28, Math.min(3, var26 - var29), 1, ColorUtil.multiplyAlpha(var9, 0.1));
      }

      DrawUtil.fillRect(var0, var25, var27, var26, 1, ColorUtil.multiplyAlpha(var9, 0.16));
      if (var20 >= 2) {
         for (int var46 = 0; var46 <= var26; var46++) {
            float var30 = graphY(var2, drawInfoBox(var3, var4, var20, (double)var46 / var26), var45);
            int var31 = Math.round(var30);
            if (var27 - var31 > 0) {
               DrawUtil.fillGradient(var0, var25 + var46, var31, 1, var27 - var31, ColorUtil.multiplyAlpha(var9, 0.26), ColorUtil.multiplyAlpha(var9, 0.0));
            }
         }

         float var47 = var25;
         float var48 = graphY(var2, sampleSpeed(var3, var4, 0, var20), var45);

         for (int var49 = 0; var49 + 1 < var20; var49++) {
            double var32 = sampleSpeed(var3, var4, Math.max(0, var49 - 1), var20);
            double var34 = sampleSpeed(var3, var4, var49, var20);
            double var36 = sampleSpeed(var3, var4, var49 + 1, var20);
            double var38 = sampleSpeed(var3, var4, Math.min(var20 - 1, var49 + 2), var20);

            for (int var40 = 1; var40 <= 4; var40++) {
               double var41 = var40 / 4.0;
               float var43 = var25 + (float)((var49 + var41) * var26 / (var20 - 1));
               float var44 = graphY(var2, MonotoneSpline.interpolate(var32, var34, var36, var38, var41), var45);
               var0.pose().pushMatrix();
               var0.pose().translate(var47, var48).rotate((float)Math.atan2(var44 - var48, var43 - var47));
               RoundedShapes.fillRounded(var0, 0.0F, -0.25F, (float)Math.hypot(var43 - var47, var44 - var48) + 0.5F, 1.5F, 0.75F, var9);
               var0.pose().popMatrix();
               var47 = var43;
               var48 = var44;
            }
         }

         RoundedShapes.fillRounded(var0, var47 - 3.5F, var48 - 3.5F, 7.0F, 7.0F, 3.5F, ColorUtil.multiplyAlpha(var9, 0.25));
         RoundedShapes.fillRounded(var0, var47 - 2.0F, var48 - 2.0F, 4.0F, 4.0F, 2.0F, var9);
      }
   }

   private static double drawInfoBox(SpeedHistory var0, boolean var1, int var2, double var3) {
      double var5 = Math.clamp(var3, 0.0, 1.0) * (var2 - 1);
      int var7 = Math.min(var2 - 2, (int)Math.floor(var5));
      return MonotoneSpline.interpolate(
         sampleSpeed(var0, var1, Math.max(0, var7 - 1), var2),
         sampleSpeed(var0, var1, var7, var2),
         sampleSpeed(var0, var1, var7 + 1, var2),
         sampleSpeed(var0, var1, Math.min(var2 - 1, var7 + 2), var2),
         var5 - var7
      );
   }

   private static double drawSplitPanel(double var0, boolean var2) {
      double var3 = Math.max(6.0, var0 * 1.15);
      if (var2) {
         return var3;
      }

      long var5 = System.nanoTime();
      double var7 = lastScaleNanos == 0L ? 1.0 : Math.clamp((var5 - lastScaleNanos) / 1.0E9, 0.0, 0.25);
      lastScaleNanos = var5;
      graphScale = graphScale + (var3 - graphScale) * (1.0 - Math.exp(-6.0 * var7));
      return Math.max(graphScale, var0);
   }

   private static double sampleSpeed(SpeedHistory var0, boolean var1, int var2, int var3) {
      return var1 ? (Math.sin(var2 * 0.18) + 1.0) * 2.2 : (var3 == 0 ? 0.0 : var0.get(var0.size() - var3 + var2));
   }

   public static float panelColor(int var0, double var1) {
      return graphY(var0, var1, 6.0);
   }

   public static float graphY(int var0, double var1, double var3) {
      return var0 + 63 - (float)(Math.clamp(var1 / Math.max(0.01, var3), 0.0, 1.0) * 25.0);
   }

   public static int drawTagPill(int var0) {
      double var1 = (var0 >> 16 & 0xFF) * 0.2126 + (var0 >> 8 & 0xFF) * 0.7152 + (var0 & 0xFF) * 0.0722;
      return var1 > 150.0 ? -15197408 : -986637;
   }

   private record Segment(String label, HudRenderer2.SegmentKind kind) {
      int width() {
         int var1 = HudPanels.textWidthAtSize(this.label, 13.0);

         return switch (this.kind) {
            case PLAYER -> 22 + var1;
            case FPS -> 17 + var1;
            default -> var1;
         };
      }
   }

   private enum SegmentKind {
      PLAYER,
      SERVER,
      FPS,
      CLOCK;
   }
}
